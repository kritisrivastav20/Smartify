package com.smartyfy.bean_class;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class Button implements Parcelable {
    @Deprecated
    public String id;
    public int value;
    @Deprecated
    public int pin;
    public String icon;
    public String type;
    public int remote_access;
    public int dimming;
    public int detect_motion;
    public int hasColor;
    public String start;
    public String end;
    public int deactivate;
    public String motion_id;
    public boolean isNew;
    public String sensorId;
    public String sensorName;
    public String onTime;
    public String offTime;
    public String colorValue;

    public boolean isButtonOn() {
        if (dimming == 1) {
            return isDimmerSwitchOn();
        }
        if (dimming == 0) {
            return isNrmlSwitchOn();
        }
        return false;
    }

    public boolean isNrmlSwitchOn() {
        if (detect_motion == 1) {
            return value == 3;
        }
        if (detect_motion == 0) {
            return value == 1;
        }
        return false;
    }

    public boolean isDimmerSwitchOn() {
        if (detect_motion == 1) {
            return value > 60;
        }
        if (detect_motion == 0) {
            return value > 10;
        }
        return false;
    }

    public int getDimmerProgress() {
        if (detect_motion == 1) {
            if (value <= 60) {
                return 0;
            }
            if (value <= 70) {
                return 1;
            }
            if (value <= 80) {
                return 2;
            }
            if (value <= 90) {
                return 3;
            }
            if (value <= 100) {
                return 4;
            }
        }
        if (detect_motion == 0) {
            if (value <= 10) {
                return 0;
            }
            if (value <= 20) {
                return 1;
            }
            if (value <= 30) {
                return 2;
            }
            if (value <= 40) {
                return 3;
            }
            if (value <= 50) {
                return 4;
            }
        }
        return 0;
    }

    public int onButtonStateChange() {
        if (detect_motion == 1) {
            if (dimming == 1) {
                return isDimmerSwitchOn() ? 60 : 100; //Toggle Switch
            }
            if (dimming == 0) {
                return isNrmlSwitchOn() ? 2 : 3; //Toggle Switch
            }
        }
        if (detect_motion == 0) {
            if (dimming == 1) {
                return isDimmerSwitchOn() ? 10 : 50; //Toggle Switch
            }
            if (dimming == 0) {
                return isNrmlSwitchOn() ? 0 : 1; //Toggle Switch
            }
        }
        return 0;
    }

    public void toggleButtonState() {
        value = onButtonStateChange();
    }

    public int onButtonStateChange(boolean state) {
        if (hasColor == 1) {
            if (state) {
//                colorValue = String.format("%08X", (Color.YELLOW));
                return 1;
            } else {
//                colorValue = String.format("%08X", Color.argb(0,0,0,0));
                return 0;
            }
        }
        if (detect_motion == 1) {
            if (dimming == 1) {
                return state ? 100 : 60; //Toggle Switch
            }
            if (dimming == 0) {
                return state ? 3 : 2; //Toggle Switch
            }
        }
        if (detect_motion == 0) {
            if (dimming == 1) {
                return state ? 50 : 10; //Toggle Switch
            }
            if (dimming == 0) {
                return state ? 1 : 0; //Toggle Switch
            }
        }
        return 0;
    }

    public void toggleButtonState(boolean state) {
        value = onButtonStateChange(state);
    }

    public void setDimmerProgress(int progress) {
        if (detect_motion == 1) {
            if (progress == 0) {
                value = 60;
                return;
            }
            if (progress == 1) {
                value = 70;
                return;
            }
            if (progress == 2) {
                value = 80;
                return;
            }
            if (progress == 3) {
                value = 90;
                return;
            }
            if (progress == 4) {
                value = 100;
                return;
            }
        }
        if (detect_motion == 0) {
            if (progress == 0) {
                value = 10;
                return;
            }
            if (progress == 1) {
                value = 20;
                return;
            }
            if (progress == 2) {
                value = 30;
                return;
            }
            if (progress == 3) {
                value = 40;
                return;
            }
            if (progress == 4) {
                value = 50;
                return;
            }
        }
        value = 0;
    }

    public Button(Button button) {
        id = button.id;
        value = button.value;
        pin = button.pin;
        icon = button.icon;
        type = button.type;
        remote_access = button.remote_access;
        dimming = button.dimming;
        detect_motion = button.detect_motion;
        start = button.start;
        end = button.end;
        deactivate = button.deactivate;
        motion_id = button.motion_id;
        isNew = button.isNew;
        sensorName = button.sensorName;
        sensorId = button.sensorId;
        onTime = button.onTime;
        offTime = button.offTime;
        hasColor = button.hasColor;
        colorValue = button.colorValue;
    }

    public Button() {
        id = "";
        value = 0;
        pin = 0;
        icon = "";
        type = "";
        remote_access = 0;
        dimming = 0;
        detect_motion = 0;
        start = "";
        end = "";
        deactivate = 0;
        motion_id = "";
        isNew = false;
        sensorId = "";
        sensorName = "";
        onTime = "00:00";
        offTime = "00:00";
        hasColor = 0;
        colorValue = "";
    }


    protected Button(Parcel in) {
        id = in.readString();
        value = in.readInt();
        pin = in.readInt();
        icon = in.readString();
        type = in.readString();
        remote_access = in.readInt();
        dimming = in.readInt();
        detect_motion = in.readInt();
        start = in.readString();
        end = in.readString();
        deactivate = in.readInt();
        motion_id = in.readString();
        sensorId = in.readString();
        sensorName = in.readString();
        isNew = in.readByte() != 0;
        onTime = in.readString();
        offTime = in.readString();
        hasColor = in.readInt();
        colorValue = in.readString();
    }

    public static final Creator<Button> CREATOR = new Creator<Button>() {
        @Override
        public Button createFromParcel(Parcel in) {
            return new Button(in);
        }

        @Override
        public Button[] newArray(int size) {
            return new Button[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(value);
        dest.writeInt(pin);
        dest.writeString(icon);
        dest.writeString(type);
        dest.writeInt(remote_access);
        dest.writeInt(dimming);
        dest.writeInt(detect_motion);
        dest.writeString(start);
        dest.writeString(end);
        dest.writeInt(deactivate);
        dest.writeString(motion_id);
        dest.writeString(sensorId);
        dest.writeString(sensorName);
        dest.writeByte((byte) (isNew ? 1 : 0));
        dest.writeString(onTime);
        dest.writeString(offTime);
        dest.writeInt(hasColor);
        dest.writeString(colorValue);
    }
}
