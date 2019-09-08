package com.smartyfy.bean_class;

import android.os.Parcel;
import android.os.Parcelable;

@Deprecated
public class InputDeviceOld implements Parcelable {
    public String id = "";
    public String name = "";

    public InputDeviceOld() {
        id = "";
        name = "";
    }

    public InputDeviceOld(InputDeviceOld inputDeviceOld) {
        this.id = inputDeviceOld.id;
        this.name = inputDeviceOld.name;
    }


    protected InputDeviceOld(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<InputDeviceOld> CREATOR = new Creator<InputDeviceOld>() {
        @Override
        public InputDeviceOld createFromParcel(Parcel in) {
            return new InputDeviceOld(in);
        }

        @Override
        public InputDeviceOld[] newArray(int size) {
            return new InputDeviceOld[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }
}
