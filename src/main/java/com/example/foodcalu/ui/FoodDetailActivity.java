package com.example.foodcalu.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.bumptech.glide.Glide;
import com.example.foodcalu.R;
import com.example.foodcalu.data.entity.Food;
import com.example.foodcalu.repository.AppRepository;

public class FoodDetailActivity extends AppCompatActivity {
    public static final String EXTRA_FOOD_ID = "extra_food_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        ImageView image = findViewById(R.id.image_food);
        TextView name = findViewById(R.id.text_food_name);
        TextView kcal = findViewById(R.id.text_food_kcal);
        TextView macros = findViewById(R.id.text_food_macros);
        TextView tags = findViewById(R.id.text_food_tags);
        TextView reason = findViewById(R.id.text_food_reason);

        long id = getIntent().getLongExtra(EXTRA_FOOD_ID, -1);
        if (id == -1) {
            finish();
            return;
        }

        LiveData<Food> foodLive = AppRepository.getInstance(this).getFoodById(id);
        foodLive.observe(this, food -> {
            if (food == null) {
                return;
            }
            name.setText(food.name);
            kcal.setText(String.format("%.0f 千卡/100克", food.kcalPer100g));
            macros.setText(String.format("蛋白质 %.1fg | 脂肪 %.1fg | 碳水 %.1fg", food.protein, food.fat, food.carbs));
            tags.setText(food.tags == null ? "" : food.tags);
            reason.setText(buildReason(food));
            if (food.imageUrl != null && !food.imageUrl.isEmpty()) {
                image.setVisibility(View.VISIBLE);
                Glide.with(this).load(food.imageUrl).into(image);
            } else {
                image.setVisibility(View.GONE);
            }
        });
    }

    private String buildReason(Food food) {
        if (food.tags == null) {
            return "适合日常的均衡选择。";
        }
        String tagLower = food.tags.toLowerCase();
        if (tagLower.contains("高蛋白")) {
            return "高蛋白有助于肌肉恢复。";
        }
        if (tagLower.contains("高纤维")) {
            return "高纤维帮助增强饱腹感。";
        }
        if (tagLower.contains("低脂")) {
            return "低脂有助于控制热量。";
        }
        return "适合日常的均衡选择。";
    }
}
