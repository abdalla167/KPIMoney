package com.kpi.money.mony;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.PowerManager;
import android.util.Log;

import com.facebook.appevents.ml.Utils;


public class BatteryPref {
    public static final String BATTERY_PREF;
    public static final String EXTRA_CURENT_TIME1 = "curenttime1";
    public static final String EXTRA_CURENT_TIME2 = "curenttime2";
    public static final String EXTRA_CURENT_TIME3 = "curenttime3";
    public static final String EXTRA_CURENT_TIME4 = "curenttime4";
    public static final String EXTRA_CURENT_TIME5 = "curenttime5";
    public static final String EXTRA_CURENT_TIME_CHARGE_AC = "time_charge_ac";
    public static final String EXTRA_CURENT_TIME_CHARGE_USB = "time_charge_USB";
    public static final String EXTRA_LEVEL = "level";
    public static final String EXTRA_TIME_CHARGING_AC = "timecharging_ac";
    public static final String EXTRA_TIME_CHARGING_USB = "timecharging_usb";
    public static final String EXTRA_TIME_REMAIN = "timeremainning";
    public static final String TIMEMAIN1 = "timemain1";
    public static final String TIMEMAIN2 = "timemain2";
    public static final String TIMEMAIN3 = "timemain3";
    public static final String TIMEMAIN4 = "timemain4";
    public static final String TIMEMAIN5 = "timemain5";
    public static final String TIMESCREENOFF = "time_Screen_on";
    public static final String TIMESCREENON = "time_Screen_on";
    public static final long TIME_CHARGING_AC_DEFAULT = 216000;
    public static final long TIME_CHARGING_AC_MAX = 108000;
    public static final long TIME_CHARGING_AC_MIN = 36000;
    public static final long TIME_CHARGING_USB_DEFAULT = 144000;
    public static final long TIME_CHARGING_USB_MAX = 180000;
    public static final long TIME_CHARGING_USB_MIN = 108000;
    public static long TIME_REMAIN_DEFAULT = 864000;
    public static final long TIME_REMAIN_MAX = 2160000;
    public static final long TIME_REMAIN_MIN = 720000;
    private static BatteryPref batteryPref;
    static Context mContext;

    static {
        StringBuilder sb = new StringBuilder();
        String str = "battery_info";
        sb.append(str);
        sb.append(str.hashCode());
        BATTERY_PREF = sb.toString();
    }

    private BatteryPref(Context context) {
        Log.e("TIME_REMAIN_DEFAULT", String.valueOf(TIME_REMAIN_DEFAULT));
        long round = (Math.round(getBatteryCapacity(context)) * 24) / 3;
        TIME_REMAIN_DEFAULT = (TIME_REMAIN_DEFAULT * Math.round(getBatteryCapacity(context))) / 3200;
        SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, 0);
        Editor edit = sharedPreferences.edit();
        long j = TIME_REMAIN_DEFAULT;
        String str = EXTRA_TIME_REMAIN;
        edit.putLong(str, j).commit();
        boolean contains = sharedPreferences.contains(str);
        String str2 = EXTRA_TIME_CHARGING_USB;
        String str3 = EXTRA_TIME_CHARGING_AC;
        if (!contains || !sharedPreferences.contains(EXTRA_CURENT_TIME1) || !sharedPreferences.contains(str3) || !sharedPreferences.contains(str2)) {
            sharedPreferences.edit().putLong(str3, TIME_CHARGING_AC_DEFAULT).commit();
            sharedPreferences.edit().putLong(str2, TIME_CHARGING_USB_DEFAULT).commit();
        }
    }

    public static double getBatteryCapacity(Context context) {
        Object obj;
        String str = "com.android.internal.os.PowerProfile";
        try {
            obj = Class.forName(str).getConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
        } catch (Exception e) {
            e.printStackTrace();
            obj = null;
        }
        try {
            return ((Double) Class.forName(str).getMethod("getAveragePower", new Class[]{String.class}).invoke(obj, new Object[]{"battery.capacity"})).doubleValue();
        } catch (Exception e2) {
            e2.printStackTrace();
            return 0.0;
        }
    }

    public static BatteryPref initilaze(Context context) {
        mContext = context;
        if (batteryPref == null) {
            batteryPref = new BatteryPref(mContext);
        }
        return batteryPref;
    }

    public void putLevel(Context context, int i) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, 0);
        if (i != getLevel(context)) {
            sharedPreferences.edit().putInt(EXTRA_LEVEL, i).commit();
        }
    }

    public void setScreenOn() {
        mContext.getSharedPreferences(BATTERY_PREF, 0).edit().putLong("time_Screen_on", System.currentTimeMillis()).commit();
    }

    public void setScreenOff() {
        mContext.getSharedPreferences(BATTERY_PREF, 0).edit().putLong("time_Screen_on", System.currentTimeMillis()).commit();
    }

    @SuppressLint("WrongConstant")
    public int getTimeRemainning(Context context, int i) {
        int i2;
        long j;
        Context context2 = context;
        int i3 = i;
        SharedPreferences sharedPreferences = context2.getSharedPreferences(BATTERY_PREF, 0);
        ((PowerManager) context2.getSystemService("power")).isScreenOn();
        long j2 = (long) i3;
        long j3 = TIME_REMAIN_DEFAULT;
        String str = EXTRA_TIME_REMAIN;
        int i4 = (int) ((j2 * sharedPreferences.getLong(str, j3)) / 60000);
        if (getLevel(context) == -1) {
            putLevel(context, i);
        } else if (i3 < getLevel(context)) {
            sharedPreferences.edit().putInt(EXTRA_LEVEL, i3).commit();
            String str2 = EXTRA_CURENT_TIME1;
            long j4 = sharedPreferences.getLong(str2, 0);
            String str3 = EXTRA_CURENT_TIME5;
            String str4 = TIMEMAIN4;
            String str5 = TIMEMAIN3;
            String str6 = TIMEMAIN2;
            String str7 = TIMEMAIN1;
            String str8 = EXTRA_CURENT_TIME2;
            String str9 = EXTRA_CURENT_TIME3;
            String str10 = " giay";
            i2 = i4;
            String str11 = "ALO ";
            if (j4 == 0 || sharedPreferences.getLong(str8, 0) == 0 || sharedPreferences.getLong(str9, 0) == 0) {
                String str12 = str4;
                String str13 = str6;
                String str14 = str7;
                String str15 = str9;
                if (sharedPreferences.getLong(str2, 0) == 0) {
                    sharedPreferences.edit().putLong(str2, System.currentTimeMillis()).commit();
                    return i2;
                }
                String str16 = "timeRemain1111 ";
                if (sharedPreferences.getLong(str8, 0) == 0) {
                    sharedPreferences.edit().putLong(str14, System.currentTimeMillis() - sharedPreferences.getLong(str2, System.currentTimeMillis())).commit();
                    sharedPreferences.edit().putLong(str8, System.currentTimeMillis()).commit();
                    StringBuilder sb = new StringBuilder();
                    sb.append(str16);
                    sb.append(String.valueOf((System.currentTimeMillis() - sharedPreferences.getLong(str2, System.currentTimeMillis())) / 1000));
                    sb.append(str10);
                    Log.e(str11, sb.toString());
                    return i2;
                } else if (sharedPreferences.getLong(str15, 0) == 0) {
                    sharedPreferences.edit().putLong(str13, System.currentTimeMillis() - sharedPreferences.getLong(str2, System.currentTimeMillis())).commit();
                    sharedPreferences.edit().putLong(str15, System.currentTimeMillis()).commit();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str16);
                    sb2.append(String.valueOf((System.currentTimeMillis() - sharedPreferences.getLong(str2, System.currentTimeMillis())) / 1000));
                    sb2.append(str10);
                    Log.e(str11, sb2.toString());
                    return i2;
                } else {
                    String str17 = EXTRA_CURENT_TIME4;
                    if (sharedPreferences.getLong(str17, 0) == 0) {
                        sharedPreferences.edit().putLong(str5, System.currentTimeMillis() - sharedPreferences.getLong(str2, System.currentTimeMillis())).commit();
                        sharedPreferences.edit().putLong(str17, System.currentTimeMillis()).commit();
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(str16);
                        sb3.append(String.valueOf((System.currentTimeMillis() - sharedPreferences.getLong(str2, System.currentTimeMillis())) / 1000));
                        sb3.append(str10);
                        Log.e(str11, sb3.toString());
                        return i2;
                    }
                    if (sharedPreferences.getLong(str3, 0) == 0) {
                        sharedPreferences.edit().putLong(str12, System.currentTimeMillis() - sharedPreferences.getLong(str2, System.currentTimeMillis())).commit();
                        sharedPreferences.edit().putLong(str3, System.currentTimeMillis()).commit();
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(str16);
                        sb4.append(String.valueOf((System.currentTimeMillis() - sharedPreferences.getLong(str2, System.currentTimeMillis())) / 1000));
                        sb4.append(str10);
                        Log.e(str11, sb4.toString());
                    }
                    return i2;
                }
            } else {
                long j5 = sharedPreferences.getLong(str7, 0);
                String str18 = str9;
                long j6 = sharedPreferences.getLong(str6, 0);
                String str19 = str6;
                String str20 = str7;
                long j7 = sharedPreferences.getLong(str5, 0);
                long j8 = sharedPreferences.getLong(str4, 0);
                String str21 = str4;
                String str22 = str11;
                long currentTimeMillis = System.currentTimeMillis() - sharedPreferences.getLong(str3, System.currentTimeMillis());
                long j9 = (((((j5 + j6) + j7) + j8) + currentTimeMillis) + sharedPreferences.getLong(str, TIME_REMAIN_DEFAULT)) / 6;
                if (j9 > TIME_REMAIN_MIN) {
                    if (j9 < TIME_REMAIN_MAX) {
                        sharedPreferences.edit().putLong(str, j9).commit();
                    } else {
                        j = j8;
                        sharedPreferences.edit().putLong(str, j9 / 2).commit();
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("timeRemain1 ");
                        sb5.append(String.valueOf(j5 / 1000));
                        sb5.append(str10);
                        String str23 = str22;
                        Log.e(str23, sb5.toString());
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("timeRemain2 ");
                        sb6.append(String.valueOf(j6 / 1000));
                        sb6.append(str10);
                        Log.e(str23, sb6.toString());
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("timeRemain3 ");
                        sb7.append(String.valueOf(j7 / 1000));
                        sb7.append(str10);
                        Log.e(str23, sb7.toString());
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("timeRemain4 ");
                        sb8.append(String.valueOf(j / 1000));
                        sb8.append(str10);
                        Log.e(str23, sb8.toString());
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("timeRemain5 ");
                        sb9.append(String.valueOf(currentTimeMillis / 1000));
                        sb9.append(str10);
                        Log.e(str23, sb9.toString());
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append("timeRemain main  ");
                        sb10.append(String.valueOf(j9 / 1000));
                        sb10.append(str10);
                        Log.e(str23, sb10.toString());
                        sharedPreferences.edit().putLong(str20, j5).commit();
                        sharedPreferences.edit().putLong(str19, j6).commit();
                        sharedPreferences.edit().putLong(str5, j7).commit();
                        sharedPreferences.edit().putLong(str21, j).commit();
                        sharedPreferences.edit().putLong(str18, System.currentTimeMillis()).commit();
                        return i2;
                    }
                }
                j = j8;
                StringBuilder sb52 = new StringBuilder();
                sb52.append("timeRemain1 ");
                sb52.append(String.valueOf(j5 / 1000));
                sb52.append(str10);
                String str232 = str22;
                Log.e(str232, sb52.toString());
                StringBuilder sb62 = new StringBuilder();
                sb62.append("timeRemain2 ");
                sb62.append(String.valueOf(j6 / 1000));
                sb62.append(str10);
                Log.e(str232, sb62.toString());
                StringBuilder sb72 = new StringBuilder();
                sb72.append("timeRemain3 ");
                sb72.append(String.valueOf(j7 / 1000));
                sb72.append(str10);
                Log.e(str232, sb72.toString());
                StringBuilder sb82 = new StringBuilder();
                sb82.append("timeRemain4 ");
                sb82.append(String.valueOf(j / 1000));
                sb82.append(str10);
                Log.e(str232, sb82.toString());
                StringBuilder sb92 = new StringBuilder();
                sb92.append("timeRemain5 ");
                sb92.append(String.valueOf(currentTimeMillis / 1000));
                sb92.append(str10);
                Log.e(str232, sb92.toString());
                StringBuilder sb102 = new StringBuilder();
                sb102.append("timeRemain main  ");
                sb102.append(String.valueOf(j9 / 1000));
                sb102.append(str10);
                Log.e(str232, sb102.toString());
                sharedPreferences.edit().putLong(str20, j5).commit();
                sharedPreferences.edit().putLong(str19, j6).commit();
                sharedPreferences.edit().putLong(str5, j7).commit();
                sharedPreferences.edit().putLong(str21, j).commit();
                sharedPreferences.edit().putLong(str18, System.currentTimeMillis()).commit();
                return i2;
            }
        }
        i2 = i4;
        return i2;
    }

    @SuppressLint("LongLogTag")
    public int getTimeChargingUsb(Context context, int i) {
        Log.e("TIME_REMAIN_DEFAULT getBatteryCapacity(context))", String.valueOf(TIME_REMAIN_DEFAULT));
        SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, 0);
        long j = (long) (100 - i);
        String str = EXTRA_TIME_CHARGING_USB;
        int i2 = (int) ((j * sharedPreferences.getLong(str, TIME_CHARGING_USB_DEFAULT)) / 60000);
        if (i > getLevel(context)) {
            sharedPreferences.edit().putInt(EXTRA_LEVEL, i).commit();
            long currentTimeMillis = System.currentTimeMillis() - sharedPreferences.getLong(EXTRA_CURENT_TIME_CHARGE_USB, System.currentTimeMillis());
            if (currentTimeMillis < TIME_CHARGING_USB_MAX && currentTimeMillis > 108000) {
                sharedPreferences.edit().putLong(str, currentTimeMillis).commit();
            }
        }
        return i2;
    }

    public int getTimeChargingAc(Context context, int i) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(BATTERY_PREF, 0);
        long j = (long) (100 - i);
        String str = EXTRA_TIME_CHARGING_AC;
        int i2 = (int) ((j * sharedPreferences.getLong(str, TIME_CHARGING_AC_DEFAULT)) / 60000);
        if (i > getLevel(context)) {
            sharedPreferences.edit().putInt(EXTRA_LEVEL, i).commit();
            long currentTimeMillis = System.currentTimeMillis() - sharedPreferences.getLong(EXTRA_CURENT_TIME_CHARGE_AC, System.currentTimeMillis());
            if (currentTimeMillis < 108000 && currentTimeMillis > TIME_CHARGING_AC_MIN) {
                sharedPreferences.edit().putLong(str, currentTimeMillis).commit();
            }
        }
        return i2;
    }

    public int getLevel(Context context) {
        return context.getSharedPreferences(BATTERY_PREF, 0).getInt(EXTRA_LEVEL, -1);
    }
}
