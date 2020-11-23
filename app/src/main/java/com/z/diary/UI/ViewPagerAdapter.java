package com.z.diary.UI;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.z.diary.Fragment.DiaryListFragment;
import com.z.diary.Fragment.HomeFragment;
import com.z.diary.Fragment.MusicFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0: fragment = new HomeFragment(); break;
            case 1: fragment = new DiaryListFragment(); break;
            case 2: fragment = new MusicFragment(); break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
