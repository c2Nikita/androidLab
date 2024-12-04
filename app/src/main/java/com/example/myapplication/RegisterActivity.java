package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private OkHttpClient client;
    private Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        client = new OkHttpClient();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Установка отступов для системных панелей
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Найти кнопку и поля для ввода

        Button buttonRegister = findViewById(R.id.Register);
        EditText editTextLogin = findViewById(R.id.editTextLogin);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        EditText editTextName = findViewById(R.id.editNameText);
        EditText editTextTeam = findViewById(R.id.editTeamText);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = editTextLogin.getText().toString();
                String password = editTextPassword.getText().toString();
                String name = editTextName.getText().toString();
                String team = editTextTeam.getText().toString();

                if (login.isEmpty() || password.isEmpty() || name.isEmpty() || team.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                } else {
                    // Создание JSON-строки
                    String json = String.format("{\"login\":\"%s\", \"password\":\"%s\", \"name\":\"%s\", \"team\":\"%s\"}",
                            login, password, name, team);

                    // Формирование тела запроса
                    RequestBody body = RequestBody.create(
                            json,
                            MediaType.parse("application/json")
                    );

                    // Формирование запроса
                    Request request = new Request.Builder()
                            .url("http://10.0.2.2:8080/register/profile") // Используйте 10.0.2.2 для доступа к localhost из эмулятора
                            .post(body)
                            .build();
                    new Thread(() -> {
                        try {
                            Response response = client.newCall(request).execute();

                            // Проверка статуса ответа
                            if (response.isSuccessful()) {
                                // Получение ответа
                                String responseBody = response.body().string();
                                if(!responseBody.isEmpty())
                                {
                                    Log.d("Response", responseBody);
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }


                            } else {
                                // Логирование ошибки
                                Log.e("Request Error", "Error code: " + response.code());
                            }
                        } catch (IOException e) {
                            // Логирование исключения
                            Log.e("Request Exception", "IOException: " + e.getMessage());
                        }
                    }).start(); // Запускаем поток







                }
            }
        });
    }
}