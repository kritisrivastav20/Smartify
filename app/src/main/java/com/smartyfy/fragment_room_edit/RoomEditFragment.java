package com.smartyfy.fragment_room_edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.smartyfy.DashboardActivity;
import com.smartyfy.OnFragmentInteractionListener;
import com.smartyfy.R;
import com.smartyfy.bean_class.InputDevice;
import com.smartyfy.bean_class.OutputDevice;
import com.smartyfy.bean_class.Room;
import com.smartyfy.bean_class.Smartyfy;
import com.smartyfy.library.BaseFragment;
import com.smartyfy.library.Connection;
import com.smartyfy.library.JSONResponse;
import com.smartyfy.library.image_loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoomEditFragment extends BaseFragment implements InternalListener, JSONResponse {
    public static final String TAG = "RoomEditFragment";
    private static final String MODE_ADD = "add", MODE_EDIT = "edit";
    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_INSERT = 2;
    private String mode = "";

    private OnFragmentInteractionListener mListener;

    private Room room;
    private int index;

    private File icon;

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private PagerAdapter adapter;

    private android.support.v7.app.AlertDialog dialog;
    private JSONObject data;

    private ProgressDialog progressDialog;

    private SharedPreferences pref;

    List<String> iconList = new ArrayList<String>();
    JSONArray roomIcons = new JSONArray();

    public RoomEditFragment(){}

    public static RoomEditFragment editInstance(Room room, int index) {
        RoomEditFragment fragment = new RoomEditFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Room", room);
        bundle.putInt("index", index);
        bundle.putString("Mode", MODE_EDIT);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RoomEditFragment addInstance() {
        RoomEditFragment fragment = new RoomEditFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Mode", MODE_ADD);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onDataUpdate(Smartyfy smartyfy) {
        super.onDataUpdate(smartyfy);
        this.room = smartyfy.rooms.get(index);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof BaseFragment) {
                ((BaseFragment) fragment).notifyData(smartyfy);
            }
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Room room = getArguments().getParcelable("Room");
            if (room != null)
                this.room = new Room(room);
            else this.room = new Room();
            index = getArguments().getInt("index", 0);
            mode = getArguments().getString("Mode", MODE_EDIT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_edit, container, false);
        setHasOptionsMenu(true);
        init(view);
        listener();
        if (getActivity() instanceof DashboardActivity)
            ((DashboardActivity) getActivity()).setTitle(room.name);
        return view;
    }

    private void init(View view) {
        pref = getContext().getSharedPreferences(getString(R.string.preference_name),Context.MODE_PRIVATE);
        viewPager = view.findViewById(R.id.viewPager);
        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        adapter = new PagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        progressDialog = new ProgressDialog(getContext());
    }

    private void listener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_inputs) {
                    viewPager.setCurrentItem(1);
                }
                if (menuItem.getItemId() == R.id.action_buttons) {
                    viewPager.setCurrentItem(0);
                }
                return false;
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                bottomNavigationView.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " activity not instance of OnFragmentInteractionListener.");
        }
    }

    @Override
    public void onInputSensorAdded() {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof BaseFragment)
                ((BaseFragment) fragment).onInputTypeAdded();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_save,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (validate()) {
                    createSaveDialog();
                }
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyTheme);
        View view = getLayoutInflater().inflate(R.layout.dialog_room_edit_save,null, false);
        final TextInputLayout til_name = view.findViewById(R.id.til_name), til_id = view.findViewById(R.id.til_id);
        final Spinner sp_icon = view.findViewById(R.id.sp_icon);
        final ImageView iv_icon = view.findViewById(R.id.iv_icon);



        int pos = 0;
        try {
            JSONArray icons = new JSONArray(pref.getString(getString(R.string.pref_icon), ""));
            for (int i = 0, size = icons.length(); i < size ; i++) {
                if (icons.getJSONObject(i).getString("type").equals("1")) {
                    iconList.add(icons.getJSONObject(i).getString("icon_name"));
                    roomIcons.put(icons.getJSONObject(i));
                    if (icons.getJSONObject(i).getString("url").equals(room.url)) {
                        pos = iconList.size() - 1;
                    }
                }
            }
            iconList.add("+ Add New");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_item, iconList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_icon.setAdapter(adapter);
        sp_icon.setSelection(pos);
        final int final_pos = pos;
        sp_icon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == roomIcons.length()) {
                    sp_icon.setSelection(final_pos);
                    showModal(MODE_ADD,new JSONObject(), new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            try {
                                iconList = new ArrayList<String>();
                                roomIcons = new JSONArray();
                                JSONArray icons = new JSONArray(pref.getString(getString(R.string.pref_icon), ""));
                                for (int i = 0, size = icons.length(); i < size ; i++) {
                                    if (icons.getJSONObject(i).getString("type").equals("1")) {
                                        iconList.add(icons.getJSONObject(i).getString("icon_name"));
                                        roomIcons.put(icons.getJSONObject(i));
                                    }
                                }
                                iconList.add("+ Add New");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_item, iconList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sp_icon.setAdapter(adapter);
                            sp_icon.setSelection(final_pos);
                        }
                    });
                    return;
                }
                ImageLoader loader = new ImageLoader(getContext(), R.drawable.room_placeholder);
                try {
                    loader.DisplayImage(roomIcons.getJSONObject(position).getString("url"), iv_icon);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        til_id.getEditText().setText(room.id);
        til_id.getEditText().setEnabled(false);
        til_id.getEditText().setClickable(false);
        til_name.getEditText().setText(room.name);
        ImageLoader loader = new ImageLoader(getContext(), R.drawable.room_placeholder);
        loader.DisplayImage(room.url, iv_icon);
        builder.setView(view);
        builder.setTitle("Save Room")
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (til_name.getEditText().getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Room Name Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (til_id.getEditText().getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Room ID Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                room.id = til_id.getEditText().getText().toString();
                room.name = til_name.getEditText().getText().toString();
                try {
                    room.url = roomIcons.getJSONObject(sp_icon.getSelectedItemPosition()).getString("url");
                    room.icon_id = roomIcons.getJSONObject(sp_icon.getSelectedItemPosition()).getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (mode.equals(MODE_EDIT))
                    mListener.onRoomEdit(room);
                if (mode.equals(MODE_ADD))
                    mListener.onRoomAdd(room);

                dialog.dismiss();
                Toast.makeText(getContext(), "Room Saved", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });
    }

    private void showModal(final String mode, final JSONObject data, DialogInterface.OnDismissListener dismiss) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext(), R.style.MyTheme);
        builder.setTitle(mode.equals(MODE_ADD) ? "New Icon" : "Update Icon");
        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", null);
        builder.setNeutralButton("Upload Logo", null);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_icon, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        final TextInputLayout til_name = view.findViewById(R.id.til_name);
        ImageView iv_icon = view.findViewById(R.id.iv_sample);
        dialog.setOnDismissListener(dismiss);
        icon = null;
        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoomEditFragment.this.data = new JSONObject();
                if (mode.equals(MODE_ADD)) {
                    if (til_name.getEditText().getText().length() == 0 || icon == null) {
                        Toast.makeText(getActivity(), "All fields Compulsory", Toast.LENGTH_SHORT).show();
                    } else try {
                        RoomEditFragment.this.data.put("icon_name", til_name.getEditText().getText().toString());
                        RoomEditFragment.this.data.put("type", "1");
                        connectServer(REQUEST_INSERT);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_PICTURE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode== Activity.RESULT_OK) {
            // Get the Image from data
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            // Get the cursor
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            icon = new File(imgDecodableString);
            // Set the Image in ImageView after decoding the String
            ((ImageView)dialog.findViewById(R.id.iv_sample)).setImageBitmap(BitmapFactory
                    .decodeFile(imgDecodableString));
        } else {
            Toast.makeText(getActivity(), "NO Logo Selected", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void connectServer(int request) {
        JSONObject jo_send = new JSONObject();
        String endpoint = "";
        Connection connection = new Connection(getActivity());
        connection.delegate = this;
        try {
            switch (request) {
                case REQUEST_INSERT:
                    progressDialog.setTitle("Saving Icon");
                    progressDialog.show();
                    jo_send = data;
                    if (null != icon) jo_send.put("logo", icon);
                    endpoint = "smartify.php?action=insertIcon";
                    connection.startFormDataConn(jo_send, endpoint, new HashMap<String, String>(), request);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validate() {
        for (OutputDevice outputDevice : room.outputDevices) {
            if (outputDevice.device_name.equals("")) {
                Toast.makeText(getContext(), "Button Name Required", Toast.LENGTH_LONG).show();
                return false;
            }
            if (outputDevice.type_id.trim().length() == 0) {
                Toast.makeText(getContext(), "Type Required", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (outputDevice.icon_id.trim().length()==0) {
                Toast.makeText(getContext(), "Icon Required", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        for (InputDevice inputDevice : room.inputDevices) {
            if (inputDevice.type_id.equals("")) {
                Toast.makeText(getContext(), "Sensor Type Required", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (inputDevice.device_name.trim().equals("")) {
                Toast.makeText(getContext(), "Sensor Name Required", Toast.LENGTH_SHORT).show();
                return false;
            }
//            if (inputDevice.deactivation_time.trim().equals("0") || inputDevice.deactivation_time.trim().length() == 0) {
//                Toast.makeText(getContext(), "Deactivation Time Required", Toast.LENGTH_SHORT).show();
//                return false;
//            }
        }
        return true;
    }

    @Override
    public void getResponse(JSONObject jsonObject, String trojan, int request_id, int status_code) throws JSONException {
        if (progressDialog.isShowing()) progressDialog.dismiss();
        if (status_code == 200) {
            if (jsonObject.length() > 0) {
                if (jsonObject.getString("error").equals("0")) {
                    switch (request_id) {
                        case REQUEST_INSERT:
                            Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray icons = new JSONArray(pref.getString(getString(R.string.pref_icon),""));
                            icons.put(data);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString(getString(R.string.pref_icon), icons.toString());
                            editor.apply();
                            if (dialog != null) dialog.dismiss();
                            break;
                    }
                } else {
                    Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return PagerFragment.newInstance(room, i, index);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
