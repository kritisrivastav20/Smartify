package com.smartyfy.bean_class;

import android.os.Parcel;
import android.os.Parcelable;

public class Icon implements Parcelable {
    public String id, icon_name, url;
    public Icon() {
       id = "";
       icon_name = "";
       url = "";
    }

    public Icon(Icon icon) {
        id = icon.id;
        icon_name = icon.icon_name;
        url = icon.url;
    }


    protected Icon(Parcel in) {
        id = in.readString();
        icon_name = in.readString();
        url = in.readString();
    }

    public static final Creator<Icon> CREATOR = new Creator<Icon>() {
        @Override
        public Icon createFromParcel(Parcel in) {
            return new Icon(in);
        }

        @Override
        public Icon[] newArray(int size) {
            return new Icon[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(icon_name);
        dest.writeString(url);
    }
}
