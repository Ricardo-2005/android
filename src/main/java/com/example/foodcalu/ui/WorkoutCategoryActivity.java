package com.example.foodcalu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodcalu.R;
import com.example.foodcalu.ui.adapter.WorkoutItemAdapter;
import com.example.foodcalu.repository.AppRepository;

public class WorkoutCategoryActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORY_ID = "extra_category_id";
    public static final String EXTRA_CATEGORY_NAME = "extra_category_name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_category);

        TextView title = findViewById(R.id.text_category_title);
        RecyclerView recycler = findViewById(R.id.recycler_category_workouts);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        long categoryId = getIntent().getLongExtra(EXTRA_CATEGORY_ID, -1);
        String categoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        title.setText(categoryName == null ? "训练列表" : categoryName);

        WorkoutItemAdapter adapter = new WorkoutItemAdapter(item -> {
            Intent intent = new Intent(this, WorkoutDetailActivity.class);
            intent.putExtra(WorkoutDetailActivity.EXTRA_WORKOUT_ID, item.id);
            startActivity(intent);
        });
        recycler.setAdapter(adapter);

        if (categoryId != -1) {
            AppRepository.getInstance(this).getWorkoutItemsByCategory(categoryId)
                    .observe(this, adapter::submitList);
        }
    }
}
