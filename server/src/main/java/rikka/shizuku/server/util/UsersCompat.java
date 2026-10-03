package rikka.shizuku.server.util;

import android.content.pm.UserInfo;
import android.os.IUserManager;
import android.os.ServiceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * hidden-compat's UserManagerApis calls IUserManager.getUsers(boolean, boolean, boolean) on API 30+
 * and silently falls back to user 0 when it is missing. Android 17 QPR1 on Pixel only has
 * getUsers(boolean excludeDying), so profiles such as Private Space were invisible to the server.
 */
public class UsersCompat {

    private static final String TAG = "UsersCompat";

    public static List<Integer> getUserIdsNoThrow() {
        List<Integer> ids = new ArrayList<>();
        try {
            IUserManager um = IUserManager.Stub.asInterface(ServiceManager.getService("user"));
            List<UserInfo> users;
            try {
                users = um.getUsers(true, true, true);
            } catch (NoSuchMethodError e) {
                // Excludes partial and pre-created users too, like the three-argument version.
                users = um.getUsers(true);
            }
            for (UserInfo user : users) {
                ids.add(user.id);
            }
        } catch (Throwable tr) {
            Log.w(TAG, "getUsers failed, assuming only user 0", tr);
        }
        if (ids.isEmpty()) {
            ids.add(0);
        }
        return ids;
    }
}
