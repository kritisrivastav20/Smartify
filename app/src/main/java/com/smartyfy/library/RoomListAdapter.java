package com.smartyfy.library;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartyfy.R;
import com.smartyfy.bean_class.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {
    private static final String TAG = "RoomListAdapter";
    private List<Room> rooms;
    private Context context;
    private Callback callback;

    public RoomListAdapter(Context context) {
        this.context = context;
        this.rooms = new ArrayList<Room>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Room room = rooms.get(i);
        viewHolder.tv_name.setText(room.name);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.itemView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                if (callback != null)
                    callback.onRoomSelected(room, viewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
        notifyDataSetChanged();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_room_list, parent, false));
            tv_name = itemView.findViewById(R.id.tv_room_name);
            itemView.setHapticFeedbackEnabled(true);
        }
    }

    public interface Callback {
        public void onRoomSelected(Room room, int room_index);
    }
}
