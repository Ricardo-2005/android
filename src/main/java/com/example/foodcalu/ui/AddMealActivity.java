package com.example.foodcalu.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodcalu.R;
import com.example.foodcalu.data.entity.Food;
import com.example.foodcalu.data.entity.MealRecord;
import com.example.foodcalu.repository.AppRepository;
import com.example.foodcalu.ui.viewmodel.AddMealViewModel;
import com.example.foodcalu.util.CalorieCalculator;
import com.example.foodcalu.util.DeepSeekCalorieService;
import com.example.foodcalu.util.DateUtils;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddMealActivity extends AppCompatActivity {
    public static final String EXTRA_DATE = "extra_date";
    public static final String EXTRA_FOOD_ID = "extra_food_id";

    private AddMealViewModel viewModel;
    private Spinner mealTypeSpinner;
    private Spinner foodSpinner;
    private Spinner unitSpinner;
    private EditText inputQuantity;
    private TextView dateText;
    private TextView kcalText;
    private Button saveButton;

    private long selectedDate;
    private long preselectFoodId;
    private final List<Food> foods = new ArrayList<>();
    private final Handler aiHandler = new Handler(Looper.getMainLooper());
    private Runnable aiRunnable;
    private int aiRequestId = 0;
    private String lastAiKey = "";
    private float lastEstimatedWeight = 0f;
    private float lastCalculatedKcal = 0f;
    private boolean aiPending = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        mealTypeSpinner = findViewById(R.id.spinner_meal_type);
        foodSpinner = findViewById(R.id.spinner_food);
        unitSpinner = findViewById(R.id.spinner_unit);
        inputQuantity = findViewById(R.id.input_quantity);
        dateText = findViewById(R.id.text_add_date);
        kcalText = findViewById(R.id.text_kcal_value);
        Button pickDate = findViewById(R.id.button_pick_date);
        saveButton = findViewById(R.id.button_save_meal);

        selectedDate = getIntent().getLongExtra(EXTRA_DATE, DateUtils.today());
        preselectFoodId = getIntent().getLongExtra(EXTRA_FOOD_ID, -1);
        updateDateText();

        ArrayAdapter<CharSequence> mealAdapter = ArrayAdapter.createFromResource(
                this, R.array.meal_types, android.R.layout.simple_spinner_item);
        mealAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeSpinner.setAdapter(mealAdapter);

        ArrayAdapter<String> foodAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, new ArrayList<>());
        foodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodSpinner.setAdapter(foodAdapter);

        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(
                this, R.array.unit_options, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);

        viewModel = new ViewModelProvider(this).get(AddMealViewModel.class);
        viewModel.getFoods().observe(this, list -> {
            foods.clear();
            if (list != null) {
                foods.addAll(list);
            }
            List<String> names = new ArrayList<>();
            for (Food food : foods) {
                names.add(food.name);
            }
            foodAdapter.clear();
            foodAdapter.addAll(names);
            foodAdapter.notifyDataSetChanged();
            applyPreselectFood();
            updateKcal();
        });

        inputQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateKcal();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        foodSpinner.setOnItemSelectedListener(new SimpleItemSelectedListener(this::updateKcal));
        unitSpinner.setOnItemSelectedListener(new SimpleItemSelectedListener(() -> {
            updateQuantityHint();
            updateKcal();
        }));
        pickDate.setOnClickListener(v -> openDatePicker());
        saveButton.setOnClickListener(v -> saveMeal());

        updateQuantityHint();
        updateSaveEnabled(false);
    }

    private void updateDateText() {
        dateText.setText(DateUtils.formatDate(selectedDate));
    }

    private void applyPreselectFood() {
        if (preselectFoodId == -1) {
            return;
        }
        for (int i = 0; i < foods.size(); i++) {
            if (foods.get(i).id == preselectFoodId) {
                foodSpinner.setSelection(i);
                break;
            }
        }
    }

    private void updateQuantityHint() {
        String unit = getSelectedUnit();
        if (CalorieCalculator.UNIT_GRAM.equals(unit)) {
            inputQuantity.setHint("克数");
        } else {
            inputQuantity.setHint("数量");
        }
    }

    private void openDatePicker() {
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("选择日期")
                .setSelection(selectedDate)
                .build();
        picker.addOnPositiveButtonClickListener(selection -> {
            selectedDate = DateUtils.startOfDay(selection);
            updateDateText();
        });
        picker.show(getSupportFragmentManager(), "add_meal_date_picker");
    }

    private void updateKcal() {
        Food food = getSelectedFood();
        float quantity = parseQuantity();
        if (food == null || quantity <= 0f) {
            cancelAiRequest();
            lastCalculatedKcal = 0f;
            lastEstimatedWeight = 0f;
            lastAiKey = "";
            aiPending = false;
            setKcalText(0f);
            updateSaveEnabled(false);
            return;
        }

        String unit = getSelectedUnit();
        float estimatedWeight = CalorieCalculator.estimateTotalGrams(unit, quantity);
        lastEstimatedWeight = estimatedWeight;
        String requestKey = food.id + "|" + unit + "|" + quantity;
        if (requestKey.equals(lastAiKey) && lastCalculatedKcal > 0f && !aiPending) {
            setKcalText(lastCalculatedKcal);
            updateSaveEnabled(true);
            return;
        }

        lastAiKey = requestKey;
        lastCalculatedKcal = 0f;
        aiPending = true;
        setKcalLoadingText();
        updateSaveEnabled(false);
        requestAiCalories(food, unit, quantity);
    }

    private void setKcalText(float kcal) {
        if (kcal <= 0f) {
            kcalText.setText("0 千卡");
            return;
        }
        kcalText.setText(String.format(Locale.CHINA, "预计摄入：%.0f 千卡（API计算）", kcal));
    }

    private void setKcalLoadingText() {
        kcalText.setText("正在通过API计算...");
    }

    private void setKcalErrorText() {
        kcalText.setText("API计算失败，请重试");
    }

    private void requestAiCalories(Food food, String unit, float quantity) {
        if (!DeepSeekCalorieService.isConfigured()) {
            aiPending = false;
            setKcalErrorText();
            updateSaveEnabled(false);
            return;
        }
        cancelAiRequest();
        int requestId = ++aiRequestId;
        aiRunnable = () -> DeepSeekCalorieService.requestCalories(
                food.name,
                unit,
                quantity,
                new DeepSeekCalorieService.CalorieCallback() {
                    @Override
                    public void onSuccess(float calories, String rawContent) {
                        runOnUiThread(() -> {
                            if (requestId != aiRequestId || isFinishing() || isDestroyed()) {
                                return;
                            }
                            float safeCalories = Math.max(0f, calories);
                            lastCalculatedKcal = safeCalories;
                            aiPending = false;
                            setKcalText(safeCalories);
                            updateSaveEnabled(true);
                        });
                    }

                    @Override
                    public void onError(String message) {
                        runOnUiThread(() -> {
                            if (requestId != aiRequestId || isFinishing() || isDestroyed()) {
                                return;
                            }
                            aiPending = false;
                            setKcalErrorText();
                            updateSaveEnabled(false);
                        });
                    }
                }
        );
        aiHandler.postDelayed(aiRunnable, 500);
    }

    private void cancelAiRequest() {
        if (aiRunnable != null) {
            aiHandler.removeCallbacks(aiRunnable);
            aiRunnable = null;
        }
    }

    private void updateSaveEnabled(boolean enabled) {
        if (saveButton == null) {
            return;
        }
        saveButton.setEnabled(enabled);
        saveButton.setAlpha(enabled ? 1f : 0.6f);
    }

    private Food getSelectedFood() {
        int index = foodSpinner.getSelectedItemPosition();
        if (index >= 0 && index < foods.size()) {
            return foods.get(index);
        }
        return null;
    }

    private String getSelectedUnit() {
        Object item = unitSpinner.getSelectedItem();
        return item == null ? CalorieCalculator.UNIT_GRAM : item.toString();
    }

    private float parseQuantity() {
        String text = inputQuantity.getText().toString().trim();
        if (text.isEmpty()) {
            return 0f;
        }
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    private void saveMeal() {
        Food food = getSelectedFood();
        float quantity = parseQuantity();
        if (food == null) {
            Toast.makeText(this, "请选择食物", Toast.LENGTH_SHORT).show();
            return;
        }
        if (quantity <= 0f) {
            Toast.makeText(this, "请输入数量", Toast.LENGTH_SHORT).show();
            return;
        }
        if (aiPending || lastCalculatedKcal <= 0f) {
            Toast.makeText(this, "请等待API计算完成", Toast.LENGTH_SHORT).show();
            return;
        }

        String unit = getSelectedUnit();
        float estimatedWeight = lastEstimatedWeight > 0f
                ? lastEstimatedWeight
                : CalorieCalculator.estimateTotalGrams(unit, quantity);
        float caloriesToSave = lastCalculatedKcal;

        MealRecord record = new MealRecord(
                0,
                selectedDate,
                mealTypeSpinner.getSelectedItem().toString(),
                food.id,
                estimatedWeight,
                caloriesToSave,
                unit,
                estimatedWeight,
                caloriesToSave,
                System.currentTimeMillis()
        );

        AppRepository.getInstance(this).addMealRecord(record);
        Toast.makeText(this, "记录已保存", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        cancelAiRequest();
        super.onDestroy();
    }
}
