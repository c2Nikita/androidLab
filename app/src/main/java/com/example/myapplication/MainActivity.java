package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Добавьте импорт для логирования
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.models.Profile;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private OkHttpClient client;
    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        client = new OkHttpClient();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Установка отступов для системных панелей
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Найти кнопку и поля для ввода
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonRegister = findViewById(R.id.registerButton);
        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);

        // Установка обработчика нажатия для кнопки
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получение данных из полей ввода
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();


                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Введите логин и пароль", Toast.LENGTH_SHORT).show();
                } else {
                    HttpUrl url = new HttpUrl.Builder()
                            .scheme("http")
                            .host("10.0.2.2") // Используйте это для доступа к localhost из эмулятора
                            .port(8080)
                            .addPathSegment("authentication")
                            .addPathSegment("profile")
                            .addQueryParameter("login", username)
                            .addQueryParameter("password", password)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();

                    // Выполнение запроса в отдельном потоке
                    new Thread(() -> {
                        try {
                            Response response = client.newCall(request).execute();

                            // Проверка статуса ответа
                            if (response.isSuccessful()) {
                                String responseBody = response.body().string();

                                // Логирование тела ответа
                                Log.d("Response Body", responseBody);

                                // Парсинг JSON ответа в объект Profile
                                Profile profile = gson.fromJson(responseBody, Profile.class);
                                if (profile != null && profile.team != null) {
                                    Intent intent = new Intent(MainActivity.this, ProfileNewsActivity.class);
                                    intent.putExtra("profile", profile);
                                    startActivity(intent);
                                } else {
                                    runOnUiThread(() -> {
                                        Toast.makeText(MainActivity.this, "Неправильно введены данные", Toast.LENGTH_SHORT).show();
                                    });
                                }


                            } else {
                                // Логирование кода ошибки
                                Log.e("Request Error", "Error code: " + response.code());
                                runOnUiThread(() -> {
                                    Toast.makeText(MainActivity.this, "Ошибка: " + response.code(), Toast.LENGTH_SHORT).show();
                                });
                            }
                        } catch (IOException e) {
                            // Логирование исключения
                            Log.e("Request Exception", "IOException: " + e.getMessage());
                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "Ошибка соединения", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }).start(); // Запускаем поток
                }
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
