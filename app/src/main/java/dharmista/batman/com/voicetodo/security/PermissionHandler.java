package dharmista.batman.com.voicetodo.security;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Dharmista on 7/18/2017.
 */

public class PermissionHandler {

    public List<String> askPermissions(Context context, Activity activity, String[] permission) {
        List<String> notAcceptedPermissions = new ArrayList<>();

        for(String permission_ : permission) {
            if (ContextCompat.checkSelfPermission(context, permission_)
                    != PackageManager.PERMISSION_GRANTED) {
                notAcceptedPermissions.add(permission_);
            }
        }

        return notAcceptedPermissions;
    }

}
