package com.smartyfy.library;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.smartyfy.DashboardTabFragment;
import com.smartyfy.bean_class.Room;

import java.util.List;

public class DashboardTabAdapter extends FragmentStatePagerAdapter {
    private List<Room> rooms;

    public DashboardTabAdapter(FragmentManager fm, List<Room> rooms) {
        super(fm);
        this.rooms = rooms;
    }

    @Override
    public Fragment getItem(int i) {
        return DashboardTabFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return rooms.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return rooms.get(position).name;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
