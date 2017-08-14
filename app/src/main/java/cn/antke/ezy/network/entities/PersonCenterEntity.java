package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2017/5/9.
 * 个人中心item数量
 */

public class PersonCenterEntity {

    @SerializedName("deliver_goods")
    private String delivingNum;//待发货
    @SerializedName("friends_num")
    private String friendNum;//好友数
    @SerializedName("integral")
    private String integral;//积分数
    @SerializedName("news_num")
    private String messageNum;//未读消息
    @SerializedName("pending_payment")
    private String payingNum;//待付款数
    @SerializedName("take_delivery")
    private String delivedNum;//待收货数
    @SerializedName("refund_num")
    private String refundNum;//退款数
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("user_type")
    private String userType;//用户类型：1，未激活用户；2，激活用户；3，报单中心(用户服务中心)；4，分公司
	@SerializedName("store_status")
	private String storeStatus;//0 待审核 1 正常[审核成功] 2 锁定 3 拒绝审核
	@SerializedName("store_name")
	private String storeName;
	@SerializedName("store_pic")
	private String storePic;

    public String getDelivingNum() {
        return delivingNum;
    }

    public void setDelivingNum(String delivingNum) {
        this.delivingNum = delivingNum;
    }

    public String getFriendNum() {
        return friendNum;
    }

    public void setFriendNum(String friendNum) {
        this.friendNum = friendNum;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(String messageNum) {
        this.messageNum = messageNum;
    }

    public String getPayingNum() {
        return payingNum;
    }

    public void setPayingNum(String payingNum) {
        this.payingNum = payingNum;
    }

    public String getDelivedNum() {
        return delivedNum;
    }

    public void setDelivedNum(String delivedNum) {
        this.delivedNum = delivedNum;
    }

    public String getRefundNum() {
        return refundNum;
    }

    public void setRefundNum(String refundNum) {
        this.refundNum = refundNum;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

	public String getStoreStatus() {
		return storeStatus;
	}

	public void setStoreStatus(String storeStatus) {
		this.storeStatus = storeStatus;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStorePic() {
		return storePic;
	}

	public void setStorePic(String storePic) {
		this.storePic = storePic;
	}
}
