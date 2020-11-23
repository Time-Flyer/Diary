package com.z.diary.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.z.diary.Entity.Music;
import com.z.diary.Entity.MusicList;
import com.z.diary.MainActivity;
import com.z.diary.R;
import com.z.diary.UI.SideBar;
import com.z.diary.Utils.Connection;

public class MusicFragment extends Fragment {

    private View rootView;
    private ListView listView;
    private TextView tv_blank;

    private MusicList musicList;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            musicList = (MusicList) getActivity().getApplication();
            rootView = inflater.inflate(R.layout.fragment_music, null);
            tv_blank = rootView.findViewById(R.id.blank);
            if(musicList.getList() == null ||
                musicList.getList().size() == 0){
                tv_blank.setVisibility(View.VISIBLE);
                tv_blank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(rootView.getContext(), "重启应用试试看", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            init();
        }
        return rootView;
    }

    private void init() {

        ListViewAdapter adapter = new ListViewAdapter(rootView.getContext());

        listView = rootView.findViewById(R.id.music_list);
        listView.setAdapter(adapter);

        // 侧边筛选
        SideBar sideBar = rootView.findViewById(R.id.side_bar);
        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                if(musicList.getList() != null) {
                    for (int i = 0; i < musicList.getList().size(); i++){
                        if(selectStr.equalsIgnoreCase(musicList.getList().get(i).getHeaderWord())){
                            listView.setSelection(i);
                            return;
                        }
                    }
                }
            }
        });
    }

    private class ListViewAdapter extends BaseAdapter{

        private LayoutInflater inflater;

        ListViewAdapter(Context context){
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (musicList.getList() == null)
                return 0;
            return musicList.getList().size();
        }

        @Override
        public Object getItem(int i) {
            if(i < musicList.getList().size()){
                return musicList.getList().get(i);
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if(view == null){
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.layout_music_item, null);
                holder.tv_headWord = view.findViewById(R.id.head_word);
                holder.tv_music = view.findViewById(R.id.music_music);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            final int pos = i;
            Music music = musicList.getList().get(pos);
            String word = music.getHeaderWord();
            holder.tv_headWord.setText(word);
            holder.tv_music.setText(music.getMusic());
            // 点击播放
            holder.tv_music.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Connection.getBinder().play(pos);
                }
            });

            if(i == 0){
                holder.tv_headWord.setVisibility(View.VISIBLE);
            } else {
                String headWord = musicList.getList().get(i - 1).getHeaderWord();
                if (headWord.equals(word)){
                    holder.tv_headWord.setVisibility(View.GONE);
                } else {
                    holder.tv_headWord.setVisibility(View.VISIBLE);
                }
            }
            return view;
        }

        private class ViewHolder{
            private TextView tv_music, tv_headWord;
        }
    }

}