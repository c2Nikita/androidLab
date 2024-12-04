package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.models.News;

import java.util.List;

public class NewsFragment extends Fragment {
    private static int i = 0;
    private List<News> newsList;
    private TextView textView1, textView2, textView3;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance(List<News> newsList) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putSerializable("newsList", (java.io.Serializable) newsList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newsList = (List<News>) getArguments().getSerializable("newsList");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        // Инициализация TextView
        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView3);

        // Заполняем текстовые поля первым элементом из списка
        if (newsList != null && !newsList.isEmpty()) {
            updateTextViews();
        }

        // Создаем GestureDetector для отслеживания свайпов
        GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                Log.d("NewsFragment", "onDown called");
                return true; // Это важно, иначе другие жесты не будут работать
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.d("NewsFragment", "onFling called: e1Y=" + e1.getY() + ", e2Y=" + e2.getY() + ", velocityY=" + velocityY);
                if(e1.getY()-e2.getY()>500)
                {
                    updateTextViews();
                }
                return true;
            }
        });

        // Устанавливаем OnTouchListener на view для перехвата событий свайпа
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        return view;
    }

    // Метод для обновления текста в TextViews
    private void updateTextViews() {
        if (newsList != null && !newsList.isEmpty() && i < newsList.size()) {

            textView1.setText(newsList.get(i).getSource());
            textView2.setText(newsList.get(i).getTitle());
            textView3.setText(newsList.get(i).getUrl());
            i++;
        }
    }

    // Метод для обновления списка новостей
    public void updateNewsList(List<News> newNewsList) {
        this.newsList = newNewsList;
        if (getView() != null) {
            Log.d("NewsFragment", "News list updated with size: " + newsList.size());
            // Обновите данные в вашем интерфейсе, например, через RecyclerView Adapter
        }
    }
}
