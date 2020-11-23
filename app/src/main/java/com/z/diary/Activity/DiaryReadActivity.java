package com.z.diary.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.imuxuan.floatingview.FloatingView;
import com.z.diary.R;
import com.z.diary.UI.TitleBar;

public class DiaryReadActivity extends AppCompatActivity {

    private int pos;
    private long id;
    private String title, weather, date, content;
    private TextView tv_title;
    private TextView tv_weather;
    private TextView tv_date;
    private TextView tv_content;

    private MyBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_diary_read);

        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();

        receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, new IntentFilter("change"));

        tv_title = findViewById(R.id.diary_title);
        tv_weather = findViewById(R.id.diary_weather);
        tv_date = findViewById(R.id.diary_date);
        tv_content = findViewById(R.id.diary_content);

        initActionBar();
        getData(getIntent());
        setData();

    }

    private void initActionBar() {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.setTv_title("日记");
        titleBar.setTv_save("编辑");
        titleBar.setClick(new TitleBar.TitleOnClick() {
            @Override
            public void backClick() {
                finish();
            }

            @Override
            public void btnClick() {
                Intent intent = new Intent(DiaryReadActivity.this, DiaryEditActivity.class);
                intent.putExtra("pos", pos);
                intent.putExtra("id", id);
                intent.putExtra("date", date);
                intent.putExtra("weather", weather);
                intent.putExtra("title", title);
                intent.putExtra("content", content);
                startActivity(intent);
            }

            @Override
            public void lastClick() {
                Intent intent = new Intent();
                intent.setAction("last");
                sendBroadcast(intent);
            }

            @Override
            public void nextClick() {
                Intent intent = new Intent();
                intent.setAction("next");
                sendBroadcast(intent);
            }
        });
    }

    private void getData(Intent intent) {
        pos = intent.getIntExtra("pos", -1);
        id = intent.getLongExtra("id", -1);
        date = intent.getStringExtra("date");
        weather = intent.getStringExtra("weather");
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
    }

    private void setData() {
        tv_title.setText(title);
        tv_weather.setText(weather);
        tv_date.setText(date);
        tv_content.setText(content);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FloatingView.get().attach(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("change".equals(intent.getAction())){
                getData(intent);
                setData();
            }
        }
    }
}
