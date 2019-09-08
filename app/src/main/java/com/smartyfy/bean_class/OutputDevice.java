package com.smartyfy.bean_class;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class OutputDevice implements Parcelable {
    public String index, all_off, on_time, type_id, icon_url, icon_id, device_name, icon_name, off_time, all_on, activation_time, type, id, value;
    public int sensor_id;
    public int motorised_control;

    public OutputDevice() {
        index = "";
        all_off = "";
        on_time = "";
        type_id = "1";
        icon_url = "";
        icon_id = "";
        device_name = "";
        icon_name = "";
        off_time = "";
        all_on = "";
        activation_time = "";
        type = "";
        id = "";
        value = "0";
        sensor_id = -1;
        motorised_control = 0;
    }

    public OutputDevice(OutputDevice outputDevice) {
        index = outputDevice.index;
        all_off = outputDevice.all_off;
        on_time = outputDevice.on_time;
        type_id = outputDevice.type_id;
        icon_url = outputDevice.icon_url;
        icon_id = outputDevice.icon_id;
        device_name = outputDevice.device_name;
        icon_name = outputDevice.icon_name;
        off_time = outputDevice.off_time;
        all_on = outputDevice.all_on;
        activation_time = outputDevice.activation_time;
        type = outputDevice.type;
        id = outputDevice.id;
        value = outputDevice.value;
        sensor_id = outputDevice.sensor_id;
        motorised_control = outputDevice.motorised_control;
    }

    protected OutputDevice(Parcel in) {
        index = in.readString();
        all_off = in.readString();
        on_time = in.readString();
        type_id = in.readString();
        icon_url = in.readString();
        icon_id = in.readString();
        device_name = in.readString();
        icon_name = in.readString();
        off_time = in.readString();
        all_on = in.readString();
        activation_time = in.readString();
        type = in.readString();
        id = in.readString();
        value = in.readString();
        sensor_id = in.readInt();
        motorised_control = in.readInt();
    }

    public static final Creator<OutputDevice> CREATOR = new Creator<OutputDevice>() {
        @Override
        public OutputDevice createFromParcel(Parcel in) {
            return new OutputDevice(in);
        }

        @Override
        public OutputDevice[] newArray(int size) {
            return new OutputDevice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(index);
        dest.writeString(all_off);
        dest.writeString(on_time);
        dest.writeString(type_id);
        dest.writeString(icon_url);
        dest.writeString(icon_id);
        dest.writeString(device_name);
        dest.writeString(icon_name);
        dest.writeString(off_time);
        dest.writeString(all_on);
        dest.writeString(activation_time);
        dest.writeString(type);
        dest.writeString(id);
        dest.writeString(value);
        dest.writeInt(sensor_id);
        dest.writeInt(motorised_control);
    }

    @NonNull
    @Override
    public String toString() {
        JSONObject data = new JSONObject();
        try {
            data.put("index", index)
                    .put("all_off", all_off)
                    .put("all_on", all_on)
                    .put("on_time", on_time)
                    .put("type_id", type_id)
                    .put("icon_url", icon_url)
                    .put("icon_id", icon_id)
                    .put("device_name", device_name)
                    .put("icon_name", icon_name)
                    .put("off_time", off_time)
                    .put("activation_time", activation_time)
                    .put("type", type)
                    .put("id", id)
                    .put("value", value)
                    .put("sensor_id", sensor_id)
                    .put("motorised_control", motorised_control);
            return data.toString();
        } catch (JSONException ignored) {

        }
        return super.toString();
    }
}
