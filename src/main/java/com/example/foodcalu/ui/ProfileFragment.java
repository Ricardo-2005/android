package com.example.foodcalu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodcalu.R;
import com.example.foodcalu.data.entity.UserProfile;
import com.example.foodcalu.ui.viewmodel.ProfileViewModel;
import com.example.foodcalu.util.BmiUtils;
import com.example.foodcalu.util.TdeeCalculator;
import com.example.foodcalu.util.SessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProfileFragment extends Fragment {
    private ProfileViewModel viewModel;
    private EditText inputName;
    private EditText inputAge;
    private EditText inputHeight;
    private EditText inputWeight;
    private Spinner spinnerGender;
    private Spinner spinnerActivity;
    private Spinner spinnerGoal;
    private TextView bmiInfo;
    private TextView tdeeInfo;
    private long userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        inputName = view.findViewById(R.id.input_nickname);
        inputAge = view.findViewById(R.id.input_age);
        inputHeight = view.findViewById(R.id.input_height);
        inputWeight = view.findViewById(R.id.input_weight);
        spinnerGender = view.findViewById(R.id.spinner_gender);
        spinnerActivity = view.findViewById(R.id.spinner_activity);
        spinnerGoal = view.findViewById(R.id.spinner_goal);
        bmiInfo = view.findViewById(R.id.text_bmi_info);
        tdeeInfo = view.findViewById(R.id.text_tdee_info);
        Button save = view.findViewById(R.id.button_save_profile);
        Button logout = view.findViewById(R.id.button_logout);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.activity_options, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivity.setAdapter(activityAdapter);

        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.goal_options, android.R.layout.simple_spinner_item);
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGoal.setAdapter(goalAdapter);

        save.setOnClickListener(v -> saveProfile());
        logout.setOnClickListener(v -> confirmLogout());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        userId = SessionManager.getUserId(requireContext());
        viewModel.setUserId(userId);
        viewModel.getUserProfile().observe(getViewLifecycleOwner(), this::bindProfile);
    }

    private void bindProfile(UserProfile profile) {
        if (profile == null) {
            return;
        }
        inputName.setText(profile.nickname);
        inputAge.setText(String.valueOf(profile.age));
        inputHeight.setText(String.valueOf(profile.heightCm));
        inputWeight.setText(String.valueOf(profile.weightKg));
        setSpinnerValue(spinnerGender, profile.gender);
        setSpinnerValue(spinnerActivity, profile.activityLevel);
        setSpinnerValue(spinnerGoal, profile.goal);
        updateMetrics(profile);
    }

    private void saveProfile() {
        String name = inputName.getText().toString().trim();
        String ageText = inputAge.getText().toString().trim();
        String heightText = inputHeight.getText().toString().trim();
        String weightText = inputWeight.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ageText) || TextUtils.isEmpty(heightText)
                || TextUtils.isEmpty(weightText)) {
            Toast.makeText(requireContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        int age;
        float height;
        float weight;
        try {
            age = Integer.parseInt(ageText);
            height = Float.parseFloat(heightText);
            weight = Float.parseFloat(weightText);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "数字格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userId <= 0L) {
            Toast.makeText(requireContext(), "请先登录", Toast.LENGTH_SHORT).show();
            return;
        }
        UserProfile profile = new UserProfile(
                0,
                userId,
                name,
                spinnerGender.getSelectedItem().toString(),
                age,
                height,
                weight,
                spinnerActivity.getSelectedItem().toString(),
                spinnerGoal.getSelectedItem().toString(),
                System.currentTimeMillis()
        );

        viewModel.saveProfile(profile);
        updateMetrics(profile);
        Toast.makeText(requireContext(), "档案已保存", Toast.LENGTH_SHORT).show();
    }

    private void updateMetrics(UserProfile profile) {
        float bmi = BmiUtils.calcBmi(profile.weightKg, profile.heightCm);
        String category = BmiUtils.bmiCategory(bmi);
        int tdee = TdeeCalculator.calculateTdee(profile);
        bmiInfo.setText(String.format("BMI：%.1f（%s）", bmi, category));
        tdeeInfo.setText(String.format("推荐摄入（Mifflin-St Jeor）：%d 千卡", tdee));
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                return;
            }
        }
    }

    private void confirmLogout() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("退出登录")
                .setMessage("确认退出登录？")
                .setNegativeButton("取消", null)
                .setPositiveButton("退出", (dialog, which) -> {
                    SessionManager.logout(requireContext());
                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .show();
    }
}
