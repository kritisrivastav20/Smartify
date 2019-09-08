package com.smartyfy.library;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartyfy.MainActivity;
import com.smartyfy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Tecno Waev on 13-01-2017.
 */

public class Permission {
    private static final String TAG = "Permission";
    public static final int PERMISSION_GRANT = 1;
    public static final int PERMISSION_DENY = -1;
    public static final int PERMISSION_DONT_ASK = 0;
    private static final int PERMISSION_NAME = 0, PERMISSION_REQUEST_CODE = 1, PERMISSION_REQUIRED = 2,
            PERMISSION_ICON = 3, PERMISSION_DONTASK_MSG = 4, PERMISSION_DENY_MSG = 5, PERMISSION_EXIT_IF_DONTASK = 6;
    private static final int REQUEST_REFRESH = 2000;
    private final HashMap<String, List<String>> permission = new HashMap<String, List<String>>();
    private List<String> permissions_ask = new ArrayList<String>();
    private FragmentActivity activity;
    public Permission(FragmentActivity activity) {
        this.activity = activity;
        //Todo append if want to add any new permission
        putPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage", "1", true
                , R.drawable.ic_sd_storage_black_24dp, "The app might not perform as expected without permissions grant. Please allow to continue",
                "The app might not perform as expected without permissions grant. Please allow to continue", false
        );
//        putPermission(Manifest.permission.READ_SMS, "Read SMS", "2", true, R.drawable.icon_message_bl,
//                "The app might not perform as expected without permissions grant. Please allow to continue",
//                "The app might not perform as expected without permissions grant. Please allow to continue", false);
//        putPermission(Manifest.permission.RECEIVE_SMS, "Receive SMS", "2", true, R.drawable.icon_message_bl,
//                "The app might not perform as expected without permissions grant. Please allow to continue",
//                "The app might not perform as expected without permissions grant. Please allow to continue", false);
//        putPermission(Manifest.permission.READ_PHONE_STATE, "Read Phone State", "2", true, R.drawable.icon_phone_bl,
//                "The app might not perform as expected without permissions grant. Please allow to continue",
//                "The app might not perform as expected without permissions grant. Please allow to continue", false);
//        putPermission(Manifest.permission.CALL_PHONE, "Calling functionality", "3", true,
//                R.drawable.icon_phone_bl, "The app might not perform as expected without permissions grant. Please allow to continue",
//                "The app might not perform as expected without permissions grant. Please allow to continue", false);
    }

    private void putPermission(String PermissionString, String name, String request_code,
                               boolean isRequired, int icon, String dontaskmsg, String denymsg, boolean doExit) {
        List<String> var = new ArrayList<String>();
        var.add(PERMISSION_NAME, name);
        var.add(PERMISSION_REQUEST_CODE, request_code);
        var.add(PERMISSION_REQUIRED, isRequired ? "1" : "0");
        var.add(PERMISSION_ICON, (icon == 0) ? "0" : (""+icon));
        var.add(PERMISSION_DONTASK_MSG, dontaskmsg);
        var.add(PERMISSION_DENY_MSG, denymsg);
        var.add(PERMISSION_EXIT_IF_DONTASK, doExit ? "1" : "0");
        permission.put(PermissionString, var);
    }

    /**
     * alter the required of the permission use {@link #onActivityResult(int, int, Intent)} compulsory to make things work.
     * @param permission the permission to be altered
     * @param name the name to be displayed in alert box if null default will be displayed
     * @param isRequired true if required false if not
     * @param iconRes the resource of the icon 0 for default
     * @param dontaskmsg the dontask msg null for default
     * @param denymsg the deny msg null for default
     * @param doExit do exit if dont ask is denied?
     * @return true if successfull else false
     */
    public boolean alterPermission(String permission, @Nullable String name, boolean isRequired,
                                   int iconRes, String dontaskmsg, String denymsg, boolean doExit) {
        if (this.permission.containsKey(permission)) {
            this.permission.get(permission).remove(PERMISSION_REQUIRED);
            this.permission.get(permission).add(PERMISSION_REQUIRED, isRequired ? "1" : "0");
            if (null != name) {
                this.permission.get(permission).remove(PERMISSION_NAME);
                this.permission.get(permission).add(PERMISSION_NAME, name);
            }
            if (0 != iconRes) {
                this.permission.get(permission).remove(PERMISSION_ICON);
                this.permission.get(permission).add(PERMISSION_ICON, iconRes + "");
            }
            if (null != dontaskmsg) {
                this.permission.get(permission).remove(PERMISSION_DONTASK_MSG);
                this.permission.get(permission).add(PERMISSION_DONTASK_MSG, dontaskmsg);
            }
            if (null != denymsg) {
                this.permission.get(permission).remove(PERMISSION_DENY_MSG);
                this.permission.get(permission).add(PERMISSION_DENY_MSG, denymsg);
            }
            this.permission.get(permission).remove(PERMISSION_EXIT_IF_DONTASK);
            this.permission.get(permission).add(PERMISSION_EXIT_IF_DONTASK, doExit ? "1" : "0");
            return true;
        }
        else return false;
    }

    private String getPermissionName(String permission) {
        if (this.permission.containsKey(permission))
        return this.permission.get(permission).get(PERMISSION_NAME);
        else return "";
    }

    private String getPermissionRequestCode(String permission) {
        if (this.permission.containsKey(permission))
        return this.permission.get(permission).get(PERMISSION_REQUEST_CODE);
        else return "";
    }

    private boolean getPermissionIsRequired(String permission) {
        if (this.permission.containsKey(permission))
            return this.permission.get(permission).get(PERMISSION_REQUIRED).equals("1");
        else return false;
    }

    private int getPermissionIconResource(String permission) {
        if (this.permission.containsKey(permission)) {
            String icon = this.permission.get(permission).get(PERMISSION_ICON);
            if (!icon.isEmpty()) {
                try {
                    return Integer.parseInt(icon);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
            else return 0;
        }
        else
            return 0;
    }

    private String getPermissionDontAskMsg(String permission) {
        if (this.permission.containsKey(permission)) {
            return this.permission.get(permission).get(PERMISSION_DONTASK_MSG);
        }
        else return "";
    }

    private String getPermissionDenyMsg(String permission) {
        if (this.permission.containsKey(permission))
            return this.permission.get(permission).get(PERMISSION_DENY_MSG);
        else return "";
    }

    private boolean getPermissionDoExit(String permission) {
        if (this.permission.containsKey(permission))
            return this.permission.get(permission).get(PERMISSION_EXIT_IF_DONTASK).equals("1");
        else return false;
    }

    public void askPermission(final List<String> permissions) {
        List<String> permission_needed = new ArrayList<String>();
        permissions_ask = new ArrayList<String>();
        for (int i = 0, count = permissions.size(); i < count ; i++) {
            if (hasPermission(permissions.get(i)) != PERMISSION_GRANT) {
                permission_needed.add(
                        getPermissionName(permissions.get(i))
                );
                permissions_ask.add(permissions.get(i));
            }
        }
        if (permissions.size() > 0) {
            if (permission_needed.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to ";
                final int permission_size = permissions_ask.size();
                List<Integer> icon_res = new ArrayList<Integer>();
                for (int i = 0; i < permission_size; i++) {
                    if (i == 0) message += permission_needed.get(i);
                    else if (i == permission_size - 1) message += " and " + permission_needed.get(i);
                    else message += ", " + permission_needed.get(i);
                    icon_res.add(getPermissionIconResource(permissions_ask.get(i)));
                }


                showMessageOKCancel(
                        "Grant Permission",
                        message,
                        "Grant",
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    if (!getPermissionRequestCode(permissions_ask.get(0)).isEmpty()) {
                                        requestPermission(permissions_ask.get(0), Integer.parseInt(getPermissionRequestCode(permissions_ask.get(0))));
                                    }
                            }
                        },
                        null,
                        icon_res
                );
            }
        }
    }
    private void requestPermission(String permission, int request_dode) {
        ActivityCompat.requestPermissions(activity, new String[] {permission}, request_dode);
    }
    public void onRequestPermissionResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        if (permissions.length > 0) {
            if (!getPermissionRequestCode(permissions[0]).isEmpty()) {
                if (requestCode == Integer.parseInt(getPermissionRequestCode(permissions[0]))) {
                    List<Integer> icon_res = new ArrayList<Integer>();
                    icon_res.add(getPermissionIconResource(permissions[0]));
                    if (hasPermission(permissions[0]) == PERMISSION_DENY) {
                        showMessageOKCancel("Grant Permission", getPermissionDenyMsg(permissions[0]),
                                "Allow",
                                "Deny",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int position = permissions_ask.indexOf(permissions[0]);
                                        if (!getPermissionRequestCode(permissions_ask.get(position)).isEmpty())
                                            requestPermission(permissions_ask.get(position), Integer.parseInt(getPermissionRequestCode(permissions_ask.get(position))));
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        askNextPermission(permissions);
                                    }
                                },
                                icon_res
                        );
                    }
                    else if (hasPermission(permissions[0]) == PERMISSION_DONT_ASK) {
//                        showMessageOKCancel("Grant Permission", "");
                        if (getPermissionIsRequired(permissions[0])) {
                            showMessageOKCancel("Grant Permission",
                                    getPermissionDontAskMsg(permissions[0]),
                                    "Allow",
                                    "Deny",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                            intent.setData(uri);
                                            activity.startActivityForResult(intent, REQUEST_REFRESH);
                                        }
                                    },
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (getPermissionDoExit(permissions[0]))
                                                activity.finish();
                                            else askNextPermission(permissions);
                                        }
                                    },
                                    icon_res
                            );
                        }
                        else
                            askNextPermission(permissions);
                    }
                    else if (hasPermission(permissions[0]) == PERMISSION_GRANT) {
                        askNextPermission(permissions);
                    }
                }
            }
        }
    }

    private void askNextPermission(String[] permissions) {
        int position = permissions_ask.indexOf(permissions[0]);
        if (position > -1 && position < permissions_ask.size() - 1) {
            if (!getPermissionRequestCode(permissions_ask.get(position +1)).isEmpty())
                requestPermission(permissions_ask.get(position + 1), Integer.parseInt(getPermissionRequestCode(permissions_ask.get(position+1))));
        }
    }

    /**
     * checks for the permission
     * @param permission the permission to be checked
     * @return the code for the permission {@link #PERMISSION_GRANT} OR {@link #PERMISSION_DENY} OR {@link #PERMISSION_DONT_ASK}
     */
    public int hasPermission(String permission) {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            // Check for Rationale Option
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!activity.shouldShowRequestPermissionRationale(permission)) {
                    if (getPermissionIsRequired(permission))
                        return PERMISSION_DONT_ASK;
                    else return PERMISSION_GRANT;
                }
                else return PERMISSION_DENY;
            }
        }
        return PERMISSION_GRANT;
    }

    private void showMessageOKCancel(String title, String message, String positivebutton,
                                     String negativebutton, DialogInterface.OnClickListener okListener,
                                     DialogInterface.OnClickListener cancelListener, List<Integer> icon_res) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.MyTheme)
                .setTitle(title)
//                .setMessage(message)
                .setView(activity.getLayoutInflater().inflate(R.layout.dialog_permission, null))
                .setPositiveButton(positivebutton, okListener)
                .setNegativeButton(negativebutton, cancelListener);
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
        LinearLayout ll_icon = (LinearLayout) dialog.findViewById(R.id.ll_icon);
        tv_msg.setText(message);
        if (null != icon_res) {
            LinearLayout ll_container = new LinearLayout(activity);
            ll_container.setOrientation(LinearLayout.HORIZONTAL);
            for (int i = 0, count = icon_res.size(); i < count ; i++) {
                if (icon_res.get(i) > 0) {
                    ImageView iv_icon = new ImageView(activity);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100, 1.0f);
                    params.setMargins(15, 15, 15, 15);
                    iv_icon.setLayoutParams(params);
                    iv_icon.setImageResource(icon_res.get(i));
                    ll_container.addView(iv_icon);
                }
            }
            ll_icon.addView(ll_container);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_REFRESH) {
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        }
    }
}
