package com.smartyfy;

import com.smartyfy.bean_class.Button;
import com.smartyfy.bean_class.OutputDevice;
import com.smartyfy.bean_class.Room;
import com.smartyfy.bean_class.Smartyfy;

public interface OnFragmentInteractionListener {
    @Deprecated
    public void onLogin(String username, String password);
    @Deprecated
    public void onButtonClicked(Button button, Room room, int value, int index);
    public void onRoomSelect(Room room, int room_index);
    @Deprecated
    public void onDimmerChange(Button button, Room room , int value, int index);
    public void onEEPROMChange();
    public void onRoomSelectEdit(Room room, int room_index);
    public void onNewRoomClicked();
    public void onRoomEdit(Room room);
    public void onRoomAdd(Room room);
    @Deprecated
    public void loginSuccessful(String username, String password);
    public void onAccountSettingChange(Smartyfy smartyfy);
    public void onValueChanged(OutputDevice outputDevice, Room room, int index);
    public void onMasterChanged(Room room,String value);
}
