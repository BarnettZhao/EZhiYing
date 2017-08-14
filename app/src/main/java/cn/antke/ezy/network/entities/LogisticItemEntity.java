package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2017/5/23.
 */

public class LogisticItemEntity {
    @SerializedName("context")
    private String describe;
    @SerializedName("time")
    private String time;

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
