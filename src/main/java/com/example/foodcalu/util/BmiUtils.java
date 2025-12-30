package com.example.foodcalu.util;

public final class BmiUtils {
    private BmiUtils() {
    }

    public static float calcBmi(float weightKg, float heightCm) {
        if (heightCm <= 0) {
            return 0f;
        }
        float heightM = heightCm / 100f;
        return weightKg / (heightM * heightM);
    }

    public static String bmiCategory(float bmi) {
        if (bmi <= 0f) {
            return "未知";
        }
        if (bmi < 18.5f) {
            return "偏瘦";
        }
        if (bmi < 24.9f) {
            return "正常";
        }
        if (bmi < 29.9f) {
            return "超重";
        }
        return "肥胖";
    }

    public static String bmiTip(String category) {
        if ("偏瘦".equals(category)) {
            return "适当增加蛋白质与力量训练。";
        }
        if ("正常".equals(category)) {
            return "保持均衡饮食与规律训练。";
        }
        if ("超重".equals(category)) {
            return "优先有氧训练并注意控制食量。";
        }
        if ("肥胖".equals(category)) {
            return "建议从低冲击有氧与拉伸开始。";
        }
        return "请完善档案以获取建议。";
    }
}
