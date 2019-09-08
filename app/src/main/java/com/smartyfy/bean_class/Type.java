package com.smartyfy.bean_class;

import android.os.Parcel;
import android.os.Parcelable;

public class Type implements Parcelable {
    public String id, type;
    public int output;
    public Type() {
        id = "";
        type = "";
        output = 0;
    }
    public Type(Type type) {
        id = type.id;
        this.type = type.id;
        output = type.output;
    }

    protected Type(Parcel in) {
        id = in.readString();
        type = in.readString();
        output = in.readInt();
    }

    public static final Creator<Type> CREATOR = new Creator<Type>() {
        @Override
        public Type createFromParcel(Parcel in) {
            return new Type(in);
        }

        @Override
        public Type[] newArray(int size) {
            return new Type[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeInt(output);
    }
}
