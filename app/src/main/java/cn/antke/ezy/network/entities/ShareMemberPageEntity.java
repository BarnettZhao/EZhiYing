package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zww on 2017/6/17.
 */

public class ShareMemberPageEntity {
    @SerializedName("total_page")
    private int totalPage;
    @SerializedName("user_list")
    private List<ShareMemberEntity> memberEntities;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<ShareMemberEntity> getMemberEntities() {
        return memberEntities;
    }

    public void setMemberEntities(List<ShareMemberEntity> memberEntities) {
        this.memberEntities = memberEntities;
    }
}
