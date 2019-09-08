package com.smartyfy.library;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartyfy.R;

import java.util.ArrayList;
import java.util.List;

public class NavigationMenuAdapter extends RecyclerView.Adapter<NavigationMenuAdapter.MenuItem> {
    private List<String> data;
    private List<String> icons;
    private Context context;
    private onClickListener listener;

    public NavigationMenuAdapter(Context context) {
        data = new ArrayList<String>();
        icons = new ArrayList<String>();
        this.context = context;

        data.add("Wifi Setting");
        icons.add("ic_menu_camera");

        data.add("Users");
        icons.add("ic_menu_camera");

        data.add("Sensors");
        icons.add("ic_menu_camera");

        data.add("Room Settings");
        icons.add("ic_menu_camera");
    }

    @NonNull
    @Override
    public MenuItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MenuItem(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuItem menuItem, int i) {
        menuItem.tv_menu.setText(data.get(i));
        menuItem.iv_menu.setImageResource(context.getResources().getIdentifier(icons.get(i),"drawable",context.getPackageName()));
        menuItem.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) listener.onClick(menuItem.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setListener(onClickListener listener) {
        this.listener = listener;
    }

    public class MenuItem extends RecyclerView.ViewHolder {
        final ImageView iv_menu;
        final TextView tv_menu;
        MenuItem(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_nav_menu_item, parent, false));
            iv_menu = itemView.findViewById(R.id.iv_menu);
            tv_menu = itemView.findViewById(R.id.tv_menu);
        }
    }

    public interface onClickListener {
        public void onClick(int position);
    }
}
