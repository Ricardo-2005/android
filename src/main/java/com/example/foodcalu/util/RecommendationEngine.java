package com.example.foodcalu.util;

import com.example.foodcalu.data.entity.UserProfile;
import com.example.foodcalu.data.entity.WorkoutCategory;
import com.example.foodcalu.data.entity.WorkoutItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RecommendationEngine {
    private RecommendationEngine() {
    }

    public static List<WorkoutItem> recommend(UserProfile profile, List<WorkoutCategory> categories,
                                              List<WorkoutItem> items) {
        List<WorkoutItem> result = new ArrayList<>();
        if (profile == null || items == null || items.isEmpty() || categories == null) {
            return result;
        }

        Map<Long, List<WorkoutItem>> grouped = new HashMap<>();
        for (WorkoutItem item : items) {
            if (!grouped.containsKey(item.categoryId)) {
                grouped.put(item.categoryId, new ArrayList<>());
            }
            grouped.get(item.categoryId).add(item);
        }

        long bodyweightId = findCategoryId(categories, "徒手基础");
        long cardioId = findCategoryId(categories, "有氧燃脂");
        long stretchId = findCategoryId(categories, "拉伸恢复");

        float bmi = BmiUtils.calcBmi(profile.weightKg, profile.heightCm);
        String bmiCategory = BmiUtils.bmiCategory(bmi);

        if ("减脂".equalsIgnoreCase(profile.goal)) {
            addFirst(result, grouped.get(cardioId), 2);
            if ("超重".equals(bmiCategory) || "肥胖".equals(bmiCategory)) {
                addFirst(result, grouped.get(stretchId), 1);
            }
            addFirst(result, grouped.get(bodyweightId), 1);
        } else if ("增肌".equalsIgnoreCase(profile.goal)) {
            addFirst(result, grouped.get(bodyweightId), 2);
            addFirst(result, grouped.get(cardioId), 1);
            addFirst(result, grouped.get(stretchId), 1);
        } else {
            addFirst(result, grouped.get(bodyweightId), 1);
            addFirst(result, grouped.get(cardioId), 1);
            addFirst(result, grouped.get(stretchId), 1);
        }

        return result;
    }

    private static long findCategoryId(List<WorkoutCategory> categories, String name) {
        for (WorkoutCategory category : categories) {
            if (name.equalsIgnoreCase(category.name)) {
                return category.id;
            }
        }
        return -1;
    }

    private static void addFirst(List<WorkoutItem> result, List<WorkoutItem> source, int count) {
        if (source == null || source.isEmpty()) {
            return;
        }
        for (int i = 0; i < source.size() && count > 0; i++) {
            WorkoutItem item = source.get(i);
            if (!result.contains(item)) {
                result.add(item);
                count--;
            }
        }
    }
}
