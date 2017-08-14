package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zww on 2017/6/17.
 */

public class ConsumerBindQueryEntity {
    @SerializedName("service_status")
    private String serviceType;
    @SerializedName("service_code")
    private String userCode;
    @SerializedName("contact")
    private String userPhone;
    @SerializedName("service_name")
    private String userName;

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
