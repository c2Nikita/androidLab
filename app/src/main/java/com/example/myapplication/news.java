package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.models.Profile;

public class news extends AppCompatActivity {

    private enum Background { IMAGE1, IMAGE2 }
    private Background currentBackground = Background.IMAGE1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news); // Подключаем разметку XML

        // Получение объекта профиля
        Profile profile = (Profile) getIntent().getSerializableExtra("profile");

        // Находим элементы разметки
        Button buttonNewBackGround = findViewById(R.id.buttonNewBackGround); // Ищем кнопку
        TextView textViewName = findViewById(R.id.ProfileNameText); // Ищем TextView для имени профиля
        Button buttonNextGround = findViewById(R.id.RealProfile);

        // Отображаем данные профиля
        if (profile != null) {
            textViewName.setText("Имя: " + profile.name+" команда: "+profile.team); // Убедитесь, что profile.getName() возвращает имя
        } else {
            textViewName.setText("Имя не найдено");
        }

        // Устанавливаем обработчик нажатия для кнопки
        buttonNewBackGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout view = findViewById(R.id.my_layout_id);
                if (currentBackground == Background.IMAGE1) {
                    currentBackground = Background.IMAGE2;
                    view.setBackgroundResource(R.drawable.image2); // Устанавливаем новый фон
                } else {
                    currentBackground = Background.IMAGE1;
                    view.setBackgroundResource(R.drawable.image1); // Возвращаем старый фон
                }
            }
        });
        buttonNextGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(news.this,ProfileNewsActivity.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
            }
        });
    }
}
