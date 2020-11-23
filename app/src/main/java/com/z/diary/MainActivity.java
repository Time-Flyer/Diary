package com.z.diary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.gyf.immersionbar.ImmersionBar;
import com.imuxuan.floatingview.FloatingMagnetView;
import com.imuxuan.floatingview.FloatingView;
import com.imuxuan.floatingview.MagnetViewListener;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.z.diary.Entity.Music;
import com.z.diary.Entity.MusicList;
import com.z.diary.UI.NoScrollViewPager;
import com.z.diary.UI.ViewPagerAdapter;
import com.z.diary.Utils.Connection;
import com.z.diary.Utils.MusicService;
import com.z.diary.Utils.ScanMusic;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private NoScrollViewPager viewPager;
    private AHBottomNavigation navigation;

    private MusicList musicList;
    private Connection conn;

    private int[] menu = {R.drawable.random, R.drawable.last, R.drawable.next};
    private boolean isRandom = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override   
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // 沉浸式状态栏
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();

        initViewPager();
        initNavigation();
        initBoomMenu();
        initMusicList();
        initFloatMenu();
    }

    public void initMusicList() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }

        musicList = (MusicList) getApplication();
        musicList.setList(ScanMusic.getMusic(this));

        Collections.sort(musicList.getList());
        int i = 0, size = musicList.getList().size();
        while (i < size) {
            if(musicList.getList().get(i).getHeaderWord().equals("#")) {
                Music music = musicList.getList().get(i);
                musicList.getList().remove(i);
                musicList.getList().add(music);
                size--;
            } else {
                i++;
            }
        }

        conn = new Connection();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        if(requestCode == 1 && (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            initMusicList();
            return;
        }
        else {
            Toast.makeText(this, "请开启存储权限以扫描本地音乐",
                    Toast.LENGTH_SHORT).show();
        }
        // 处理"禁止后不再询问"
        for (int i = 0; i < grantResults.length; i++){
            if(grantResults[i] == PackageManager. PERMISSION_DENIED ){
                if(!ActivityCompat. shouldShowRequestPermissionRationale (this, permissions[i])){
                    Toast. makeText (this, "请手动开启权限", Toast. LENGTH_SHORT ).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS );
                    Uri uri = Uri. fromParts ("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        }
    }

    private void initBoomMenu() {
        BoomMenuButton bmb = findViewById(R.id.bmb);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            final SimpleCircleButton.Builder builder = new SimpleCircleButton.Builder()
                    .normalImageRes(menu[i])
                    .normalColorRes(R.color.background)
                    .highlightedColorRes(R.color.active)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            Intent intent = new Intent();
                            switch (index) {
                                case 0:
                                    isRandom = !isRandom;
                                    intent.setAction("random");
                                    intent.putExtra("flag", isRandom);
                                    String tip = isRandom ? "随机播放" : "列表循环";
                                    Toast.makeText(MainActivity.this,
                                            tip, Toast.LENGTH_SHORT).show();
                                break;
                                case 1: intent.setAction("last");break;
                                case 2: intent.setAction("next");break;
                            }
                            sendBroadcast(intent);
                        }
                    });
            bmb.addBuilder(builder);
        }
    }

    private void initFloatMenu(){
        FloatingView.get().add();
        FloatingView.get().icon(R.drawable.control);
        FloatingView.get().listener(new MagnetViewListener() {
            @Override
            public void onRemove(FloatingMagnetView magnetView) { }

            @Override
            public void onClick(FloatingMagnetView magnetView) {
                if (musicList.getList().size() == 0) {
                    Toast.makeText(MainActivity.this,
                            "音乐列表空空如也，快去下载音乐吧!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initNavigation() {
        navigation = findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Today", R.drawable.write);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("List", R.drawable.list);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Music", R.drawable.music);

        navigation.addItem(item1);
        navigation.addItem(item2);
        navigation.addItem(item3);
        navigation.setCurrentItem(0);

        navigation.setDefaultBackgroundColor(getResources().getColor(R.color.background)); //
        navigation.setAccentColor(getResources().getColor(R.color.colorPrimary));
        navigation.setInactiveColor(getResources().getColor(R.color.inactive));

        navigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        // Force to tint the drawable (useful for font with icon for example)
        navigation.setForceTint(true);

        // Display color under navigation bar (API 21+)
        navigation.setTranslucentNavigationEnabled(true);

        navigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                viewPager.setCurrentItem(position);
                return true;
            }
        });
    }

    private void initViewPager(){
        viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setScanScroll(false);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                navigation.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FloatingView.get().attach(this);
    }

    @Override
    protected void onStop() {
        FloatingView.get().detach(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }
}
