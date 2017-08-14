package cn.antke.ezy.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.AddressEntity;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.utils.InputUtil;
import cn.antke.ezy.utils.VerifyUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_ENTITY;
import static cn.antke.ezy.common.CommonConstant.EXTRA_TYPE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.TYPE_ADD;
import static cn.antke.ezy.common.CommonConstant.TYPE_EDIT;

/**
 * Created by zhaoweiwei on 2016/12/26.
 * 添加/修改收货地址
 */
public class AddressEditActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.edit_address_user)
    private EditText editAddressUser;
    @ViewInject(R.id.edit_address_phone)
    private EditText editAddressPhone;
    @ViewInject(R.id.edit_address_select_area)
    private RelativeLayout editAddressSelect;
    @ViewInject(R.id.edit_address_area)
    private TextView editAddressArea;
    @ViewInject(R.id.edit_address_detail)
    private EditText editAddressDetail;
    @ViewInject(R.id.edit_address_default)
    private TextView editAddressDefault;
    @ViewInject(R.id.edit_address_save)
    private TextView editAddressSave;

    private boolean isDefault;
    private int type;
    private String districtId;
    private String cityId;
    private String provinceId;
    private String receiveingId = "";
    private String status = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_person_address_edit);
        ViewInjectUtils.inject(this);
        type = getIntent().getIntExtra(EXTRA_TYPE, TYPE_ADD);
        initView();
    }

    private void initView() {
        if (TYPE_EDIT == type) {
            status = "2";
            AddressEntity entity = getIntent().getParcelableExtra(EXTRA_ENTITY);
            if (entity != null) {
                receiveingId = entity.getReciveId();
                provinceId = entity.getProvinceId();
                cityId = entity.getCityId();
                districtId = entity.getDistrictId();
                editAddressUser.setText(entity.getUserName());
                editAddressPhone.setText(entity.getUserPhone());
                editAddressDetail.setText(entity.getAddress());
                editAddressArea.setText(entity.getProvinceName() + entity.getCityName() + entity.getDistrictName());
                isDefault = entity.isDefault();
                if (entity.isDefault()) {
                    editAddressDefault.setTextColor(ContextCompat.getColor(this, R.color.primary_color_text));
                    editAddressDefault.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.list_check_checked, 0, 0, 0);
                } else {
                    editAddressDefault.setTextColor(ContextCompat.getColor(this, R.color.text_introduce_color));
                    editAddressDefault.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.list_check_normal, 0, 0, 0);
                }
                editAddressSave.setEnabled(true);
            }
            setLeftTitle(getString(R.string.person_address_edit));
            setRightText(getString(R.string.delete));
            rightText.setOnClickListener(this);
        } else {
            setLeftTitle(getString(R.string.person_address));
        }
        InputUtil.editIsEmpty(editAddressSave, editAddressUser, editAddressPhone, editAddressDetail);
        editAddressSelect.setOnClickListener(this);
        editAddressDefault.setOnClickListener(this);
        editAddressSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_address_select_area:
                startActivityForResult(new Intent(this, AddressSelectActivity.class), REQUEST_ACT_ONE);
                break;
            case R.id.edit_address_default:
                isDefault = !isDefault;
                if (isDefault) {
                    editAddressDefault.setTextColor(ContextCompat.getColor(this, R.color.primary_color_text));
                    editAddressDefault.setCompoundDrawablesWithIntrinsicBounds(R.drawable.list_check_checked, 0, 0, 0);
                } else {
                    editAddressDefault.setTextColor(ContextCompat.getColor(this, R.color.text_introduce_color));
                    editAddressDefault.setCompoundDrawablesWithIntrinsicBounds(R.drawable.list_check_normal, 0, 0, 0);
                }
                break;
            case R.id.edit_address_save:
                updateAddress();
                break;
            case R.id.rigth_text:
                deleteAddress();
                break;
        }
    }

    /**
     * 添加/修改收货地址
     */
    private void updateAddress() {
        String phoneNumber = editAddressPhone.getText().toString();
        if (!VerifyUtils.checkPhoneNumber(phoneNumber)) {
            ToastUtil.shortShow(this, getString(R.string.register_input_phone));
            return;
        }
        if (StringUtil.isEmpty(provinceId)) {
            ToastUtil.shortShow(this, getString(R.string.person_address_select_province));
            return;
        }
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("receivingId", receiveingId);
        params.put("operation_type", status);//1: 添加 2：修改
        params.put("consignee", editAddressUser.getText().toString());
        params.put("contacts", phoneNumber);
        params.put("province", provinceId);
        params.put("city", cityId);
        params.put("district", districtId);
        params.put("address", editAddressDetail.getText().toString());
        if (isDefault) {
            params.put("isDefault", "1");//1：是，2：不是
        } else {
            params.put("isDefault", "2");//1：是，2：不是
        }
        requestHttpData(Constants.Urls.URL_POST_ADD_EDIT_ADDRESS, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    private void deleteAddress() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("receiving_id", receiveingId);
        requestHttpData(Constants.Urls.URL_POST_DELETE_ADDRESS, CommonConstant.REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void success(int requestCode, String data) {
        closeProgressDialog();
        Entity result = Parsers.getResult(data);
        if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
            setResult(RESULT_OK);
            finish();
        } else {
            ToastUtil.shortShow(this, result.getResultMsg());
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        closeProgressDialog();
        ToastUtil.shortShow(this, errorMessage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case REQUEST_ACT_ONE://获取省市区
                    String provinceName = data.getStringExtra("provinceName");
                    provinceId = data.getStringExtra("provinceId");
                    String cityName = data.getStringExtra("cityName");
                    cityId = data.getStringExtra("cityId");
                    String destrictName = data.getStringExtra("districtName");
                    districtId = data.getStringExtra("districtId");
                    editAddressArea.setText(provinceName + cityName + destrictName);
                    break;
            }
        }
    }
}
