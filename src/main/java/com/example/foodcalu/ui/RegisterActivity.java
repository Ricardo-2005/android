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

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText inputUsername = findViewById(R.id.input_register_username);
        EditText inputPassword = findViewById(R.id.input_register_password);
        Button registerButton = findViewById(R.id.button_register);
        TextView goLogin = findViewById(R.id.text_go_login);

        registerButton.setOnClickListener(v -> {
            String username = inputUsername.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                return;
            }

            AppRepository repository = AppRepository.getInstance(this);
            AppDatabase.DB_EXECUTOR.execute(() -> {
                UserAccount existing = repository.getAccountByUsernameSync(username);
                if (existing != null) {
                    runOnUiThread(() -> Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show());
                    return;
                }
                UserAccount account = new UserAccount(0, username, password, System.currentTimeMillis());
                long userId = repository.insertAccountSync(account);
                runOnUiThread(() -> {
                    SessionManager.setLoggedIn(this, userId, username);
                    Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                });
            });
        });

        goLogin.setOnClickListener(v -> {
            finish();
        });
    }
}
