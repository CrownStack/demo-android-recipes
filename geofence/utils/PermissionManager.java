package geofenceex.com.mylibrary.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

public abstract class PermissionManager extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 101;

    protected static void checkPermission(Activity activity, String... permissions) {
        if (!isPermissionGiven(activity, permissions))
            requestPermission(activity, permissions);
        else ((PermissionManager) activity).permissionAllowed();
    }

    private static boolean isPermissionGiven(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    protected static void requestPermission(Activity activity, String... permission) {
        ActivityCompat.requestPermissions(activity,
                permission,
                REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                            if (!showRationale)
                                permissionPermanentlyDenied();
                            else
                                permissionDenied();

                            return;
                        }
                    }
                    permissionAllowed();
                }
            }
        }
    }

    protected void goToSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent, null);
    }

    protected abstract void permissionDenied();
    protected abstract void permissionPermanentlyDenied();
    public abstract void permissionAllowed();
}
