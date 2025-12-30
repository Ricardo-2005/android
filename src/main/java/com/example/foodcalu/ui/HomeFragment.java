package com.example.foodcalu.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodcalu.R;
import com.example.foodcalu.data.entity.UserProfile;
import com.example.foodcalu.ui.viewmodel.HomeViewModel;
import com.example.foodcalu.util.BmiUtils;
import com.example.foodcalu.util.TdeeCalculator;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private HomeViewModel viewModel;
    private TextView bmiValue;
    private TextView bmiCategory;
    private TextView bmiTip;
    private TextView recommendKcal;
    private TextView todayKcal;
    private TextView workoutCount;
    private TextView workoutDuration;
    private ProgressBar kcalProgress;
    private PieChart pieChart;
    private TextView chartEmpty;

    private int recommended = 0;
    private float intake = 0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        bmiValue = view.findViewById(R.id.text_bmi_value);
        bmiCategory = view.findViewById(R.id.text_bmi_category);
        bmiTip = view.findViewById(R.id.text_bmi_tip);
        recommendKcal = view.findViewById(R.id.text_recommend_kcal);
        todayKcal = view.findViewById(R.id.text_today_kcal);
        workoutCount = view.findViewById(R.id.text_today_workout_count);
        workoutDuration = view.findViewById(R.id.text_today_workout_duration);
        kcalProgress = view.findViewById(R.id.progress_kcal);
        pieChart = view.findViewById(R.id.chart_intake);
        chartEmpty = view.findViewById(R.id.text_chart_empty);
        setupChart();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        viewModel.getUserProfile().observe(getViewLifecycleOwner(), profile -> {
            recommended = updateProfile(profile);
            updateChart();
        });

        viewModel.getTodayKcal().observe(getViewLifecycleOwner(), kcal -> {
            intake = kcal == null ? 0f : kcal;
            todayKcal.setText(String.format("今日摄入：%.0f 千卡", intake));
            updateChart();
        });

        viewModel.getTodayWorkoutCount().observe(getViewLifecycleOwner(), count -> {
            int value = count == null ? 0 : count;
            workoutCount.setText(String.format("今日打卡：%d 次", value));
        });

        viewModel.getTodayWorkoutDuration().observe(getViewLifecycleOwner(), minutes -> {
            int value = minutes == null ? 0 : minutes;
            workoutDuration.setText(String.format("今日训练时长：%d 分钟", value));
        });
    }

    private int updateProfile(UserProfile profile) {
        if (profile == null) {
            bmiValue.setText("BMI：--");
            bmiCategory.setText("分类：--");
            bmiTip.setText("请完善档案以获得分析。");
            recommendKcal.setText("推荐摄入：-- 千卡");
            kcalProgress.setProgress(0);
            return 0;
        }
        float bmi = BmiUtils.calcBmi(profile.weightKg, profile.heightCm);
        String category = BmiUtils.bmiCategory(bmi);
        bmiValue.setText(String.format("BMI：%.1f", bmi));
        bmiCategory.setText(String.format("分类：%s", category));
        bmiTip.setText(BmiUtils.bmiTip(category));

        int tdee = TdeeCalculator.calculateTdee(profile);
        recommendKcal.setText(String.format("推荐摄入：%d 千卡", tdee));
        return tdee;
    }

    private void setupChart() {
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setDrawEntryLabels(false);
        pieChart.setUsePercentValues(false);
        pieChart.setHoleRadius(70f);
        pieChart.setTransparentCircleRadius(75f);
    }

    private void updateChart() {
        if (recommended <= 0) {
            chartEmpty.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
            kcalProgress.setProgress(0);
            return;
        }

        chartEmpty.setVisibility(View.GONE);
        pieChart.setVisibility(View.VISIBLE);

        int progress = Math.round((intake / recommended) * 100f);
        if (progress > 100) {
            progress = 100;
        }
        kcalProgress.setProgress(progress);

        float remaining = Math.max(recommended - intake, 0f);
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(intake, "已摄入"));
        entries.add(new PieEntry(remaining, "剩余"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{
                ContextCompat.getColor(requireContext(), R.color.green_500),
                ContextCompat.getColor(requireContext(), R.color.green_200)
        });
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setCenterText(String.format("摄入 %.0f / %d 千卡", intake, recommended));
        pieChart.invalidate();
        pieChart.animateY(800);
    }
}
