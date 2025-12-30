package com.example.foodcalu.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodcalu.R;
import com.example.foodcalu.data.entity.DietRecord;
import com.example.foodcalu.data.model.AiDietItem;
import com.example.foodcalu.data.model.AiDietResult;
import com.example.foodcalu.ui.adapter.DietRecordAdapter;
import com.example.foodcalu.ui.viewmodel.DietViewModel;
import com.example.foodcalu.util.DateUtils;
import com.example.foodcalu.util.DeepSeekCalorieService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DietMainActivity extends AppCompatActivity {
    private DietViewModel viewModel;
    private DietRecordAdapter adapter;
    private TextView totalText;
    private TextView dateText;
    private TextView emptyText;
    private long selectedDate;
    private int dialogRequestId = 0;
    private AlertDialog activeDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_main);

        dateText = findViewById(R.id.text_date);
        totalText = findViewById(R.id.text_total);
        emptyText = findViewById(R.id.text_empty);
        Button addButton = findViewById(R.id.button_add_diet);
        RecyclerView recyclerView = findViewById(R.id.recycler_diet);

        adapter = new DietRecordAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        selectedDate = DateUtils.today();
        dateText.setText(String.format(Locale.CHINA, "日期：%s", DateUtils.formatDate(selectedDate)));

        viewModel = new ViewModelProvider(this).get(DietViewModel.class);
        viewModel.setDate(selectedDate);
        viewModel.getRecords().observe(this, records -> {
            adapter.setItems(records);
            boolean isEmpty = records == null || records.isEmpty();
            emptyText.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        });
        viewModel.getTotalCalories().observe(this, total -> {
            float value = total == null ? 0f : total;
            totalText.setText(String.format(Locale.CHINA, "今日总摄入：%.0f 千卡", value));
        });

        addButton.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        android.view.View view = inflater.inflate(R.layout.dialog_add_diet, null);
        EditText input = view.findViewById(R.id.input_diet_text);
        TextView tip = view.findViewById(R.id.text_ai_tip);

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle("添加饮食")
                .setView(view)
                .setNegativeButton("取消", null)
                .setPositiveButton("识别并保存", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            positive.setOnClickListener(v -> {
                String text = input.getText().toString().trim();
                if (text.isEmpty()) {
                    Toast.makeText(this, "请输入饮食描述", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!DeepSeekCalorieService.isConfigured()) {
                    Toast.makeText(this, "未配置 API Key", Toast.LENGTH_SHORT).show();
                    return;
                }
                int requestId = ++dialogRequestId;
                activeDialog = dialog;
                positive.setEnabled(false);
                negative.setEnabled(false);
                positive.setText("识别中...");
                tip.setText("正在识别中，请稍候");

                DeepSeekCalorieService.requestAnalysis(text, new DeepSeekCalorieService.AiResultCallback() {
                    @Override
                    public void onSuccess(AiDietResult result, String rawContent) {
                        runOnUiThread(() -> {
                            if (requestId != dialogRequestId || activeDialog == null) {
                                return;
                            }
                            List<DietRecord> records = buildRecords(text, result.items);
                            if (records.isEmpty()) {
                                restoreDialogState(dialog, positive, negative, tip);
                                Toast.makeText(DietMainActivity.this, "识别结果为空", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            viewModel.insertRecords(records);
                            dialog.dismiss();
                            Toast.makeText(DietMainActivity.this, "已保存", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onError(String message) {
                        runOnUiThread(() -> {
                            if (requestId != dialogRequestId || activeDialog == null) {
                                return;
                            }
                            restoreDialogState(dialog, positive, negative, tip);
                            Toast.makeText(DietMainActivity.this, "识别失败，请重试", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            });
        });

        dialog.setOnDismissListener(d -> {
            dialogRequestId++;
            activeDialog = null;
        });

        dialog.show();
    }

    private void restoreDialogState(AlertDialog dialog, Button positive, Button negative, TextView tip) {
        if (!dialog.isShowing()) {
            return;
        }
        positive.setEnabled(true);
        negative.setEnabled(true);
        positive.setText("识别并保存");
        tip.setText("由 AI 自动估算热量，仅供参考");
    }

    private List<DietRecord> buildRecords(String rawInput, List<AiDietItem> items) {
        List<DietRecord> records = new ArrayList<>();
        if (items == null) {
            return records;
        }
        for (AiDietItem item : items) {
            if (item == null || item.food == null || item.food.trim().isEmpty()) {
                continue;
            }
            records.add(new DietRecord(
                    0,
                    selectedDate,
                    rawInput,
                    item.food.trim(),
                    item.estimatedWeightG,
                    item.caloriesKcal
            ));
        }
        return records;
    }
}
