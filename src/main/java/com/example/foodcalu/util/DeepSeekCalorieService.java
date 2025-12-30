package com.example.foodcalu.util;

import com.example.foodcalu.BuildConfig;
import com.example.foodcalu.data.model.AiDietItem;
import com.example.foodcalu.data.model.AiDietResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class DeepSeekCalorieService {
    public interface AiResultCallback {
        void onSuccess(AiDietResult result, String rawContent);

        void onError(String message);
    }

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String SYSTEM_PROMPT =
            "You are a nutrition analysis assistant.\n"
                    + "The user will describe food intake in natural language.\n"
                    + "You must identify each food item and estimate its weight and calories.\n"
                    + "Return ONLY valid JSON. Do not include explanations or extra text.\n"
                    + "\n"
                    + "The JSON format must be exactly as follows:\n"
                    + "{\n"
                    + "  \"items\": [\n"
                    + "    {\n"
                    + "      \"food\": \"food name\",\n"
                    + "      \"estimated_weight_g\": number,\n"
                    + "      \"calories_kcal\": number\n"
                    + "    }\n"
                    + "  ],\n"
                    + "  \"total_calories_kcal\": number,\n"
                    + "  \"note\": \"Calories are AI estimates for reference only\"\n"
                    + "}";

    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    private DeepSeekCalorieService() {
    }

    public static boolean isConfigured() {
        return !isBlank(BuildConfig.DEEPSEEK_API_KEY) && !isBlank(BuildConfig.DEEPSEEK_BASE_URL);
    }

    public static void requestAnalysis(String userInput, AiResultCallback callback) {
        if (!isConfigured()) {
            callback.onError("missing_api_key");
            return;
        }
        if (isBlank(userInput)) {
            callback.onError("empty_input");
            return;
        }

        JSONObject bodyJson = new JSONObject();
        try {
            bodyJson.put("model", "deepseek-chat");
            bodyJson.put("temperature", 0);
            bodyJson.put("stream", false);
            JSONArray messages = new JSONArray();
            JSONObject system = new JSONObject();
            system.put("role", "system");
            system.put("content", SYSTEM_PROMPT);
            messages.put(system);
            JSONObject user = new JSONObject();
            user.put("role", "user");
            user.put("content", userInput);
            messages.put(user);
            bodyJson.put("messages", messages);
        } catch (JSONException e) {
            callback.onError("json_error");
            return;
        }

        Request request = new Request.Builder()
                .url(normalizeBaseUrl(BuildConfig.DEEPSEEK_BASE_URL) + "/chat/completions")
                .addHeader("Authorization", "Bearer " + BuildConfig.DEEPSEEK_API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(bodyJson.toString(), JSON))
                .build();

        CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("network_error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        callback.onError("http_" + response.code());
                        return;
                    }
                    String body = response.body() != null ? response.body().string() : "";
                    String content = parseContent(body);
                    if (content == null) {
                        callback.onError("parse_error");
                        return;
                    }
                    AiDietResult result = parseDietResult(content);
                    if (result == null || result.items.isEmpty()) {
                        callback.onError("invalid_result");
                        return;
                    }
                    callback.onSuccess(result, content);
                } finally {
                    response.close();
                }
            }
        });
    }

    private static String parseContent(String body) {
        try {
            JSONObject json = new JSONObject(body);
            JSONArray choices = json.optJSONArray("choices");
            if (choices == null || choices.length() == 0) {
                return null;
            }
            JSONObject message = choices.getJSONObject(0).optJSONObject("message");
            if (message == null) {
                return null;
            }
            return message.optString("content", null);
        } catch (JSONException e) {
            return null;
        }
    }

    private static AiDietResult parseDietResult(String content) {
        String jsonText = extractJsonObject(content);
        if (jsonText == null) {
            return null;
        }
        try {
            JSONObject json = new JSONObject(jsonText);
            JSONArray itemsArray = json.optJSONArray("items");
            List<AiDietItem> items = new ArrayList<>();
            if (itemsArray != null) {
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject itemObj = itemsArray.optJSONObject(i);
                    if (itemObj == null) {
                        continue;
                    }
                    String food = itemObj.optString("food", "").trim();
                    float weight = parseNumber(itemObj, "estimated_weight_g");
                    float calories = parseNumber(itemObj, "calories_kcal");
                    if (food.isEmpty() || calories <= 0f) {
                        continue;
                    }
                    if (weight < 0f) {
                        weight = 0f;
                    }
                    items.add(new AiDietItem(food, weight, calories));
                }
            }

            float total = parseNumber(json, "total_calories_kcal");
            if (total <= 0f) {
                float sum = 0f;
                for (AiDietItem item : items) {
                    sum += item.caloriesKcal;
                }
                total = sum;
            }
            String note = json.optString("note", "");
            return new AiDietResult(items, total, note);
        } catch (JSONException e) {
            return null;
        }
    }

    private static String extractJsonObject(String content) {
        String trimmed = content == null ? "" : content.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        int start = trimmed.indexOf('{');
        int end = trimmed.lastIndexOf('}');
        if (start == -1 || end == -1 || start >= end) {
            return null;
        }
        return trimmed.substring(start, end + 1);
    }

    private static float parseNumber(JSONObject obj, String key) {
        if (obj == null || key == null) {
            return 0f;
        }
        double value = obj.optDouble(key, Double.NaN);
        if (!Double.isNaN(value)) {
            return (float) value;
        }
        String text = obj.optString(key, "").trim();
        if (text.isEmpty()) {
            return 0f;
        }
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException ignored) {
            return 0f;
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static String normalizeBaseUrl(String baseUrl) {
        if (baseUrl == null) {
            return "";
        }
        if (baseUrl.endsWith("/")) {
            return baseUrl.substring(0, baseUrl.length() - 1);
        }
        return baseUrl;
    }
}
