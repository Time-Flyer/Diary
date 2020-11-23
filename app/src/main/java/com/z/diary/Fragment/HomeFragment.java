package com.z.diary.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dlong.rep.dlocationmanager.DLocationTools;
import com.dlong.rep.dlocationmanager.DLocationUtils;
import com.dlong.rep.dlocationmanager.OnLocationChangeListener;
import com.dlong.rep.dlsimpleweathermanager.DLSimpleWeatherUtils;
import com.dlong.rep.dlsimpleweathermanager.OnGetWeatherListener;
import com.dlong.rep.dlsimpleweathermanager.model.DLCoordinateCode;
import com.dlong.rep.dlsimpleweathermanager.model.DLPlaceInfo;
import com.dlong.rep.dlsimpleweathermanager.model.DLWeatherInfo;
import com.yinglan.shadowimageview.ShadowImageView;
import com.z.diary.Activity.DiaryEditActivity;
import com.z.diary.R;
import com.z.diary.Utils.DiaryHelper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.dlong.rep.dlocationmanager.DLocationWhat.NO_LOCATIONMANAGER;
import static com.dlong.rep.dlocationmanager.DLocationWhat.NO_PROVIDER;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private final String key = "e2ct85TFn0";

    private View rootView;

    private MyBroadcastReceiver receiver1, receiver2;

    private SharedPreferences sharedPreferences;
    private double oldLongitude, oldLatitude;

    private DecimalFormat df;

    private ShadowImageView image;
    private TextView tv_date, tv_week;
    private TextView tv_tip;
    private Button button;
    private String date;
    private String weather;

    private ImageView iv_weather;
    private TextView tv_weather;
    private TextView tv_location;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_home, null);

            receiver1 = new MyBroadcastReceiver();
            rootView.getContext().registerReceiver(receiver1, new IntentFilter("new"));
            receiver2 = new MyBroadcastReceiver();
            rootView.getContext().registerReceiver(receiver2, new IntentFilter("today is null"));

            init();
        }
        return rootView;
    }

    private void init() {
        DLocationUtils.init(rootView.getContext());
        DLSimpleWeatherUtils.init(rootView.getContext());

        df = new DecimalFormat(".00"); // 保留2位小数

        // 获取已保存的经纬度
        sharedPreferences =  rootView.getContext().getSharedPreferences("location", MODE_PRIVATE );
        oldLongitude = sharedPreferences.getFloat("longitude", -1);
        oldLatitude = sharedPreferences.getFloat("latitude", -1);

        if(oldLongitude != -1) {
            // 天气
            DLSimpleWeatherUtils.checkWeather(oldLatitude, oldLongitude,
                    DLCoordinateCode.CODE_WGS84, onGetWeatherListener);

            // 保留2位小数进行比较
            oldLongitude = Double.parseDouble(df.format(oldLongitude));
            oldLatitude = Double.parseDouble(df.format(oldLatitude));
        }

        DLocationUtils.getInstance().register(locationChangeListener);

        // 获取当天的日期和星期
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        date = format.format(today);
        format = new SimpleDateFormat("EEEE");
        String week = format.format(today);

        image = rootView.findViewById(R.id.picture);
        tv_date = rootView.findViewById(R.id.date);
        tv_week = rootView.findViewById(R.id.week);
        tv_tip = rootView.findViewById(R.id.tip);
        button = rootView.findViewById(R.id.btn_write);
        button.setOnClickListener(this);

        iv_weather = rootView.findViewById(R.id.iv_weather);
        iv_weather.setOnClickListener(this);
        tv_weather = rootView.findViewById(R.id.tv_weather);
        tv_weather.setOnClickListener(this);
        tv_location = rootView.findViewById(R.id.location);

        image.setImageResource(R.drawable.theme);
        tv_date.setText(date);
        tv_week.setText(week);

        DiaryHelper helper = new DiaryHelper(rootView.getContext());

        if (helper.find(date)) {
            tv_tip.setText("good，今天已记录");
            button.setText("再写一篇");
        }
    }

    class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if("new".equals(intent.getAction())){
                tv_tip.setText("good，今天已记录");
                button.setText("再写一篇");
            }
            if("today is null".equals(intent.getAction())){
                tv_tip.setText("今天还没有写日记哦");
                button.setText("去写一篇");
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        rootView.getContext().unregisterReceiver(receiver1);
        rootView.getContext().unregisterReceiver(receiver2);
        DLocationUtils.getInstance().unregister();
    }

    /**
     * 定位监听器
     */
    private OnLocationChangeListener locationChangeListener = new OnLocationChangeListener() {
        String TAG = "定位";
        @Override
        public void getLastKnownLocation(Location location) {
            // 获取上一次获得的定位
            Log.e(TAG, "last开始");
            Log.e(TAG, "定位方式：" + location.getProvider());
            Log.e(TAG, "时间：" + location.getTime());
            Log.e(TAG, "经度：" + oldLongitude);
            Log.e(TAG, "纬度：" + oldLatitude);
            Log.e(TAG, "海拔：" + location.getAltitude());
            Log.e(TAG, "获取地理位置：" + DLocationTools.getAddress(rootView.getContext(), location.getLatitude(), location.getLongitude()));
            Log.e(TAG, "国家：" + DLocationTools.getCountryName(rootView.getContext(), location.getLatitude(), location.getLongitude()));
            Log.e(TAG, "所在地：" + DLocationTools.getLocality(rootView.getContext(), location.getLatitude(), location.getLongitude()));
            Log.e(TAG, "所在街道：" + DLocationTools.getStreet(rootView.getContext(), location.getLatitude(), location.getLongitude()));
            Log.e(TAG, "last结束");
        }

        @Override
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            if(oldLongitude == Double.parseDouble(df.format(longitude))
                    && oldLatitude == Double.parseDouble(df.format(latitude)))
                return;
            // 定位改变
            sharedPreferences.edit().putFloat("longitude", (float) longitude).apply();
            sharedPreferences.edit().putFloat("latitude", (float) latitude).apply();
            oldLongitude = Double.parseDouble(df.format(longitude));
            oldLatitude = Double.parseDouble(df.format(latitude));
            // 自动刷新天气
            DLSimpleWeatherUtils.checkWeather(latitude, longitude,
                    DLCoordinateCode.CODE_WGS84, onGetWeatherListener);
            Log.e(TAG, "定位改变开始");
            Log.e(TAG, "定位方式：" + location.getProvider());
            Log.e(TAG, "时间：" + location.getTime());
            Log.e(TAG, "经度：" + location.getLongitude());
            Log.e(TAG, "纬度：" + location.getLatitude());
            Log.e(TAG, "海拔：" + location.getAltitude());
            Log.e(TAG, "获取地理位置：" + DLocationTools.getAddress(rootView.getContext(), location.getLatitude(), location.getLongitude()));
            Log.e(TAG, "国家：" + DLocationTools.getCountryName(rootView.getContext(), location.getLatitude(), location.getLongitude()));
            Log.e(TAG, "所在地：" + DLocationTools.getLocality(rootView.getContext(), location.getLatitude(), location.getLongitude()));
            Log.e(TAG, "所在街道：" + DLocationTools.getStreet(rootView.getContext(), location.getLatitude(), location.getLongitude()));
            Log.e(TAG, "定位改变结束");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 状态改变
            // 比如GPS的开关，DLocationWhat.STATUS_ENABLE/DLocationWhat.STATUS_DISABLE

        }
    };

    /**
     * 天气获取监听器
     */
    private OnGetWeatherListener onGetWeatherListener = new OnGetWeatherListener() {
        @Override
        public void OnNetworkDisable() {
            Toast.makeText(rootView.getContext(), "没有打开网络，或没有网络权限", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void OnError(int step, int code) {
            Log.e("错误信息", "step = " + step + "; code = " + code);
        }

        @Override
        public void OnGetWeather(DLWeatherInfo weatherInfo) {
            switch (weatherInfo.getStatusCode()){
                case "0": iv_weather.setImageResource(R.drawable.a00); break;
                case "1": iv_weather.setImageResource(R.drawable.a01); break;
                case "2": iv_weather.setImageResource(R.drawable.a02); break;
                default: iv_weather.setImageResource(R.drawable.nothing); break;
            }
            weather = weatherInfo.getStatusText();
            tv_weather.setText(weatherInfo.getCurrentTemperature());
            Toast.makeText(rootView.getContext(), "天气已更新", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void OnGetLatAndLon(double latitude, double longitude) {
        }

        @Override
        public void OnGetRealAddress(DLPlaceInfo placeInfo) {
            // 更新地区
            if(placeInfo.getDistrict() == null ||
                placeInfo.getDistrict().isEmpty()){
                tv_location.setText(placeInfo.getCity());
            } else {
                tv_location.setText(placeInfo.getDistrict());
            }
        }
    };

    public void onClick(View view){
        switch (view.getId()) {
            // 手动刷新天气
            case R.id.iv_weather:
            case R.id.tv_weather:
                int status = DLocationUtils.getInstance().register(locationChangeListener);
                switch (status) {
                    case NO_LOCATIONMANAGER:
                        Toast.makeText(rootView.getContext(), "没有定位权限", Toast.LENGTH_SHORT).show();
                        // TODO: 2019/4/13 请求权限
                        DLocationTools.openAppSetting(rootView.getContext());

                        break;
                    case NO_PROVIDER:
                        Toast.makeText(rootView.getContext(),
                                "请开启定位服务", Toast.LENGTH_SHORT).show();
                        // TODO: 2019/4/13 打开定位
                        DLocationTools.openGpsSettings(rootView.getContext());
                        break;
                    default:
                        DLSimpleWeatherUtils.checkWeather(oldLatitude, oldLongitude,
                                DLCoordinateCode.CODE_WGS84, onGetWeatherListener);
                        break;
                }
                break;
            case R.id.btn_write:
                Intent intent = new Intent(rootView.getContext(), DiaryEditActivity.class);
                intent.putExtra("weather", weather); // 此刻天气
                intent.putExtra("date", date);
                startActivity(intent);
                break;
        }
    }

    private static String getWeek(Date date){
        String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(week_index<0){
            week_index = 0;
        }
        return weeks[week_index];
    }

}