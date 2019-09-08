package com.smartyfy.bean_class;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Smartyfy implements Parcelable {
    public List<Room> rooms;
    @Deprecated
    public String first_name;
    @Deprecated
    public String last_name;
    @Deprecated
    public String reg_mobile;
    @Deprecated
    public String password;
    @Deprecated
    public String wifi_username;
    @Deprecated
    public String wifi_password;
    @Deprecated
    public String gprs_mobile;
    public Client client;
    public List<User> users;


    public Smartyfy(Smartyfy smartyfy) {
        first_name = smartyfy.first_name;
        last_name = smartyfy.last_name;
        reg_mobile = smartyfy.reg_mobile;
        password = smartyfy.password;
        wifi_password = smartyfy.wifi_password;
        wifi_username = smartyfy.wifi_username;
        gprs_mobile = smartyfy.gprs_mobile;
        client = new Client(smartyfy.client);
        rooms = new ArrayList<Room>();
        for (Room room : smartyfy.rooms) {
            Room room1 = new Room(room);
            this.rooms.add(room1);
        }
        users = new ArrayList<User>();
        for (User user : smartyfy.users) {
            User user1 = new User(user);
            this.users.add(user1);
        }
    }

    public Smartyfy() {
        rooms = new ArrayList<Room>();
        first_name = "";
        last_name = "";
        reg_mobile = "";
        password = "";
        wifi_password = "";
        wifi_username = "";
        gprs_mobile = "";
        client = new Client();
        users = new ArrayList<User>();
    }

    protected Smartyfy(Parcel in) {
        rooms = in.createTypedArrayList(Room.CREATOR);
        first_name = in.readString();
        last_name = in.readString();
        reg_mobile = in.readString();
        password = in.readString();
        wifi_username = in.readString();
        wifi_password = in.readString();
        gprs_mobile = in.readString();
        client = in.readParcelable(Client.class.getClassLoader());
        users = in.createTypedArrayList(User.CREATOR);
    }

    public static final Creator<Smartyfy> CREATOR = new Creator<Smartyfy>() {
        @Override
        public Smartyfy createFromParcel(Parcel in) {
            return new Smartyfy(in);
        }

        @Override
        public Smartyfy[] newArray(int size) {
            return new Smartyfy[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(rooms);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(reg_mobile);
        dest.writeString(password);
        dest.writeString(wifi_username);
        dest.writeString(wifi_password);
        dest.writeString(gprs_mobile);
        dest.writeParcelable(client, flags);
        dest.writeTypedList(users);
    }
}
