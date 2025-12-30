package com.example.foodcalu.util;

import com.example.foodcalu.data.entity.UserProfile;

public final class TdeeCalculator {
    private TdeeCalculator() {
    }

    public static int calculateTdee(UserProfile profile) {
        if (profile == null) {
            return 0;
        }
        float weight = profile.weightKg;
        float height = profile.heightCm;
        int age = profile.age;
        float bmr;
        if ("女".equalsIgnoreCase(profile.gender)) {
            bmr = 10f * weight + 6.25f * height - 5f * age - 161f;
        } else {
            bmr = 10f * weight + 6.25f * height - 5f * age + 5f;
        }

        float factor = 1.2f;
        if ("轻度".equalsIgnoreCase(profile.activityLevel)) {
            factor = 1.375f;
        } else if ("中度".equalsIgnoreCase(profile.activityLevel)) {
            factor = 1.55f;
        } else if ("重度".equalsIgnoreCase(profile.activityLevel)) {
            factor = 1.725f;
        }

        return Math.round(bmr * factor);
    }
}
