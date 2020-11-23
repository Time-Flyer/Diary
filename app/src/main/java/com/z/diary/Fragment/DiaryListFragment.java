package com.z.diary.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bigkoo.alertview.AlertView;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.widget.DefaultItemDecoration;
import com.z.diary.Activity.DiaryReadActivity;
import com.z.diary.Entity.Diary;
import com.z.diary.R;
import com.z.diary.Entity.StickyItem;
import com.z.diary.Utils.DiaryHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiaryListFragment extends Fragment {
    private View rootView;

    static final private int [] days = { R.drawable.d01, R.drawable.d02, R.drawable.d03,
            R.drawable.d04, R.drawable.d05, R.drawable.d06, R.drawable.d07, R.drawable.d08,
            R.drawable.d09, R.drawable.d10, R.drawable.d11, R.drawable.d12, R.drawable.d13,
            R.drawable.d14, R.drawable.d15, R.drawable.d16, R.drawable.d17, R.drawable.d18,
            R.drawable.d19, R.drawable.d20, R.drawable.d21, R.drawable.d22, R.drawable.d23,
            R.drawable.d24, R.drawable.d25, R.drawable.d26, R.drawable.d27, R.drawable.d28,
            R.drawable.d29, R.drawable.d30, R.drawable.d31 };

    private DiaryHelper helper;
    private List<Diary> diaryList;

    private SwipeRecyclerView recyclerView;
    private GroupAdapter adapter;

    private MyBroadcastReceiver receiver1, receiver2;
    private String date;

    private TextView tv_blank;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_diary_list, null);

            tv_blank = rootView.findViewById(R.id.blank);

            receiver1 = new MyBroadcastReceiver();
            rootView.getContext().registerReceiver(receiver1, new IntentFilter("new"));

            receiver2 = new MyBroadcastReceiver();
            rootView.getContext().registerReceiver(receiver2, new IntentFilter("change"));

            helper = new DiaryHelper(rootView.getContext());
            diaryList = helper.findAll();

//            addTestData();
            if (diaryList == null || diaryList.size() == 0) {
                tv_blank.setVisibility(View.VISIBLE);
            }

            Date today = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            date = format.format(today);

            initRecyclerView();
        }
        return rootView;
    }

    private void addTestData(){
        if(diaryList.size() == 0) {
            List<Diary> test = new ArrayList<>();

//            test.add(new Diary("2019.12.18", "cloudy", "diary test", "today isn't nice"));
//            test.add(new Diary("2019.12.17", "cloudy", "diary test", "today isn't nice"));
//            test.add(new Diary("2019.12.16", "cloudy", "diary test", "today isn't nice"));
//            test.add(new Diary("2019.12.15", "cloudy", "diary test", "today isn't nice"));
//            test.add(new Diary("2019.12.14", "cloudy", "diary test", "today isn't nice"));
//            test.add(new Diary("2019.12.13", "cloudy", "diary test", "today isn't nice"));
//            test.add(new Diary("2019.12.12", "cloudy", "diary test", "today isn't nice"));
//            test.add(new Diary("2019.12.11", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.12.10", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.12.09", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.12.08", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.12.07", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.12.06", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.12.05", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.12.04", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.12.03", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.12.02", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.12.01", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.30", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.29", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.28", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.27", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.26", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.25", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.24", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.23", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.22", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.21", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.20", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.19", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.18", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.17", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.16", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.15", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.14", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.13", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.12", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.11", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.10", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.09", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.08", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.07", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.06", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.05", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.04", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.03", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.02", "cloudy", "diary test", "today isn't nice"));
            test.add(new Diary("2019.11.01", "cloudy", "diary test", "today isn't nice"));

            for (int i = test.size() - 1; i >= 0; i--) {
                helper.insert(test.get(i));
            }

            diaryList = helper.findAll();
        }
    }
    private void add(){
        diaryList.add(new Diary("2019.10.31", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.30", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.29", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.28", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.27", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.26", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.25", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.24", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.23", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.22", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.21", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.20", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.19", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.18", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.17", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.16", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.15", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.14", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.13", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.12", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.11", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.10", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.09", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.08", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.07", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.06", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.05", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.04", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.03", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.02", "cloudy", "diary test", "today isn't nice"));
        diaryList.add(new Diary("2019.10.01", "cloudy", "diary test", "today isn't nice"));
    }

    private void initRecyclerView(){

        recyclerView = rootView.findViewById(R.id.recyclerView);

        recyclerView.setNestedScrollingEnabled(false); // 嵌套滑动

        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        recyclerView.addItemDecoration(new DefaultItemDecoration(getResources().getColor(R.color.divide)));

        // item点击监听
        recyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int adapterPosition) {
                if(!(diaryList.get(adapterPosition) instanceof StickyItem)){
                    Intent intent = new Intent(rootView.getContext(), DiaryReadActivity.class);
                    Diary diary = diaryList.get(adapterPosition);
                    intent.putExtra("pos", adapterPosition); // 回传时更新diaryList
                    intent.putExtra("id", diary.getId()); // 数据库操作定位
                    intent.putExtra("date", diary.getDate());
                    intent.putExtra("weather", diary.getWeather());
                    intent.putExtra("title", diary.getTitle());
                    intent.putExtra("content", diary.getContent());
                    startActivity(intent);
                }
            }
        });

        // 创建滑动菜单
        recyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
                int viewType = adapter.getItemViewType(position);
                if (viewType == GroupAdapter.VIEW_TYPE_NON_STICKY) {
                    SwipeMenuItem del = new SwipeMenuItem(rootView.getContext());
                    int len = getResources().getDimensionPixelSize(R.dimen.dp_40);
                    del.setWidth(len);
                    del.setHeight(len);
                    del.setImage(R.drawable.delete);
                    del.setText("删除");
                    rightMenu.addMenuItem(del);
                }
            }
        });

        // 滑动菜单监听
        recyclerView.setOnItemMenuClickListener(new OnItemMenuClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, final int adapterPosition) {
                new AlertView("确定要删除吗?", "日记删除后不可恢复!",
                        "我再想想", new String[]{"我要删除"}, null,
                        rootView.getContext(),
                        AlertView.Style.Alert,
                        new com.bigkoo.alertview.OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int position) {
                                if(position == 0){
                                    long id = diaryList.get(adapterPosition).getId();
                                    if(helper.delete(id) == 1) {
                                        diaryList.remove(adapterPosition);
                                        adapter.notifyItemRemoved(adapterPosition);
                                        if(!helper.find(date)){
                                            Intent intent = new Intent();
                                            intent.setAction("today is null");
                                            rootView.getContext().sendBroadcast(intent);
                                        }
                                    }
                                }
                            }
                        })
                        .setCancelable(true)
                        .show();
            }
        });

        adapter = new GroupAdapter();
        adapter.setItemList();
        recyclerView.setAdapter(adapter);

        // 下拉刷新
        final SwipeRefreshLayout refreshLayout = rootView.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        diaryList = helper.findAll();
                        if (diaryList == null || diaryList.size() == 0) {
                            tv_blank.setVisibility(View.VISIBLE);
                        }
                        adapter.setItemList();
                        refreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        // 加载更多
        recyclerView.useDefaultLoadMore();
        recyclerView.loadMoreFinish(false, true);
        recyclerView.setLoadMoreListener(new SwipeRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                // 该加载更多啦。
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 请求数据，并更新数据源操作
//                        int size = diaryList.size();
//                        add();
//                        adapter.notifyItemRangeInserted(diaryList.size() - size, 31);

                        // 加载完更多数据，一定要调用这个方法。
                        // 第一个参数：表示此次数据是否为空。
                        // 第二个参数：表示是否还有更多数据。
                        recyclerView.loadMoreFinish(false, true);

                        // 如果加载失败调用下面的方法，传入errorCode和errorMessage。
                        // errorCode随便传，你自定义LoadMoreView时可以根据errorCode判断错误类型。
                        // errorMessage是会显示到loadMoreView上的，用户可以看到。
                        // mRecyclerView.loadMoreError(0, "请求网络失败");
                    }
                }, 1000);
            }
        });
    }

    private class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder> {
        static final int VIEW_TYPE_STICKY = R.layout.layout_item_sticky;
        static final int VIEW_TYPE_NON_STICKY = R.layout.layout_rv_item;

        @NonNull
        @Override
        public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(viewType, parent, false);
            return new GroupViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
            if(getItemViewType(position) == VIEW_TYPE_NON_STICKY)
                holder.bind(diaryList.get(position));
            else {
                StickyItem stickyItem = (StickyItem) diaryList.get(position);
                holder.tv_year.setText(stickyItem.getYear());
                holder.tv_month.setText(stickyItem.getMonth());
            }
        }

        @Override
        public int getItemCount() {
            return diaryList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (diaryList.get(position) instanceof StickyItem) {
                return VIEW_TYPE_STICKY;
            }
            return VIEW_TYPE_NON_STICKY;
        }

        void setItemList() {
            StickyItem stickyItem = null;
            for(int i = 0, size = diaryList.size(); i < size; i++){
                Diary diary = diaryList.get(i);
                String [] date = diary.getDate().split("\\.");
                if (stickyItem == null || !stickyItem.getMonth().equals(date[1]) ||
                        !stickyItem.getYear().equals(date[0])) {
                    stickyItem = new StickyItem(null, null, null, null);
                    stickyItem.setYear(date[0]);
                    stickyItem.setMonth(date[1]);
                    diaryList.add(i, stickyItem);
                    size++;
                }
            }
            notifyDataSetChanged();
        }
    }

    private static class GroupViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_day;
        private TextView tv_summary;
        private TextView tv_year, tv_month;

        GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_day = itemView.findViewById(R.id.iv_day);
            tv_summary = itemView.findViewById(R.id.tv_summary);
            tv_year = itemView.findViewById(R.id.tv_year);
            tv_month = itemView.findViewById(R.id.tv_month);
        }

        void bind(Diary diary){
            int day = Integer.parseInt(diary.getDate().split("\\.")[2]);
            iv_day.setImageResource(days[day - 1]);
            tv_summary.setText(diary.getContent());
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("change".equals(intent.getAction())){
                int pos = intent.getIntExtra("pos", -1);
                diaryList.get(pos)
                        .setTitle(intent.getStringExtra("title"));
                diaryList.get(pos)
                        .setWeather(intent.getStringExtra("weather"));
                diaryList.get(pos)
                        .setContent(intent.getStringExtra("content"));
                adapter.notifyItemChanged(pos);
            }
            if("new".equals(intent.getAction())){
                tv_blank.setVisibility(View.GONE);
                Diary diary = new Diary();
                diary.setId(intent.getLongExtra("id", -1));
                diary.setDate(intent.getStringExtra("date"));
                diary.setWeather(intent.getStringExtra("weather"));
                diary.setTitle(intent.getStringExtra("title"));
                diary.setContent(intent.getStringExtra("content"));

                if(diaryList.size() == 0) {
                    // 用户第一次写日记
                    diaryList.add(0, diary);
                    adapter.setItemList();
                } else {
                    String [] date = diary.getDate().split("\\.");
                    StickyItem stickyItem = (StickyItem) diaryList.get(0);
                    if(stickyItem.getYear().equals(date[0])
                            && stickyItem.getMonth().equals(date[1]) )
                    {
                        diaryList.add(1, diary);
                        adapter.notifyItemInserted(1);
                    } else {
                        stickyItem = new StickyItem(null,null,null,null);
                        stickyItem.setYear(date[0]);
                        stickyItem.setMonth(date[1]);
                        diaryList.add(0, diary);
                        diaryList.add(0, stickyItem);
                        adapter.notifyItemRangeInserted(0,2);
                    }
                }
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        rootView.getContext().unregisterReceiver(receiver1);
        rootView.getContext().unregisterReceiver(receiver2);
    }

}