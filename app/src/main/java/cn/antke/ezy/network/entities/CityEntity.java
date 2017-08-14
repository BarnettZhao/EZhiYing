package cn.antke.ezy.network.entities;

/**
 * Created by liuzhichao on 2017/2/23.
 * 省市区
 */
public class CityEntity {

	private String no;
	private String name;

	public CityEntity(String no, String name) {
		this.no = no;
		this.name = name;
	}

	public String getNo() {
		return no;
	}

	public String getName() {
		return name;
	}
}
