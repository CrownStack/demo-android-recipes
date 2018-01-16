package geofenceex.com.mylibrary.dialoges;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import geofenceex.com.mylibrary.R;
import geofenceex.com.mylibrary.interfaces.IDialogCallback;

public class DialogManager {

    private static DialogManager sDialogManager;
    private Activity mActivity;
    private IDialogCallback iDialogCallback;

    public static DialogManager getDialogManager() {
        if (sDialogManager == null)
            sDialogManager = new DialogManager();
        return sDialogManager;
    }

    public void init(Activity activity, IDialogCallback iDialogCallback) {
        mActivity = activity;
        this.iDialogCallback = iDialogCallback;
    }

    public void showPermissionDialog(String title, String msg, String positiveBtnLabel, String negativeBtnLabel,
                                     boolean showTitle, boolean showMsg, boolean showNegativeBtn, boolean showPositiveBtn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        if (showMsg)
            builder.setMessage(msg);
        if (showTitle)
            builder.setTitle(title);
        if (showPositiveBtn)
            builder.setPositiveButton(positiveBtnLabel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    iDialogCallback.dialogOkClick();
                    dialog.dismiss();
                }
            });
        if (showNegativeBtn)
            builder.setNegativeButton(negativeBtnLabel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    iDialogCallback.dialogCancelClick();
                    dialog.dismiss();
                }
            });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showSettingDialog(String title, String msg, String positiveBtnLabel, String negativeBtnLabel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setPositiveButton(positiveBtnLabel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                iDialogCallback.dialogSettingOkClick();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(negativeBtnLabel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                iDialogCallback.dialogCancelClick();
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void gotoLocationSettings() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        dialog.setMessage(mActivity.getResources().getString(R.string.gps_network_not_enabled));
        dialog.setPositiveButton(mActivity.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mActivity.startActivity(myIntent);
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton(mActivity.getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int paramInt) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
