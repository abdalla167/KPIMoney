package com.kpi.money.mony;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;


import com.kpi.money.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Utils {

    private static final String MARKET_DETAILS_ID = "market://details?id=";
    private static final String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=";
    private static final String text = "Check out " + ", the free app for Create Sticker Whatsapp  " ;
    private static final String POLICY = "https://policy.com";
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    public static String mail;
    public static String appId;


    private Utils() {
    }

    public static boolean isUserApp(ApplicationInfo applicationInfo) {
        return (applicationInfo.flags & 129) == 0;
    }


    public static String convertStorage(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;





        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }


    public static void goPolicy(Context context, String appId) {
        Utils.context = context;
        Utils.appId = appId;
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(POLICY)));

        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(POLICY)));
        }
    }
    public static void geShareApp(Context context, String appid){

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,PLAY_STORE_LINK+appid+text );
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public static void goRateApp(Context context, String appId) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_DETAILS_ID + appId)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_LINK + appId)));
        }
    }



    public static void goUpdate(Context context, String appId) {
        PackageManager manager2 = context.getApplicationContext()
                .getPackageManager();



        try {

            PackageInfo info = manager2.getPackageInfo(context.getPackageName(), 0);
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_DETAILS_ID + appId)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_LINK + appId)));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void  getFeedback(Context context, String mail){
        Utils.context = context;
        Utils.mail = mail;
        Intent mIntent;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        PackageManager manager = context.getApplicationContext()
                .getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assert info != null;
        String version = info.versionName;

        mIntent = new Intent(Intent.ACTION_SEND);
        mIntent.setType("message/rfc822");
        mIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.developer_email_feedback)});
        mIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name) + "Version =" + version);
        mIntent.putExtra(Intent.EXTRA_TEXT,
                "\n" + " Device :" + getDeviceName() +
                        "\n" + " SystemVersion:" + Build.VERSION.SDK_INT +
                        "\n" + " Display Height  :" + height + "px" +
                        "\n" + " Display Width  :" + width + "px" +
                        "\n\n" + " Please write your problem to us we will try our best to solve it .." +
                        "\n");

        context.startActivity(Intent.createChooser(mIntent, "Send Email"));

    }

    private static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }
        return phrase.toString();
    }

    public static int dip2px(Context context, float dpValue) {
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) dpValue;
    }

    public static Point getDisplayMetrics(Activity activity) {
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= 18) {
            activity.getWindowManager().getDefaultDisplay().getRealSize(point);
        } else {
            activity.getWindowManager().getDefaultDisplay().getSize(point);
        }
        return point;
    }









    @SuppressLint("WrongConstant")
    public static void ShowToastShort(Context context, String str) {
        Toast.makeText(context, str, 0).show();
    }

    @SuppressLint("WrongConstant")
    public static void ShowToastLong(Context context, String str) {
        Toast.makeText(context, str, 1).show();
    }





    public static void RateApp(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://play.google.com/store/apps/details?id=");
        sb.append(context.getPackageName());
        context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
    }

    public static void SendFeedBack(Context context, String str, String str2) {
        String[] strArr = {str};
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setData(Uri.parse("mailto:"));
        intent.setType("message/rfc822");
        intent.putExtra("android.intent.extra.EMAIL", strArr);
        intent.putExtra("android.intent.extra.SUBJECT", str2);
        intent.putExtra("android.intent.extra.TEXT", "Enter your FeedBack");
        try {
            context.startActivity(Intent.createChooser(intent, "Send FeedBack..."));
        } catch (ActivityNotFoundException unused) {
            ShowToastShort(context, "There is no email client installed.");
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        @SuppressLint("WrongConstant") ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected() || !activeNetworkInfo.isConnectedOrConnecting() || !activeNetworkInfo.isAvailable()) {
            return false;
        }
        return true;
    }



    public static boolean isMyServiceRunning(Class<?> cls, Context context) {
        String str;
        @SuppressLint("WrongConstant") Iterator it = ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE).iterator();
        do {
            str = "isMyServiceRunning?";
            if (it.hasNext()) {
            } else {
                Log.i(str, "false");
                return false;
            }
        } while (!cls.getName().equals(((ActivityManager.RunningServiceInfo) it.next()).service.getClassName()));
        Log.i(str, "true");
        return true;
    }

}
