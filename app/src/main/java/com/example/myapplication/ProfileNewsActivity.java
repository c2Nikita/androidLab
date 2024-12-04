package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.models.News;
import com.example.myapplication.models.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileNewsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_news);

        Profile profile = (Profile) getIntent().getSerializableExtra("profile");

        replaceFragment(new NewsFragment());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.news) {
                fetchAndUpdateNews();
                return true;
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(ProfileFragment.newInstance(profile));
                return true;
            }
            return false;
        });

        fetchAndUpdateNews(); // Загрузка новостей при запуске
    }

    private void fetchAndUpdateNews() {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("10.0.2.2")
                .port(8080)
                .addPathSegment("news")
                .addPathSegment("get")
                .build();

        Request request = new Request.Builder().url(url).get().build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Type newsListType = new TypeToken<List<News>>() {}.getType();
                    List<News> newsList = gson.fromJson(responseBody, newsListType);

                    runOnUiThread(() -> updateNewsFragment(newsList));
                } else {
                    Log.e("Request Error", "Error code: " + response.code());
                    runOnUiThread(() -> Toast.makeText(this, "Ошибка: " + response.code(), Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                Log.e("Request Exception", "IOException: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(this, "Ошибка соединения", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void updateNewsFragment(List<News> newsList) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.my_layout_id);

        if (currentFragment instanceof NewsFragment) {
            ((NewsFragment) currentFragment).updateNewsList(newsList);
        } else {
            NewsFragment newsFragment = NewsFragment.newInstance(newsList);
            replaceFragment(newsFragment);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.my_layout_id, fragment);
        fragmentTransaction.commit();
    }
}
