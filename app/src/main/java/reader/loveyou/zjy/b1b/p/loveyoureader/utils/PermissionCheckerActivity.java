package reader.loveyou.zjy.b1b.p.loveyoureader.utils;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/**
 Created by 张建宇 on 2019/1/22. */
public class PermissionCheckerActivity extends AppCompatActivity {

    private int ReqCode = 10086;

    private AllowListner listener;

    public interface AllowListner {
        void permissionResult(String[] permissions, boolean[] isAllow);
    }

    public void custReqPermission(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, ReqCode);
    }

    public boolean isPermitted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void custReqPermission(String[] permissions, AllowListner listner) {
        this.listener = listner;
        ActivityCompat.requestPermissions(this, permissions, ReqCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ReqCode == requestCode) {
            boolean[] isAllow = new boolean[permissions.length];
            for (int i = 0; i < grantResults.length; i++) {
                isAllow[i] = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
            listener.permissionResult(permissions, isAllow);
        }
    }

}
