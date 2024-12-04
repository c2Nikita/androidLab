package com.example.myapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.MotionEvent; // Импорт для MotionEvent
import android.widget.ImageView; // Импорт для ImageView
import android.widget.TextView; // Импорт для TextView
import com.example.myapplication.models.Profile;

public class ProfileFragment extends Fragment {

    private static final String ARG_PROFILE = "profile";
    private Profile profile; // Хранение переданного объекта Profile
    private ImageView imageView; // Объект ImageView
    private float scaleFactor = 1.0f; // Текущий масштаб изображения
    private static final float SCALE_STEP = 1.5f; // Коэффициент увеличения

    public ProfileFragment() {
        // Обязательный пустой конструктор
    }

    // Фабричный метод для создания нового экземпляра этого фрагмента с переданным объектом Profile
    public static ProfileFragment newInstance(Profile profile) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROFILE, profile); // Передаем объект Profile
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            profile = (Profile) getArguments().getSerializable(ARG_PROFILE); // Получаем объект Profile
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Инфляция макета для этого фрагмента
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Используем view для поиска и настройки компонентов UI
        TextView nameTextView = view.findViewById(R.id.name); // Убедитесь, что ID совпадает с вашим
        if (profile != null) {
            nameTextView.setText("Ваше имя: "+profile.name); // Устанавливаем имя из объекта Profile
        }
        TextView teamTextView = view.findViewById(R.id.team); // Убедитесь, что ID совпадает с вашим
        if (profile != null) {
            teamTextView.setText("Ваша команда: "+profile.team); // Устанавливаем команду из объекта Profile
        }

        // Находим ImageView и устанавливаем обработчик касаний
        imageView = view.findViewById(R.id.imageView4); // Замените на правильный ID вашего ImageView
        imageView.setOnTouchListener(new View.OnTouchListener() {
            private long lastTapTime = 0; // Время последнего нажатия

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    long currentTime = System.currentTimeMillis();
                    // Проверяем, было ли нажатие двойным
                    if (currentTime - lastTapTime < 300) { // Порог для двойного нажатия
                        // Увеличиваем масштаб изображения
                        scaleFactor = (scaleFactor == 1.0f) ? SCALE_STEP : 1.0f; // Либо увеличиваем, либо сбрасываем
                        imageView.setScaleX(scaleFactor);
                        imageView.setScaleY(scaleFactor);
                    }
                    lastTapTime = currentTime;
                }
                return true; // Обработано
            }
        });

        return view; // Возвращаем инфлятированный view
    }
}
