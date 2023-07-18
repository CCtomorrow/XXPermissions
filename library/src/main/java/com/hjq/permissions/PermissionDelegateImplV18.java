package com.hjq.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/XXPermissions
 *    time   : 2023/03/11
 *    desc   : Android 4.3 权限委托实现
 */
class PermissionDelegateImplV18 extends PermissionDelegateImplV14 {

    @Override
    public boolean isGrantedPermission(Context context, String permission) {
        // 检测通知栏监听权限
        if (PermissionUtils.equalsPermission(permission, Permission.BIND_NOTIFICATION_LISTENER_SERVICE)) {
            return NotificationListenerPermissionCompat.isGrantedPermission(context);
        }
        return super.isGrantedPermission(context, permission);
    }

    @Override
    public boolean isPermissionPermanentDenied(Activity activity, String permission) {
        if (PermissionUtils.equalsPermission(permission, Permission.BIND_NOTIFICATION_LISTENER_SERVICE)) {
            return false;
        }
        return super.isPermissionPermanentDenied(activity, permission);
    }

    @Override
    public Intent getPermissionIntent(Context context, String permission) {
        if (PermissionUtils.equalsPermission(permission, Permission.BIND_NOTIFICATION_LISTENER_SERVICE)) {
            return NotificationListenerPermissionCompat.getPermissionIntent(context);
        }
        return super.getPermissionIntent(context, permission);
    }
}
