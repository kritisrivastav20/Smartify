package com.smartyfy.library;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.smartyfy.R;
import com.smartyfy.bean_class.Button;
import com.smartyfy.bean_class.Room;

import java.util.ArrayList;
import java.util.List;

import top.defaults.drawabletoolbox.DrawableBuilder;
import yuku.ambilwarna.AmbilWarnaDialog;
@Deprecated
public class ButtonViewAdapter extends RecyclerView.Adapter<ButtonViewAdapter.ViewHolder> {
    private static final String TAG = "ButtonViewAdapter";

    private List<Button> buttons;

    private Context context;

    private Callback callback;

    public ButtonViewAdapter(Context context) {
        this.context = context;
        this.buttons = new ArrayList<Button>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Button button = buttons.get(i);
        viewHolder.tv_type.setText(button.type);
        if (button.hasColor == 1) {
            viewHolder.bt_change_color.setVisibility(View.VISIBLE);
            viewHolder.vw_background.setBackgroundDrawable(getBackgroundDrawable(button.colorValue));
            viewHolder.sw_state.setVisibility(View.GONE);
        } else {
            viewHolder.sw_state.setVisibility(View.VISIBLE);
            viewHolder.bt_change_color.setVisibility(View.GONE);
        }
        if (!button.icon.isEmpty())
            viewHolder.iv_icon.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(button.icon, "drawable",context.getPackageName())));
//        viewHolder.vw_background.setBackgroundDrawable(context.getResources().getDrawable(button.isButtonOn() ? R.drawable.selected_background : R.drawable.unselected_background));
        viewHolder.sb_dimmer.setVisibility(button.dimming == 1 ? View.VISIBLE : View.GONE);
        viewHolder.sb_dimmer.setProgress(button.getDimmerProgress());
        viewHolder.sw_state.setChecked(button.isButtonOn());
//        viewHolder.sw_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (null != callback)
//                    callback.onButtonStateChange(button, viewHolder.getAdapterPosition(), isChecked);
//            }
//        });
        viewHolder.sw_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.sw_state.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                if (null != callback)
                    callback.onButtonStateChange(button, viewHolder.sw_state.isChecked());
            }
        });
        viewHolder.sb_dimmer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                viewHolder.sb_dimmer.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                button.setDimmerProgress(progress);
                if (null != callback)
                    callback.onSeekbarChange(button);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        viewHolder.bt_change_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.bt_change_color.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                showColorModal(button);
            }
        });
    }

    public Drawable getBackgroundDrawable(String color) {
        int color_int = Color.RED;
        try {
            Color.parseColor("#" + color);
            color_int = Color.parseColor("#" + color);
        } catch (Exception e) {
            Log.e(TAG, "Unable to parse color " + color);
        }
        return new DrawableBuilder()
                .oval()
                .solidColor(color_int)
                .build();
    }

    private void showColorModal(final Button button) {
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
                    callback.onButtonStateChange(button, true);
            }
        };
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(context, color, true, listener);
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return buttons.size();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = new ArrayList<Button>();
        for (int i = 0, size = buttons.size(); i < size ; i++) {
            this.buttons.add(buttons.get(i));
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View vw_background;
        final TextView tv_type;
        final ImageView iv_icon;
        final Switch sw_state;
        final SeekBar sb_dimmer;
        final android.widget.Button bt_change_color;
        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_button_room_view, parent, false));
            vw_background = itemView.findViewById(R.id.vw_background);
            tv_type = itemView.findViewById(R.id.tv_type);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            sw_state = itemView.findViewById(R.id.sw_state);
            sw_state.setHapticFeedbackEnabled(true);
            sb_dimmer = itemView.findViewById(R.id.sb_dimmer);
            sb_dimmer.setHapticFeedbackEnabled(true);
            bt_change_color = itemView.findViewById(R.id.bt_change_color);
            bt_change_color.setHapticFeedbackEnabled(true);
        }
    }

    public interface Callback {
        public void onSeekbarChange(Button button);
        public void onButtonStateChange(Button button, boolean isChecked);
    }
}
