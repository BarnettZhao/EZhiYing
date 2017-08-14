package cn.antke.ezy.network.entities;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import cn.antke.ezy.utils.Cn2Spell;

/**
 * Created by zhaoweiwei on 2017/5/16.
 * 好友
 */

public class ShareMemberEntity implements Comparable<ShareMemberEntity> {
    @SerializedName("user_id")
    private String userID;
    @SerializedName("user_code")
    private String userCode;
    @SerializedName("user_name")
    private String name;
    @SerializedName("head_pic")
    private String headPic;
    @SerializedName("friend_count")
    private String friendCount;
    @SerializedName("remuneration")
    private String remuneration;//隔代取筹

    private String firstLetter;
    private String pinyin;//姓名对应的拼音

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLetter() {
        pinyin = Cn2Spell.getPinYin(name); // 根据姓名获取拼音
        firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
        if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            firstLetter = "#";
        }
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(String friendCount) {
        this.friendCount = friendCount;
    }

    public String getRemuneration() {
        return remuneration;
    }

    public void setRemuneration(String remuneration) {
        this.remuneration = remuneration;
    }

    @Override
    public int compareTo(@NonNull ShareMemberEntity o) {
        if (firstLetter.equals("#") && !o.getFirstLetter().equals("#")) {
            return 1;
        } else if (!firstLetter.equals("#") && o.getFirstLetter().equals("#")) {
            return -1;
        } else {
            return pinyin.compareToIgnoreCase(o.getPinyin());
        }
    }
}
