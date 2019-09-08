package com.smartyfy.bean_class;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Room implements Parcelable {
    public String id;
    public String name;
    public String url;
    public String icon_id;
    @Deprecated
    public List<Button> buttons;
    public List<InputDevice> inputDevices;
    public List<OutputDevice> outputDevices;

    public Room(Room room) {
        id = room.id;
        name = room.name;
        url = room.url;
        icon_id = room.icon_id;
        buttons = new ArrayList<Button>();
        for (Button button : room.buttons) {
            Button button1 = new Button(button);
            this.buttons.add(button1);
        }
        inputDevices = new ArrayList<InputDevice>();
        for (InputDevice inputDevice : room.inputDevices) {
            InputDevice inputDevice1 = new InputDevice(inputDevice);
            this.inputDevices.add(inputDevice);
        }
        outputDevices = new ArrayList<OutputDevice>();
        for (OutputDevice outputDevice : room.outputDevices) {
            OutputDevice outputDevice1 = new OutputDevice(outputDevice);
            this.outputDevices.add(outputDevice);
        }
    }

    public Room() {
        id = "";
        name = "";
        url = "";
        icon_id = "";
        buttons = new ArrayList<Button>();
        inputDevices = new ArrayList<InputDevice>();
        outputDevices = new ArrayList<OutputDevice>();
    }

    protected Room(Parcel in) {
        id = in.readString();
        name = in.readString();
        url = in.readString();
        icon_id = in.readString();
        buttons = in.createTypedArrayList(Button.CREATOR);
        inputDevices = in.createTypedArrayList(InputDevice.CREATOR);
        outputDevices = in.createTypedArrayList(OutputDevice.CREATOR);
    }

    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<Button>();
        for (Button button : this.buttons) {
            if (!button.type.equals("Sensor")) {
                buttons.add(button);
            }
        }
        return buttons;
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
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
        dest.writeString(url);
        dest.writeString(icon_id);
        dest.writeTypedList(buttons);
        dest.writeTypedList(inputDevices);
        dest.writeTypedList(outputDevices);
    }
}
