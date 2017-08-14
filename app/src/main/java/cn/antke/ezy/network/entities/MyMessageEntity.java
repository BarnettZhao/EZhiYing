package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2017/5/16.
 */

public class MyMessageEntity {
    @SerializedName("type")
    private String messType;
    @SerializedName("message_content")
    private String messageContent;
    @SerializedName("message_id")
    private String messId;
    @SerializedName("message_title")
    private String messTitle;
    @SerializedName("message_url")
    private String messUrl;
    @SerializedName("update_date")
    private String messData;

    //1：系统消息 2: 交易大厅续费 3: 消费服务中心续费 4：赠送审批消息(审核人); 5,赠送审批结果消息(申请人) 6: 积分互转 7: 商品审核 8: 支付成功 9: 退款消息
    public String getMessType() {
        return messType;
    }

    public void setMessType(String messType) {
        this.messType = messType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessId() {
        return messId;
    }

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public String getMessTitle() {
        return messTitle;
    }

    public void setMessTitle(String messTitle) {
        this.messTitle = messTitle;
    }

    public String getMessUrl() {
        return messUrl;
    }

    public void setMessUrl(String messUrl) {
        this.messUrl = messUrl;
    }

    public String getMessData() {
        return messData;
    }

    public void setMessData(String messData) {
        this.messData = messData;
    }
}
