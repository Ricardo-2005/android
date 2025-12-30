package com.example.foodcalu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodcalu.R;
import com.example.foodcalu.data.db.AppDatabase;
import com.example.foodcalu.data.entity.UserAccount;
import com.example.foodcalu.repository.AppRepository;
import com.example.foodcalu.util.SessionManager;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SessionManager.isLoggedIn(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        EditText inputUsername = findViewById(R.id.input_login_username);
        EditText inputPassword = findViewById(R.id.input_login_password);
        Button loginButton = findViewById(R.id.button_login);
        TextView goRegister = findViewById(R.id.text_go_register);

        loginButton.setOnClickListener(v -> {
            String username = inputUsername.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                return;
            }

            AppRepository repository = AppRepository.getInstance(this);
            AppDatabase.DB_EXECUTOR.execute(() -> {
                UserAccount account = repository.loginSync(username, password);
                runOnUiThread(() -> {
                    if (account != null) {
                        SessionManager.setLoggedIn(this, account.userId, username);
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        goRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}
