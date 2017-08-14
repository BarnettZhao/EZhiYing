package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zww on 2017/6/17.
 */

public class ConsumerQueryEntity {
    @SerializedName("freeze_integral")
    private String freezeIntegral;//待用积分
    @SerializedName("integral_standard")
    private String integralStd;//待用积分标准
    @SerializedName("mem_integral_std")
    private String memIntegralStd;//直推会员积分标准
    @SerializedName("member")
    private String member;//直推会员
    @SerializedName("member_standard")
    private String memberStandard;//直推会员标准
    @SerializedName("recharge_amount")
    private String rechargeAmount;//充值金额


    public String getFreezeIntegral() {
        return freezeIntegral;
    }

    public void setFreezeIntegral(String freezeIntegral) {
        this.freezeIntegral = freezeIntegral;
    }

    public String getIntegralStd() {
        return integralStd;
    }

    public void setIntegralStd(String integralStd) {
        this.integralStd = integralStd;
    }

    public String getMemIntegralStd() {
        return memIntegralStd;
    }

    public void setMemIntegralStd(String memIntegralStd) {
        this.memIntegralStd = memIntegralStd;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getMemberStandard() {
        return memberStandard;
    }

    public void setMemberStandard(String memberStandard) {
        this.memberStandard = memberStandard;
    }

    public String getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }
}
