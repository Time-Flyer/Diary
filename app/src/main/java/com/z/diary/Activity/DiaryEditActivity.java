package com.z.diary.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.imuxuan.floatingview.FloatingView;
import com.z.diary.R;
import com.z.diary.UI.TitleBar;
import com.z.diary.Utils.DiaryHelper;

public class DiaryEditActivity extends AppCompatActivity {

    private boolean flag; // 是否新建
    private int pos;
    private long id;
    private String title, weather, date, content;
    private EditText et_title;
    private EditText et_weather;
    private EditText et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_diary_edit);

        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();

        initActionBar();
        getData();
        setData();

    }

    private void initActionBar(){
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.setTv_title("编辑日记");
        titleBar.setClick(new TitleBar.TitleOnClick() {
            @Override
            public void backClick() {
                // 应该检查是否有未保存的修改
                finish();
            }

            @Override
            public void btnClick() {
                title = et_title.getText().toString();
                weather = et_weather.getText().toString();
                content = et_content.getText().toString();

                if ("".equals(title) || "".equals(weather) || "".equals(content)) {
                    Toast.makeText(DiaryEditActivity.this, "不能留空", Toast.LENGTH_SHORT).show();
                    return;
                }

                DiaryHelper helper = new DiaryHelper(DiaryEditActivity.this);

                Intent intent = new Intent();
                intent.putExtra("date", date);
                intent.putExtra("weather", weather);
                intent.putExtra("title", title);
                intent.putExtra("content", content);

                if(!flag) {
                    helper.update(id, weather, title, content);
                    intent.putExtra("pos", pos);
                    intent.setAction("change");
                }
                else {
                    id = helper.insert(date, weather, title, content);
                    intent.setAction("new");
                }

                intent.putExtra("id", id);

                sendBroadcast(intent, null);

                finish();
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

    private void getData() {
        Intent intent = getIntent();
        if(intent != null) {
            pos = intent.getIntExtra("pos", -1);
            id = intent.getLongExtra("id", -1);
            if(pos == -1) {
                flag = true;

            } else {
                title = intent.getStringExtra("title");
                content = intent.getStringExtra("content");
                flag = false;
            }
            weather = intent.getStringExtra("weather");
            date = intent.getStringExtra("date");
        }
    }

    private void setData(){
        TextView tv_date = findViewById(R.id.diary_date);
        et_weather = findViewById(R.id.et_weather);
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        if(!flag){
            et_title.setText(title);
            et_content.setText(content);
        }
        et_weather.setText(weather);
        tv_date.setText(date);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FloatingView.get().attach(this);
    }

}
