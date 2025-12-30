package com.example.foodcalu.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodcalu.R;
import com.example.foodcalu.data.entity.WorkoutItem;
import com.example.foodcalu.data.entity.WorkoutLog;
import com.example.foodcalu.repository.AppRepository;
import com.example.foodcalu.util.DateUtils;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkoutDetailActivity extends AppCompatActivity {
    public static final String EXTRA_WORKOUT_ID = "extra_workout_id";
    private static final int DEFAULT_TARGET_MINUTES = 10;
    private static final Pattern MINUTES_PATTERN = Pattern.compile("(\\d+)\\s*分钟");

    private ExoPlayer player;
    private Chronometer chronometer;
    private CircularProgressIndicator timerRing;
    private boolean running = false;
    private long elapsedMs = 0L;
    private WorkoutItem currentItem;
    private int targetSeconds = DEFAULT_TARGET_MINUTES * 60;
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private final Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            updateRingProgress();
            if (running) {
                timerHandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        TextView name = findViewById(R.id.text_workout_name);
        TextView meta = findViewById(R.id.text_workout_meta);
        TextView suggestion = findViewById(R.id.text_workout_suggestion);
        TextView kcal = findViewById(R.id.text_workout_kcal);
        PlayerView playerView = findViewById(R.id.player_view);
        chronometer = findViewById(R.id.chronometer);
        timerRing = findViewById(R.id.progress_timer);
        Button start = findViewById(R.id.button_start_timer);
        Button pause = findViewById(R.id.button_pause_timer);
        Button complete = findViewById(R.id.button_complete);
        EditText noteInput = findViewById(R.id.input_workout_note);

        long workoutId = getIntent().getLongExtra(EXTRA_WORKOUT_ID, -1);
        if (workoutId == -1) {
            finish();
            return;
        }

        AppRepository.getInstance(this).getWorkoutItemById(workoutId).observe(this, item -> {
            currentItem = item;
            if (item == null) {
                return;
            }
            name.setText(item.name);
            meta.setText(String.format("%s | %s", item.difficulty, item.targetMuscle));
            suggestion.setText(item.suggestion);
            kcal.setText(String.format("预计消耗：%d 千卡", item.kcalEstimate));
            updateTargetFromSuggestion(item.suggestion);
            if (item.videoUrl != null && !item.videoUrl.isEmpty()) {
                initPlayer(playerView, item.videoUrl);
            } else {
                playerView.setVisibility(View.GONE);
            }
        });

        start.setOnClickListener(v -> startTimer());
        pause.setOnClickListener(v -> pauseTimer());
        complete.setOnClickListener(v -> {
            int minutes = getElapsedMinutes();
            if (currentItem == null) {
                Toast.makeText(this, "训练项未加载", Toast.LENGTH_SHORT).show();
                return;
            }
            WorkoutLog log = new WorkoutLog(
                    0,
                    DateUtils.today(),
                    currentItem.id,
                    minutes,
                    true,
                    noteInput.getText().toString().trim(),
                    currentItem.kcalEstimate
            );
            AppRepository.getInstance(this).addWorkoutLog(log);
            Toast.makeText(this, "训练已打卡", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void initPlayer(PlayerView playerView, String url) {
        if (player == null) {
            player = new ExoPlayer.Builder(this).build();
            playerView.setPlayer(player);
        }
        MediaItem mediaItem = MediaItem.fromUri(url);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(false);
    }

    private void startTimer() {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - elapsedMs);
            chronometer.start();
            running = true;
            startProgressUpdates();
        }
    }

    private void pauseTimer() {
        if (running) {
            elapsedMs = SystemClock.elapsedRealtime() - chronometer.getBase();
            chronometer.stop();
            running = false;
            updateRingProgress();
        }
    }

    private int getElapsedMinutes() {
        if (running) {
            elapsedMs = SystemClock.elapsedRealtime() - chronometer.getBase();
        }
        int minutes = (int) Math.round(elapsedMs / 60000.0);
        return Math.max(minutes, 1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timerHandler.removeCallbacks(progressRunnable);
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void startProgressUpdates() {
        timerHandler.removeCallbacks(progressRunnable);
        timerHandler.post(progressRunnable);
    }

    private void updateRingProgress() {
        if (timerRing == null) {
            return;
        }
        long currentElapsed = running
                ? SystemClock.elapsedRealtime() - chronometer.getBase()
                : elapsedMs;
        int seconds = (int) (currentElapsed / 1000);
        timerRing.setMax(targetSeconds);
        timerRing.setProgress(Math.min(seconds, targetSeconds));
    }

    private void updateTargetFromSuggestion(String suggestion) {
        int minutes = parseMinutes(suggestion);
        targetSeconds = Math.max(1, minutes) * 60;
        updateRingProgress();
    }

    private int parseMinutes(String suggestion) {
        if (suggestion == null) {
            return DEFAULT_TARGET_MINUTES;
        }
        Matcher matcher = MINUTES_PATTERN.matcher(suggestion);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException ignored) {
                return DEFAULT_TARGET_MINUTES;
            }
        }
        return DEFAULT_TARGET_MINUTES;
    }
}
