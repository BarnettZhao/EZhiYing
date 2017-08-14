package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuzhichao on 2017/5/10.
 * 地区或语言
 */
public class AreaEntity {

	@SerializedName("site_id")
	private String id;
	@SerializedName("site_name")
	private String name;
	private String languageCode;
	private boolean isSelected;

	public AreaEntity(String name, String languageCode) {
		this.name = name;
		this.languageCode = languageCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}
}
