package cn.antke.ezy.network.entities;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Liu_ZhiChao on 2015/9/18 18:12.
 * 第三方分享
 */
public class ShareEntity {

	@SerializedName("title")
	private String title;
	@SerializedName("describe")
	private String desc;
	@SerializedName("turn_url")
	private String url;
	@SerializedName("logo_pic_url")
	private String img;

	private String name;
	private transient Drawable logo;//防止序列化操作，否则gson解析会有异常

	public ShareEntity(String name, Drawable logo) {
		this.name = name;
		this.logo = logo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Drawable getLogo() {
		return logo;
	}

	public void setLogo(Drawable logo) {
		this.logo = logo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
}
