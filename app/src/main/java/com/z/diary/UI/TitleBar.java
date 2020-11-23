package com.z.diary.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.z.diary.R;

public class TitleBar extends RelativeLayout {
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_save;
    private TitleOnClick click;
    private ImageView iv_last, iv_next;

    public TitleBar(Context context) {
        super(context);
        init(context);
    }
    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }
    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this);

        iv_back = findViewById(R.id.title_back);
        tv_title = findViewById(R.id.title_title);
        tv_save = findViewById(R.id.title_btn);
        iv_last = findViewById(R.id.last);
        iv_next = findViewById(R.id.next);

        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                click.backClick();
            }
        });
        tv_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                click.btnClick();
            }
        });
        iv_last.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                click.lastClick();
            }
        });
        iv_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                click.nextClick();
            }
        });
    }

    public void setTv_title(String title){
        if(!title.isEmpty())
            tv_title.setText(title);
    }

    public void setTv_save(String name){
        if(!name.isEmpty())
            tv_save.setText(name);
    }

    public void setClick(TitleOnClick click) { this.click = click;}

    public interface TitleOnClick {
        void backClick();
        void btnClick();
        void lastClick();
        void nextClick();
    }
}
