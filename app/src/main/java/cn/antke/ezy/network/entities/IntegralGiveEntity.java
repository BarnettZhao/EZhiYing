package cn.antke.ezy.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zww on 2017/6/19.
 */

public class IntegralGiveEntity implements Parcelable{


    @SerializedName("integral")
    private String integralNum;
    @SerializedName("start_date")
    private String startData;
    @SerializedName("end_date")
    private String endData;
    @SerializedName("remark")
    private String integralType;
    @SerializedName("is_doubly")//1 可用 2 不可用
    private String isUsable;
    @SerializedName("integral_id")
    private String giveId;

    protected IntegralGiveEntity(Parcel in) {
        integralNum = in.readString();
        startData = in.readString();
        endData = in.readString();
        integralType = in.readString();
        isUsable = in.readString();
        giveId = in.readString();
    }

    public static final Creator<IntegralGiveEntity> CREATOR = new Creator<IntegralGiveEntity>() {
        @Override
        public IntegralGiveEntity createFromParcel(Parcel in) {
            return new IntegralGiveEntity(in);
        }

        @Override
        public IntegralGiveEntity[] newArray(int size) {
            return new IntegralGiveEntity[size];
        }
    };

    public String getIntegralNum() {
        return integralNum;
    }

    public void setIntegralNum(String integralNum) {
        this.integralNum = integralNum;
    }

    public String getStartData() {
        return startData;
    }

    public void setStartData(String startData) {
        this.startData = startData;
    }

    public String getEndData() {
        return endData;
    }

    public void setEndData(String endData) {
        this.endData = endData;
    }

    public String getIntegralType() {
        return integralType;
    }

    public void setIntegralType(String integralType) {
        this.integralType = integralType;
    }

    public String getIsUsable() {
        return isUsable;
    }

    public void setIsUsable(String isUsable) {
        this.isUsable = isUsable;
    }

    public String getGiveId() {
        return giveId;
    }

    public void setGiveId(String giveId) {
        this.giveId = giveId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(integralNum);
        parcel.writeString(startData);
        parcel.writeString(endData);
        parcel.writeString(integralType);
        parcel.writeString(isUsable);
        parcel.writeString(giveId);
    }
}
