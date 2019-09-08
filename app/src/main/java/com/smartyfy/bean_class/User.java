package com.smartyfy.bean_class;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public String id, name, mobile, mpin;
    public int inactive, isAdmin, client_user_active;
    public User() {
        id = "";
        name = "";
        mobile = "";
        mpin = "";
        inactive = 0;
        isAdmin = 0;
        client_user_active = 0;
    }

    public User(User user) {
        id = user.id;
        name = user.name;
        mobile = user.mobile;
        mpin = user.mpin;
        inactive = user.inactive;
        isAdmin = user.isAdmin;
        client_user_active = user.client_user_active;
    }

    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        mobile = in.readString();
        mpin = in.readString();
        inactive = in.readInt();
        isAdmin = in.readInt();
        client_user_active = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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
        dest.writeString(mobile);
        dest.writeString(mpin);
        dest.writeInt(inactive);
        dest.writeInt(isAdmin);
        dest.writeInt(client_user_active);
    }
}
