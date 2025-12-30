package com.example.foodcalu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodcalu.R;
import com.example.foodcalu.data.entity.WorkoutCategory;
import com.example.foodcalu.data.entity.WorkoutItem;
import com.example.foodcalu.data.model.CategoryStat;
import com.example.foodcalu.data.model.WorkoutDuration;
import com.example.foodcalu.ui.adapter.WorkoutCategoryAdapter;
import com.example.foodcalu.ui.adapter.WorkoutItemAdapter;
import com.example.foodcalu.ui.viewmodel.WorkoutViewModel;
import com.example.foodcalu.util.DateUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutFragment extends Fragment {
    private WorkoutViewModel viewModel;
    private WorkoutItemAdapter recommendationAdapter;
    private WorkoutCategoryAdapter categoryAdapter;
    private TextView recommendEmpty;
    private TextView durationEmpty;
    private TextView categoryEmpty;
    private TextView weeklyCount;
    private TextView weeklyDuration;
    private TextView weeklyKcal;
    private BarChart barChart;
    private PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        recommendEmpty = view.findViewById(R.id.text_recommend_empty);
        durationEmpty = view.findViewById(R.id.text_workout_chart_empty);
        categoryEmpty = view.findViewById(R.id.text_category_chart_empty);
        weeklyCount = view.findViewById(R.id.text_weekly_count);
        weeklyDuration = view.findViewById(R.id.text_weekly_duration);
        weeklyKcal = view.findViewById(R.id.text_weekly_kcal);
        barChart = view.findViewById(R.id.chart_workout_duration);
        pieChart = view.findViewById(R.id.chart_workout_category);

        RecyclerView recommendList = view.findViewById(R.id.recycler_recommend);
        recommendList.setLayoutManager(new LinearLayoutManager(getContext()));
        recommendationAdapter = new WorkoutItemAdapter(this::openWorkoutDetail);
        recommendList.setAdapter(recommendationAdapter);

        RecyclerView categoryList = view.findViewById(R.id.recycler_categories);
        categoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryAdapter = new WorkoutCategoryAdapter(this::openCategory);
        categoryList.setAdapter(categoryAdapter);

        setupCharts();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);

        viewModel.getRecommended().observe(getViewLifecycleOwner(), items -> {
            recommendationAdapter.submitList(items);
            recommendEmpty.setVisibility(items == null || items.isEmpty() ? View.VISIBLE : View.GONE);
        });

        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.submitList(categories);
        });

        viewModel.getWeekDurations().observe(getViewLifecycleOwner(), durations -> updateBarChart(durations));
        viewModel.getWeekCategoryStats().observe(getViewLifecycleOwner(), stats -> updatePieChart(stats));
        viewModel.getWeekWorkoutCount().observe(getViewLifecycleOwner(), count -> {
            int value = count == null ? 0 : count;
            weeklyCount.setText(String.format("本周打卡：%d 次", value));
        });
        viewModel.getWeekWorkoutCalories().observe(getViewLifecycleOwner(), kcal -> {
            int value = kcal == null ? 0 : kcal;
            weeklyKcal.setText(String.format("本周消耗估算：%d 千卡", value));
        });
    }

    private void openCategory(WorkoutCategory category) {
        Intent intent = new Intent(requireContext(), WorkoutCategoryActivity.class);
        intent.putExtra(WorkoutCategoryActivity.EXTRA_CATEGORY_ID, category.id);
        intent.putExtra(WorkoutCategoryActivity.EXTRA_CATEGORY_NAME, category.name);
        startActivity(intent);
    }

    private void openWorkoutDetail(WorkoutItem item) {
        Intent intent = new Intent(requireContext(), WorkoutDetailActivity.class);
        intent.putExtra(WorkoutDetailActivity.EXTRA_WORKOUT_ID, item.id);
        startActivity(intent);
    }

    private void setupCharts() {
        Description desc = new Description();
        desc.setText("");
        barChart.setDescription(desc);
        barChart.getAxisRight().setEnabled(false);
        pieChart.setDescription(desc);
        pieChart.setDrawEntryLabels(false);
        pieChart.setUsePercentValues(true);
        pieChart.setHoleRadius(70f);
        pieChart.setTransparentCircleRadius(75f);
    }

    private void updateBarChart(List<WorkoutDuration> durations) {
        if (durations == null || durations.isEmpty()) {
            durationEmpty.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.GONE);
            weeklyDuration.setText("本周训练时长：0 分钟");
            return;
        }
        durationEmpty.setVisibility(View.GONE);
        barChart.setVisibility(View.VISIBLE);

        Map<Long, Integer> map = new HashMap<>();
        for (WorkoutDuration duration : durations) {
            map.put(duration.date, duration.totalDuration);
        }

        List<Long> days = DateUtils.lastDays(7);
        List<BarEntry> entries = new ArrayList<>();
        int totalMinutes = 0;
        for (int i = 0; i < days.size(); i++) {
            long date = days.get(i);
            int value = map.containsKey(date) ? map.get(date) : 0;
            entries.add(new BarEntry(i, value));
            totalMinutes += value;
        }

        weeklyDuration.setText(String.format("本周训练时长：%d 分钟", totalMinutes));

        BarDataSet dataSet = new BarDataSet(entries, "分钟");
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.green_500));
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.6f);
        barChart.setData(data);
        barChart.animateY(800);
        barChart.invalidate();
    }

    private void updatePieChart(List<CategoryStat> stats) {
        if (stats == null || stats.isEmpty()) {
            categoryEmpty.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
            return;
        }
        categoryEmpty.setVisibility(View.GONE);
        pieChart.setVisibility(View.VISIBLE);

        List<PieEntry> entries = new ArrayList<>();
        for (CategoryStat stat : stats) {
            entries.add(new PieEntry(stat.totalDuration, stat.categoryName));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{
                ContextCompat.getColor(requireContext(), R.color.green_500),
                ContextCompat.getColor(requireContext(), R.color.green_700),
                ContextCompat.getColor(requireContext(), R.color.orange_500)
        });

        PieData data = new PieData(dataSet);
        data.setValueTextSize(12f);
        pieChart.setData(data);
        pieChart.setCenterText("训练类型");
        pieChart.invalidate();
        pieChart.animateY(800);
    }
}
