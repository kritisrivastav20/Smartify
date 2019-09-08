package com.smartyfy.library;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartyfy.R;
import com.smartyfy.bean_class.Button;
import com.smartyfy.bean_class.Room;

import java.util.ArrayList;
import java.util.List;

import top.defaults.drawabletoolbox.DrawableBuilder;
import yuku.ambilwarna.AmbilWarnaDialog;
@Deprecated
public class DashboardRoomAdapter extends RecyclerView.Adapter<DashboardRoomAdapter.ViewHolder> {
    private static final String TAG = "DashboardRoomAdapter";
    private List<Room> roomList;
    private Callback callback;
    private Context context;
    public DashboardRoomAdapter(Context context) {
        roomList = new ArrayList<Room>();
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Log.e(TAG, "Populating View");
        final Room room = roomList.get(i);
        viewHolder.tv_room_name.setText(room.name);
        final List<Button> buttons = room.getButtons();
        if (buttons.size() >= 6) {
            viewHolder.iv_bt_1.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_1.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(0).icon,"drawable",context.getPackageName())));
            if (buttons.get(0).hasColor == 0)
                viewHolder.vw_bt_1.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(0).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_1.setBackgroundDrawable(getDrawableBackground(buttons.get(0).colorValue));
            }
            viewHolder.vw_bt_1.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_2.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_2.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(1).icon,"drawable",context.getPackageName())));
            if (buttons.get(1).hasColor == 0)
                viewHolder.vw_bt_2.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(1).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_2.setBackgroundDrawable(getDrawableBackground(buttons.get(1).colorValue));
            }
            viewHolder.vw_bt_2.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_3.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_3.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(2).icon,"drawable",context.getPackageName())));
            if (buttons.get(2).hasColor == 0)
                viewHolder.vw_bt_3.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(2).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_3.setBackgroundDrawable(getDrawableBackground(buttons.get(2).colorValue));
            }
            viewHolder.vw_bt_3.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_4.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_4.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(3).icon,"drawable",context.getPackageName())));
            if (buttons.get(3).hasColor == 0)
                viewHolder.vw_bt_4.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(3).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_4.setBackgroundDrawable(getDrawableBackground(buttons.get(3).colorValue));
            }
            viewHolder.vw_bt_4.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_5.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_5.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(4).icon,"drawable",context.getPackageName())));
            if (buttons.get(4).hasColor == 0)
                viewHolder.vw_bt_5.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(4).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_5.setBackgroundDrawable(getDrawableBackground(buttons.get(4).colorValue));
            }
            viewHolder.vw_bt_5.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_6.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_6.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(5).icon,"drawable",context.getPackageName())));
            if (buttons.get(5).hasColor == 0)
                viewHolder.vw_bt_6.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(5).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_6.setBackgroundDrawable(getDrawableBackground(buttons.get(5).colorValue));
            }
            viewHolder.vw_bt_6.setVisibility(View.VISIBLE);
        }
        if (buttons.size() == 5) {
            viewHolder.iv_bt_1.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_1.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(0).icon,"drawable",context.getPackageName())));
            if (buttons.get(0).hasColor == 0)
                viewHolder.vw_bt_1.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(0).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_1.setBackgroundDrawable(getDrawableBackground(buttons.get(0).colorValue));
            }
            viewHolder.vw_bt_1.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_2.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_2.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(1).icon,"drawable",context.getPackageName())));
            if (buttons.get(1).hasColor == 0)
                viewHolder.vw_bt_2.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(1).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_2.setBackgroundDrawable(getDrawableBackground(buttons.get(1).colorValue));
            }
            viewHolder.vw_bt_2.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_3.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_3.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(2).icon,"drawable",context.getPackageName())));
            if (buttons.get(2).hasColor == 0)
                viewHolder.vw_bt_3.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(2).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_3.setBackgroundDrawable(getDrawableBackground(buttons.get(2).colorValue));
            }
            viewHolder.vw_bt_3.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_4.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_4.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(3).icon,"drawable",context.getPackageName())));
            if (buttons.get(3).hasColor == 0)
                viewHolder.vw_bt_4.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(3).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_4.setBackgroundDrawable(getDrawableBackground(buttons.get(3).colorValue));
            }
            viewHolder.vw_bt_4.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_5.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_5.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(4).icon,"drawable",context.getPackageName())));
            if (buttons.get(4).hasColor == 0)
                viewHolder.vw_bt_5.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(4).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_5.setBackgroundDrawable(getDrawableBackground(buttons.get(4).colorValue));
            }
            viewHolder.vw_bt_5.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_6.setVisibility(View.GONE);
            viewHolder.vw_bt_6.setVisibility(View.GONE);
        }
        if (buttons.size() == 4) {
            viewHolder.iv_bt_1.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_1.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(0).icon,"drawable",context.getPackageName())));
            if (buttons.get(0).hasColor == 0)
                viewHolder.vw_bt_1.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(0).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_1.setBackgroundDrawable(getDrawableBackground(buttons.get(0).colorValue));
            }
            viewHolder.vw_bt_1.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_2.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_2.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(1).icon,"drawable",context.getPackageName())));
            if (buttons.get(1).hasColor == 0)
                viewHolder.vw_bt_2.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(1).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_2.setBackgroundDrawable(getDrawableBackground(buttons.get(1).colorValue));
            }
            viewHolder.vw_bt_2.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_3.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_3.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(2).icon,"drawable",context.getPackageName())));
            if (buttons.get(2).hasColor == 0)
                viewHolder.vw_bt_3.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(2).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_3.setBackgroundDrawable(getDrawableBackground(buttons.get(2).colorValue));
            }
            viewHolder.vw_bt_3.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_4.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_4.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(3).icon,"drawable",context.getPackageName())));
            if (buttons.get(3).hasColor == 0)
                viewHolder.vw_bt_4.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(3).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_4.setBackgroundDrawable(getDrawableBackground(buttons.get(3).colorValue));
            }
            viewHolder.vw_bt_4.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_5.setVisibility(View.GONE);
            viewHolder.vw_bt_5.setVisibility(View.GONE);
            viewHolder.iv_bt_6.setVisibility(View.GONE);
            viewHolder.vw_bt_6.setVisibility(View.GONE);
        }
        if (buttons.size() == 3) {
            viewHolder.iv_bt_1.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_1.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(0).icon,"drawable",context.getPackageName())));
            if (buttons.get(0).hasColor == 0)
                viewHolder.vw_bt_1.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(0).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_1.setBackgroundDrawable(getDrawableBackground(buttons.get(0).colorValue));
            }
            viewHolder.vw_bt_1.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_2.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_2.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(1).icon,"drawable",context.getPackageName())));
            if (buttons.get(1).hasColor == 0)
                viewHolder.vw_bt_2.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(1).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_2.setBackgroundDrawable(getDrawableBackground(buttons.get(1).colorValue));
            }
            viewHolder.vw_bt_2.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_3.setVisibility(View.VISIBLE);
            if (!buttons.get(2).icon.isEmpty())
            viewHolder.iv_bt_3.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(2).icon,"drawable",context.getPackageName())));
            if (buttons.get(2).hasColor == 0)
                viewHolder.vw_bt_3.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(2).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_3.setBackgroundDrawable(getDrawableBackground(buttons.get(2).colorValue));
            }
            viewHolder.vw_bt_3.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_4.setVisibility(View.GONE);
            viewHolder.vw_bt_4.setVisibility(View.GONE);
            viewHolder.iv_bt_5.setVisibility(View.GONE);
            viewHolder.vw_bt_5.setVisibility(View.GONE);
            viewHolder.iv_bt_6.setVisibility(View.GONE);
            viewHolder.vw_bt_6.setVisibility(View.GONE);
        }
        if (buttons.size() == 2) {
            viewHolder.iv_bt_1.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_1.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(0).icon,"drawable",context.getPackageName())));
            if (buttons.get(0).hasColor == 0)
                viewHolder.vw_bt_1.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(0).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_1.setBackgroundDrawable(getDrawableBackground(buttons.get(0).colorValue));
            }
            viewHolder.vw_bt_1.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_2.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_2.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(1).icon,"drawable",context.getPackageName())));
            if (buttons.get(1).hasColor == 0)
                viewHolder.vw_bt_2.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(1).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_2.setBackgroundDrawable(getDrawableBackground(buttons.get(1).colorValue));
            }
            viewHolder.vw_bt_2.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_3.setVisibility(View.GONE);
            viewHolder.vw_bt_3.setVisibility(View.GONE);
            viewHolder.iv_bt_4.setVisibility(View.GONE);
            viewHolder.vw_bt_4.setVisibility(View.GONE);
            viewHolder.iv_bt_5.setVisibility(View.GONE);
            viewHolder.vw_bt_5.setVisibility(View.GONE);
            viewHolder.iv_bt_6.setVisibility(View.GONE);
            viewHolder.vw_bt_6.setVisibility(View.GONE);
        }
        if (buttons.size() == 1) {
            viewHolder.iv_bt_1.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_1.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(buttons.get(0).icon,"drawable",context.getPackageName())));
            if (buttons.get(0).hasColor == 0)
                viewHolder.vw_bt_1.setBackgroundDrawable(context.getResources().getDrawable(buttons.get(0).isButtonOn() ?  R.drawable.selected_background : R.drawable.unselected_background));
            else {
                viewHolder.vw_bt_1.setBackgroundDrawable(getDrawableBackground(buttons.get(0).colorValue));
            }
            viewHolder.vw_bt_1.setVisibility(View.VISIBLE);
            viewHolder.iv_bt_2.setVisibility(View.GONE);
            viewHolder.vw_bt_2.setVisibility(View.GONE);
            viewHolder.iv_bt_3.setVisibility(View.GONE);
            viewHolder.vw_bt_3.setVisibility(View.GONE);
            viewHolder.iv_bt_4.setVisibility(View.GONE);
            viewHolder.vw_bt_4.setVisibility(View.GONE);
            viewHolder.iv_bt_5.setVisibility(View.GONE);
            viewHolder.vw_bt_5.setVisibility(View.GONE);
            viewHolder.iv_bt_6.setVisibility(View.GONE);
            viewHolder.vw_bt_6.setVisibility(View.GONE);
        }
        if (buttons.size() == 0) {
            viewHolder.iv_bt_1.setVisibility(View.GONE);
            viewHolder.vw_bt_1.setVisibility(View.GONE);
            viewHolder.iv_bt_2.setVisibility(View.GONE);
            viewHolder.vw_bt_2.setVisibility(View.GONE);
            viewHolder.iv_bt_3.setVisibility(View.GONE);
            viewHolder.vw_bt_3.setVisibility(View.GONE);
            viewHolder.iv_bt_4.setVisibility(View.GONE);
            viewHolder.vw_bt_4.setVisibility(View.GONE);
            viewHolder.iv_bt_5.setVisibility(View.GONE);
            viewHolder.vw_bt_5.setVisibility(View.GONE);
            viewHolder.iv_bt_6.setVisibility(View.GONE);
            viewHolder.vw_bt_6.setVisibility(View.GONE);
        }

        viewHolder.vw_bt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.vw_bt_1.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                if (buttons.get(0).hasColor == 1) {
                    showColorModal(buttons.get(0), room, viewHolder.getAdapterPosition(), room.buttons.indexOf(buttons.get(0)));
                } else {
                    int value = buttons.get(0).onButtonStateChange();
                    if (null != callback)
                        callback.onBt1Clicked(buttons.get(0), room, viewHolder.getAdapterPosition(), room.buttons.indexOf(buttons.get(0)), value);
                }
            }
        });


        viewHolder.vw_bt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.vw_bt_2.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                if (buttons.get(1).hasColor == 1) {
                    showColorModal(buttons.get(1), room, viewHolder.getAdapterPosition(), room.buttons.indexOf(buttons.get(1)));
                } else {
                    int value = buttons.get(1).onButtonStateChange();
                    if (null != callback)
                        callback.onBt2Clicked(buttons.get(1), room, viewHolder.getAdapterPosition(), room.buttons.indexOf(buttons.get(1)), value);
                }
            }
        });


        viewHolder.vw_bt_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.vw_bt_3.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                if (buttons.get(2).hasColor == 1) {
                    showColorModal(buttons.get(2), room, viewHolder.getAdapterPosition(), room.buttons.indexOf(buttons.get(2)));
                } else {
                    int value = buttons.get(2).onButtonStateChange();
                    if (null != callback)
                        callback.onBt3Clicked(buttons.get(2), room, viewHolder.getAdapterPosition(), room.buttons.indexOf(buttons.get(2)), value);
                }
            }
        });


        viewHolder.vw_bt_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.vw_bt_4.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                if (buttons.get(3).hasColor == 1) {
                    showColorModal(buttons.get(3), room, viewHolder.getAdapterPosition(), room.buttons.indexOf(buttons.get(3)));
                } else {
                    int value = buttons.get(3).onButtonStateChange();
                    if (null != callback)
                        callback.onBt4Clicked(buttons.get(3), room, viewHolder.getAdapterPosition(), room.buttons.indexOf(buttons.get(3)), value);
                }
            }
        });


        viewHolder.vw_bt_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.vw_bt_5.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                if (buttons.get(4).hasColor == 1) {
                    showColorModal(buttons.get(4), room, viewHolder.getAdapterPosition(), room.buttons.indexOf(buttons.get(4)));
                } else {
                    int value = buttons.get(4).onButtonStateChange();
                    if (null != callback)
                        callback.onBt5Clicked(buttons.get(4), room, viewHolder.getAdapterPosition(), room.buttons.indexOf(buttons.get(4)), value);
                }
            }
        });


        viewHolder.vw_bt_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.vw_bt_6.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                if (buttons.get(5).hasColor == 1) {
                    showColorModal(buttons.get(5), room, viewHolder.getAdapterPosition(), room.buttons.indexOf(buttons.get(5)));
                } else {
                    int value = buttons.get(5).onButtonStateChange();
                    if (null != callback)
                        callback.onBt6Clicked(buttons.get(5), room, viewHolder.getAdapterPosition(), room.buttons.indexOf(buttons.get(5)), value);
                }
            }
        });

        viewHolder.ib_room_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.ib_room_edit.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                if (null != callback) {
                    callback.onEditClicked(room, viewHolder.getAdapterPosition());
                }
            }
        });
    }

    private void showColorModal(final Button button,final Room room,final int position, final int button_index) {
        int color = Color.WHITE;
        if (!button.colorValue.isEmpty() && button.colorValue.length() == 8) try {
            color = Color.parseColor("#" + button.colorValue);
        } catch (Exception e) {
            Log.e(TAG, "Unable to parse color " + button.colorValue);
        }
        AmbilWarnaDialog.OnAmbilWarnaListener listener = new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                button.colorValue = String.format("%08X", (color));
                if (null != callback)
                    callback.onBtClicked(button,room, position, button_index, 1);
            }
        };
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(context, color, true, listener);
        dialog.show();
    }

    private Drawable getDrawableBackground(String hexColor) {
        int color = Color.RED;
        try {
            Color.parseColor("#" + hexColor);
            color = Color.parseColor("#" + hexColor);
        } catch (Exception e) {
            Log.e(TAG, "Unable to parse color");
        }
        return new DrawableBuilder()
                .oval()
                .solidColor(color)
//                .bottomLeftRadius(20) // in pixels
//                .bottomRightRadius(20) // in pixels
//        .cornerRadii(0, 0, 20, 20) // the same as the two lines above
                .build();
    }

    @Override
    public int getItemCount() {
//        Log.e(TAG, String.valueOf(roomList.size()));
        return roomList.size();
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
        notifyDataSetChanged();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView iv_bt_1, iv_bt_2, iv_bt_3, iv_bt_4, iv_bt_5, iv_bt_6;
        final TextView tv_room_name;
        final ImageButton ib_room_edit;
        final View vw_bt_1, vw_bt_2, vw_bt_3, vw_bt_4, vw_bt_5, vw_bt_6;
        ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_dashboard_list, parent, false));

            iv_bt_1 = itemView.findViewById(R.id.iv_bt_1);
            iv_bt_2 = itemView.findViewById(R.id.iv_bt_2);
            iv_bt_3 = itemView.findViewById(R.id.iv_bt_3);
            iv_bt_4 = itemView.findViewById(R.id.iv_bt_4);
            iv_bt_5 = itemView.findViewById(R.id.iv_bt_5);
            iv_bt_6 = itemView.findViewById(R.id.iv_bt_6);

            vw_bt_1 = itemView.findViewById(R.id.vw_bt_1);
            vw_bt_1.setHapticFeedbackEnabled(true);
            vw_bt_2 = itemView.findViewById(R.id.vw_bt_2);
            vw_bt_2.setHapticFeedbackEnabled(true);
            vw_bt_3 = itemView.findViewById(R.id.vw_bt_3);
            vw_bt_3.setHapticFeedbackEnabled(true);
            vw_bt_4 = itemView.findViewById(R.id.vw_bt_4);
            vw_bt_4.setHapticFeedbackEnabled(true);
            vw_bt_5 = itemView.findViewById(R.id.vw_bt_5);
            vw_bt_5.setHapticFeedbackEnabled(true);
            vw_bt_6 = itemView.findViewById(R.id.vw_bt_6);
            vw_bt_6.setHapticFeedbackEnabled(true);

            tv_room_name = itemView.findViewById(R.id.tv_room_name);

            ib_room_edit = itemView.findViewById(R.id.ib_room_edit);
            ib_room_edit.setHapticFeedbackEnabled(true);
        }
    }

    public interface Callback {
        public void onBt1Clicked(Button button, Room room , int room_index, int button_index, int value);
        public void onBt2Clicked(Button button, Room room , int room_index, int button_index, int value);
        public void onBt3Clicked(Button button, Room room , int room_index, int button_index, int value);
        public void onBt4Clicked(Button button, Room room , int room_index, int button_index, int value);
        public void onBt5Clicked(Button button, Room room , int room_index, int button_index, int value);
        public void onBt6Clicked(Button button, Room room , int room_index, int button_index, int value);
        public void onBtClicked(Button button, Room room, int room_index, int button_index, int value);
        public void onEditClicked(Room room, int room_index);
    }
}
