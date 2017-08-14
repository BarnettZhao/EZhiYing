package cn.antke.ezy.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.common.widget.FootLoadingListView;
import com.common.widget.PullToRefreshBase;

import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.AddressEntity;
import cn.antke.ezy.network.entities.AddressPageEntity;
import cn.antke.ezy.person.adapter.AddressAdapter;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_ENTITY;
import static cn.antke.ezy.common.CommonConstant.EXTRA_TYPE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_TWO;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_THREE;
import static cn.antke.ezy.common.CommonConstant.TYPE_ADD;
import static cn.antke.ezy.common.CommonConstant.TYPE_EDIT;

/**
 * Created by zhaoweiwei on 2016/12/25.
 * 地址管理
 */
public class AddressListActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.address_list)
    private FootLoadingListView addressList;
    @ViewInject(R.id.address_add_new)
    private TextView addressAddNew;

    private AddressAdapter addressAdapter;
    private AddressEntity defaultEntity;
    private List<AddressEntity> addressEntities;
    private int from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_person_address_list);
        ViewInjectUtils.inject(this);
        initView();
        loadData();
    }

    private void initView() {
        setLeftTitle(getString(R.string.person_address));
        from = getIntent().getIntExtra(CommonConstant.EXTRA_FROM, 0);
        addressAddNew.setOnClickListener(this);
        initLoadingView(this);
        setLoadingStatus(LoadingStatus.GONE);
        addressList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        addressList.setOnItemClickListener((parent, view, position, id) -> {
            if (from == CommonConstant.FROM_CONFIRM_ORDER && addressEntities != null && addressEntities.size() > position) {
                Intent intent = new Intent();
                intent.putExtra("address", addressEntities.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        addressList.setOnRefreshListener(refreshView -> loadData());
    }

    private void loadData() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put(CommonConstant.PAGESIZE, String.valueOf(6));
        params.put(CommonConstant.PAGENUM, String.valueOf(1));
        requestHttpData(Constants.Urls.URL_POST_ADDRESS_LIST, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.address_add_new:
                startActivityForResult(new Intent(this, AddressEditActivity.class).putExtra(EXTRA_TYPE, TYPE_ADD), REQUEST_ACT_ONE);
                break;
            case R.id.address_item_default:
                defaultEntity = (AddressEntity) view.getTag();
                setDefaultAddress();
                break;
            case R.id.address_item_edit:
                AddressEntity editEntity = (AddressEntity) view.getTag();
                Intent editIntent = new Intent(this, AddressEditActivity.class);
                editIntent.putExtra(EXTRA_TYPE, TYPE_EDIT);
                editIntent.putExtra(EXTRA_ENTITY, editEntity);
                startActivityForResult(editIntent, REQUEST_ACT_TWO);
                break;
            case R.id.loading_layout:
                loadData();
                break;
        }
    }

    private void setDefaultAddress() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("receivingId", defaultEntity.getReciveId());
        params.put("operation_type", "2");//1: 添加 2：修改
        params.put("consignee", defaultEntity.getUserName());
        params.put("contacts", defaultEntity.getUserPhone());
        params.put("province", defaultEntity.getProvinceId());
        params.put("city", defaultEntity.getCityId());
        params.put("district", defaultEntity.getDistrictId());
        params.put("address", defaultEntity.getAddress());
        params.put("isDefault", "1");//1：是，2：不是
        requestHttpData(Constants.Urls.URL_POST_ADD_EDIT_ADDRESS, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
    }

    @Override
    public void success(int requestCode, String data) {
        closeProgressDialog();
        setLoadingStatus(LoadingStatus.GONE);
        addressList.setOnRefreshComplete();
        switch (requestCode) {
            case REQUEST_NET_ONE: {
                AddressPageEntity addressPageEntity = Parsers.getAddressPage(data);
                addressEntities = addressPageEntity.getAddressEntities();
                if (addressEntities != null && addressEntities.size() > 0) {
                    addressAdapter = new AddressAdapter(this, addressEntities, this);
                    addressList.setAdapter(addressAdapter);
                    if (addressEntities.size() < 6) {
                        addressAddNew.setVisibility(View.VISIBLE);
                    } else {
                        addressAddNew.setVisibility(View.GONE);
                    }
                }
                break;
            }
            case REQUEST_NET_THREE://设置默认地址
                for (AddressEntity entity : addressEntities) {
                    if (defaultEntity.getReciveId().equals(entity.getReciveId())) {
                        entity.setDefault(true);
                    } else {
                        entity.setDefault(false);
                    }
                }
                addressAdapter.notifyDataSetChanged();
                break;
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
                case REQUEST_ACT_ONE://新增地址
                case REQUEST_ACT_TWO://编辑地址
                    loadData();
                    break;
            }
        }
    }

    @Override
    public void finish() {
        if (from != CommonConstant.FROM_CONFIRM_ORDER) {
            Intent intent = getIntent();
            intent.putExtra("defaultAddress", getDefaultAddress());
            setResult(RESULT_OK, intent);
        }
        super.finish();
    }

    private String getDefaultAddress() {
        String address = "";
        if (addressEntities != null && addressEntities.size() > 0) {
            for (AddressEntity entity : addressEntities) {
                if (entity.isDefault()) {
                    address = entity.getProvinceName() + entity.getCityName() + entity.getDistrictName() + entity.getAddress();
                }
            }
        }
        UserCenter.setDefaultAddress(this, address);
        return address;
    }
}
