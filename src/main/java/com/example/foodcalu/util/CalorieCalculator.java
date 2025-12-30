package com.example.foodcalu.util;

public final class CalorieCalculator {
    public static final String UNIT_GRAM = "克";
    public static final String UNIT_BOWL = "一碗";
    public static final String UNIT_PORTION = "一份";
    public static final String UNIT_ITEM = "一个";
    public static final String UNIT_CUP = "一杯";

    private CalorieCalculator() {
    }

    public static float getUnitGrams(String unit) {
        if (UNIT_BOWL.equals(unit)) {
            return 250f;
        }
        if (UNIT_PORTION.equals(unit)) {
            return 200f;
        }
        if (UNIT_ITEM.equals(unit)) {
            return 100f;
        }
        if (UNIT_CUP.equals(unit)) {
            return 180f;
        }
        return 1f;
    }

    public static float estimateTotalGrams(String unit, float quantity) {
        float unitGrams = getUnitGrams(unit);
        return unitGrams * quantity;
    }

    public static float calculateCalories(float kcalPer100g, float unitGrams, float quantity) {
        return (unitGrams / 100f) * kcalPer100g * quantity;
    }
}
