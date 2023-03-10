package com.kpi.money.mony;

import android.os.Parcel;
import android.os.Parcelable;


public class BatteryInfo implements Parcelable {
    public static final String BATTERY_INFO_KEY = "battery_info_key";
    public static final Creator CREATOR = new Creator() {
        public BatteryInfo createFromParcel(Parcel parcel) {
            return new BatteryInfo(parcel);
        }

        public BatteryInfo[] newArray(int i) {
            return new BatteryInfo[i];
        }
    };
    public int health = -1;
    public int hourleft = 0;
    public int level = -1;
    public int minleft = 0;
    public int plugged = 0;
    public int scale = -1;
    public int status = -1;
    public String technology = "";
    public int temperature = -1;
    public int voltage = -1;

    public int describeContents() {
        return 0;
    }

    public BatteryInfo() {
    }

    public BatteryInfo(Parcel parcel) {
        this.level = parcel.readInt();
        this.scale = parcel.readInt();
        this.temperature = parcel.readInt();
        this.voltage = parcel.readInt();
        this.status = parcel.readInt();
        this.health = parcel.readInt();
        this.hourleft = parcel.readInt();
        this.minleft = parcel.readInt();
        this.plugged = parcel.readInt();
        this.technology = parcel.readString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(BatteryPref.EXTRA_LEVEL);
        sb.append(this.level);
        sb.append(" temperature");
        sb.append(this.temperature);
        sb.append(" voltage");
        sb.append(this.voltage);
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.level);
        parcel.writeInt(this.scale);
        parcel.writeInt(this.temperature);
        parcel.writeInt(this.voltage);
        parcel.writeInt(this.status);
        parcel.writeInt(this.health);
        parcel.writeInt(this.hourleft);
        parcel.writeInt(this.minleft);
        parcel.writeInt(this.plugged);
        parcel.writeString(this.technology);
    }
}
