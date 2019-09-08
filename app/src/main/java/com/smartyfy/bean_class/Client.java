package com.smartyfy.bean_class;

import android.os.Parcel;
import android.os.Parcelable;

public class Client implements Parcelable {
    public String id, name, date, client_ip, port, wifi_username, wifi_password, gprs_mob;
    public int inactive;
    public Client() {
        id = "";
        name = "";
        date = "";
        client_ip = "";
        port = "";
        inactive = 0;
        wifi_username = "";
        wifi_password = "";
        gprs_mob = "";
    }
    public Client(Client client) {
        id = client.id;
        name = client.id;
        date = client.date;
        client_ip = client.client_ip;
        port = client.port;
        inactive = client.inactive;
        wifi_username = client.wifi_username;
        wifi_password = client.wifi_password;
        gprs_mob = client.gprs_mob;
    }

    protected Client(Parcel in) {
        id = in.readString();
        name = in.readString();
        date = in.readString();
        client_ip = in.readString();
        port = in.readString();
        inactive = in.readInt();
        wifi_username = in.readString();
        wifi_password = in.readString();
        gprs_mob = in.readString();
    }

    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
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
        dest.writeString(date);
        dest.writeString(client_ip);
        dest.writeString(port);
        dest.writeInt(inactive);
        dest.writeString(wifi_username);
        dest.writeString(wifi_password);
        dest.writeString(gprs_mob);
    }
}
