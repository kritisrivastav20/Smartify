package com.smartyfy.bean_class;

import android.os.Parcel;
import android.os.Parcelable;

public class InputDevice implements Parcelable {
    public String type_id, type, start_time, device_name, end_time, deactivation_time, id, value, cart_no, position;
    public int show_ui;
    public Room room;
    public InputDevice() {
        type_id = "";
        type = "";
        start_time = "";
        device_name = "";
        end_time = "";
        deactivation_time = "";
        id = "";
        room = new Room();
        value = "0";
        cart_no= "0";
        position = "0";
        show_ui = 0;
    }
    public InputDevice(InputDevice inputDevice) {
        type_id = inputDevice.type_id;
        type = inputDevice.type;
        start_time = inputDevice.start_time;
        device_name = inputDevice.device_name;
        end_time = inputDevice.end_time;
        deactivation_time = inputDevice.deactivation_time;
        id = inputDevice.id;
        room = inputDevice.room;
        value = inputDevice.value;
        cart_no = inputDevice.cart_no;
        position = inputDevice.position;
        show_ui = inputDevice.show_ui;
    }

    protected InputDevice(Parcel in) {
        type_id = in.readString();
        type = in.readString();
        start_time = in.readString();
        device_name = in.readString();
        end_time = in.readString();
        deactivation_time = in.readString();
        id = in.readString();
        room = in.readParcelable(Room.class.getClassLoader());
        value = in.readString();
        cart_no = in.readString();
        position = in.readString();
        show_ui = in.readInt();
    }

    public static final Creator<InputDevice> CREATOR = new Creator<InputDevice>() {
        @Override
        public InputDevice createFromParcel(Parcel in) {
            return new InputDevice(in);
        }

        @Override
        public InputDevice[] newArray(int size) {
            return new InputDevice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type_id);
        dest.writeString(type);
        dest.writeString(start_time);
        dest.writeString(device_name);
        dest.writeString(end_time);
        dest.writeString(deactivation_time);
        dest.writeString(id);
        dest.writeParcelable(room, flags);
        dest.writeString(value);
        dest.writeString(cart_no);
        dest.writeString(position);
        dest.writeInt(show_ui);
    }
}
