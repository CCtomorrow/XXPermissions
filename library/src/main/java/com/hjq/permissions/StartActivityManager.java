package com.hjq.permissions;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/XXPermissions
 *    time   : 2023/04/05
 *    desc   : startActivity 管理器
 */
final class StartActivityManager {

   private static final String SUB_INTENT_KEY = "sub_intent_key";

   static Intent getSubIntentInMainIntent(Intent mainIntent) {
      Intent subIntent;
      if (AndroidVersion.isAndroid13()) {
         subIntent = mainIntent.getParcelableExtra(SUB_INTENT_KEY, Intent.class);
      } else {
         subIntent = mainIntent.getParcelableExtra(SUB_INTENT_KEY);
      }
      return subIntent;
   }

   static Intent getDeepSubIntent(Intent superIntent) {
      Intent subIntent = getSubIntentInMainIntent(superIntent);
      if (subIntent != null) {
         return getDeepSubIntent(subIntent);
      }
      return superIntent;
   }

   static Intent addSubIntentToMainIntent(Intent mainIntent, Intent subIntent) {
      if (mainIntent == null && subIntent != null) {
         return subIntent;
      }
      if (subIntent == null) {
         return mainIntent;
      }
      Intent deepSubIntent = getDeepSubIntent(mainIntent);
      deepSubIntent.putExtra(SUB_INTENT_KEY, subIntent);
      return mainIntent;
   }

   static boolean startActivity(Context context, Intent intent) {
      return startActivity(new StartActivityDelegateContextImpl(context), intent);
   }

   static boolean startActivity(Activity activity, Intent intent) {
      return startActivity(new StartActivityDelegateActivityImpl(activity), intent);
   }

   static boolean startActivity(Fragment fragment, Intent intent) {
      return startActivity(new StartActivityDelegateFragmentImpl(fragment), intent);
   }

   static boolean startActivity(android.support.v4.app.Fragment fragment, Intent intent) {
      return startActivity(new StartActivityDelegateSupportFragmentImpl(fragment), intent);
   }

   static boolean startActivity(IStartActivityDelegate delegate, Intent intent) {
      try {
         delegate.startActivity(intent);
         return true;
      } catch (Exception e) {
         e.printStackTrace();
         Intent subIntent = getSubIntentInMainIntent(intent);
         if (subIntent == null) {
            return false;
         }
         return startActivity(delegate, subIntent);
      }
   }

   static boolean startActivityForResult(Activity activity, Intent intent, int requestCode) {
      return startActivityForResult(new StartActivityDelegateActivityImpl(activity), intent, requestCode);
   }

   static boolean startActivityForResult(Fragment fragment, Intent intent, int requestCode) {
      return startActivityForResult(new StartActivityDelegateFragmentImpl(fragment), intent, requestCode);
   }

   static boolean startActivityForResult(android.support.v4.app.Fragment fragment, Intent intent, int requestCode) {
      return startActivityForResult(new StartActivityDelegateSupportFragmentImpl(fragment), intent, requestCode);
   }

   static boolean startActivityForResult(IStartActivityDelegate delegate, Intent intent, int requestCode) {
      try {
         delegate.startActivityForResult(intent, requestCode);
         return true;
      } catch (Exception e) {
         e.printStackTrace();
         Intent subIntent = getSubIntentInMainIntent(intent);
         if (subIntent == null) {
            return false;
         }
         return startActivityForResult(delegate, subIntent, requestCode);
      }
   }

   private interface IStartActivityDelegate {

      void startActivity(Intent intent);

      void startActivityForResult(Intent intent, int requestCode);
   }

   private static class StartActivityDelegateContextImpl implements IStartActivityDelegate {

      private final Context mContext;

      private StartActivityDelegateContextImpl(Context context) {
         mContext = context;
      }

      @Override
      public void startActivity(Intent intent) {
         mContext.startActivity(intent);
      }

      @Override
      public void startActivityForResult(Intent intent, int requestCode) {
         Activity activity = PermissionUtils.findActivity(mContext);
         if (activity != null) {
            activity.startActivityForResult(intent, requestCode);
            return;
         }
         startActivity(intent);
      }
   }

   private static class StartActivityDelegateActivityImpl implements IStartActivityDelegate {

      private final Activity mActivity;

      private StartActivityDelegateActivityImpl(Activity activity) {
         mActivity = activity;
      }

      @Override
      public void startActivity(Intent intent) {
         mActivity.startActivity(intent);
      }

      @Override
      public void startActivityForResult(Intent intent, int requestCode) {
         mActivity.startActivityForResult(intent, requestCode);
      }
   }

   private static class StartActivityDelegateFragmentImpl implements IStartActivityDelegate {

      private final Fragment mFragment;

      private StartActivityDelegateFragmentImpl(Fragment fragment) {
         mFragment = fragment;
      }

      @Override
      public void startActivity(Intent intent) {
         mFragment.startActivity(intent);
      }

      @Override
      public void startActivityForResult(Intent intent, int requestCode) {
         mFragment.startActivityForResult(intent, requestCode);
      }
   }

   private static class StartActivityDelegateSupportFragmentImpl implements IStartActivityDelegate {

      private final android.support.v4.app.Fragment mFragment;

      private StartActivityDelegateSupportFragmentImpl(android.support.v4.app.Fragment fragment) {
         mFragment = fragment;
      }

      @Override
      public void startActivity(Intent intent) {
         mFragment.startActivity(intent);
      }

      @Override
      public void startActivityForResult(Intent intent, int requestCode) {
         mFragment.startActivityForResult(intent, requestCode);
      }
   }
}