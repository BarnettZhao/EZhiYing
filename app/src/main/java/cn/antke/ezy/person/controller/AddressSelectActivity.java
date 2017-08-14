package cn.antke.ezy.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.common.utils.StringUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.db.CityDBManager;
import cn.antke.ezy.network.entities.CityEntity;
import cn.antke.ezy.person.adapter.CityListAdapter;
import cn.antke.ezy.utils.ViewInjectUtils;

/**
 * Created by Liu_ZhiChao on 2015/9/26 12:37.
 * 选择城市
 */
public class AddressSelectActivity extends ToolBarActivity implements AdapterView.OnItemClickListener {

	private static final String GET_PROVINCE_CODE = "province";
	private static final String GET_CITY_CODE = "city";
	private static final String GET_DISTRICT_CODE = "district";
	private static final String PROVINCE_ID = "none";
	public static final String SELECT_CITY = "city_name";

	@ViewInject(R.id.city_current_city)
	private TextView cityCurrentCity;
	@ViewInject(R.id.city_name_list)
	private ListView cityNameList;

	private List<CityEntity> cityList;
	private String provinceId;
	private String cityId;
	private String districtId;
	private CityListAdapter cityAdapter;
	private String province;
	private String city;
	private String district;
	private String type = GET_PROVINCE_CODE;
	;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_person_select_address);
		ViewInjectUtils.inject(this);
		cityNameList.setOnItemClickListener(this);
		setLeftTitle(res.getString(R.string.person_address_city_title));
		loadData();
	}

	private void loadData() {
		String city = getIntent().getStringExtra(SELECT_CITY);
		if (!TextUtils.isEmpty(city)) {
			cityCurrentCity.setText(String.format(res.getString(R.string.person_address_city_current), city));
		} else {
			cityCurrentCity.setText(String.format(res.getString(R.string.person_address_city_current), ""));
		}
		cityList = getCityList(GET_PROVINCE_CODE, PROVINCE_ID);
		cityAdapter = new CityListAdapter(this, cityList);
		cityNameList.setAdapter(cityAdapter);
	}

	private List<CityEntity> getCityList(String type, String id) {
		if (GET_PROVINCE_CODE.equals(type)) {
			return CityDBManager.getAllProvince(this);
		} else if (GET_CITY_CODE.equals(type)) {
			return CityDBManager.getCityByProvince(this, id);
		} else {
			return CityDBManager.getDistrictByCity(this, id);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent();
		if (GET_PROVINCE_CODE.equals(type)) {
			//省
			CityEntity cityEntity = cityList.get(position);
			provinceId = cityEntity.getNo();
			province = cityEntity.getName();
			cityCurrentCity.setText(String.format(res.getString(R.string.person_address_city_current), province));
			cityList = getCityList(GET_CITY_CODE, provinceId);
			cityAdapter.clear();
			cityAdapter.addDatas(cityList);
			cityNameList.smoothScrollToPosition(0);
			type = GET_CITY_CODE;
		} else if (GET_CITY_CODE.equals(type)) {
			//市
			city = cityList.get(position).getName();
			cityId = cityList.get(position).getNo();
			cityCurrentCity.setText(String.format(res.getString(R.string.person_address_city_current), province + city));
			cityList = getCityList(GET_DISTRICT_CODE, cityId);
			if (cityList.size() == 0) {
				intent.putExtra("provinceId", provinceId);
				intent.putExtra("provinceName", province);
				intent.putExtra("cityId", cityId);
				intent.putExtra("cityName", city);
				intent.putExtra("districtId", "");
				intent.putExtra("districtName", "");
				setResult(RESULT_OK, intent);
				finish();
			}
			cityAdapter.clear();
			cityAdapter.addDatas(cityList);
			cityNameList.smoothScrollToPosition(0);
			type = GET_DISTRICT_CODE;
		} else if (GET_DISTRICT_CODE.equals(type)) {
			district = cityList.get(position).getName();
			districtId = cityList.get(position).getNo();
			if (StringUtil.isEmpty(city)) {
				city = "";
			}
			if (StringUtil.isEmpty(district)) {
				district = "";
			}
			cityCurrentCity.setText(String.format(res.getString(R.string.person_address_city_current), province + city + district));

			intent.putExtra("provinceId", provinceId);
			intent.putExtra("provinceName", province);
			intent.putExtra("cityId", cityId);
			intent.putExtra("cityName", city);
			intent.putExtra("districtId", districtId);
			intent.putExtra("districtName", district);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	@Override
	public void success(int requestCode, String data) {
		super.success(requestCode, data);
		Intent intent = new Intent();
		intent.putExtra("addressid", districtId);
		intent.putExtra("address", province + city);
		setResult(RESULT_OK, intent);
		finish();
	}
}
