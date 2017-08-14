package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuzhichao on 2017/5/15.
 * 中奖名单
 */
public class DrawQueryEntity {

	@SerializedName("prize_num")
	private String no;
	@SerializedName("user_name")
	private String userNo;
	@SerializedName("goods_name")
	private String prizeName;

	public DrawQueryEntity(String no, String userNo, String prizeName) {
		this.no = no;
		this.userNo = userNo;
		this.prizeName = prizeName;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}
}
