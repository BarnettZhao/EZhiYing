package cn.antke.ezy.deal.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.widget.RefreshRecyclerView;

import java.util.IdentityHashMap;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.deal.adapter.SaleAdapter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.network.entities.PagesEntity;
import cn.antke.ezy.network.entities.SaleEntity;

/**
 * Created by liuzhichao on 2017/5/23.
 * 撤销卖出
 */
public class CancelSaleActivity extends ToolBarActivity implements View.OnClickListener {

	private RefreshRecyclerView recyclerView;
	private SaleAdapter adapter;

	public static void startCancelSaleActivity(Context context) {
		Intent intent = new Intent(context, CancelSaleActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_cancel_sale);
		initView();
		loadData(false);
	}

	private void initView() {
		setLeftTitle(getString(R.string.cancel_sell));

		recyclerView = (RefreshRecyclerView) findViewById(R.id.rrv_sale_list);
		recyclerView.setHasFixedSize(true);
		recyclerView.setMode(RefreshRecyclerView.Mode.BOTH);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setOnRefreshAndLoadMoreListener(new RefreshRecyclerView.OnRefreshAndLoadMoreListener() {
			@Override
			public void onRefresh() {
				loadData(false);
			}

			@Override
			public void onLoadMore() {
				loadData(true);
			}
		});
	}

	private void loadData(boolean isMore) {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put(CommonConstant.PAGESIZE, CommonConstant.PAGE_SIZE_20);
		int requestCode = CommonConstant.REQUEST_NET_ONE;
		int num = 1;
		if (isMore) {
			num = adapter.getPage() + 1;
			requestCode = CommonConstant.REQUEST_NET_TWO;
		}
		params.put(CommonConstant.PAGENUM, String.valueOf(num));
		requestHttpData(Constants.Urls.URL_POST_SELL_INTEGRAL_LIST, requestCode, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		recyclerView.resetStatus();
		Entity result = Parsers.getResult(data);
		if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			switch (requestCode) {
				case CommonConstant.REQUEST_NET_ONE:{
					PagesEntity<SaleEntity> pageSale = Parsers.getPageSale(data);
					if (pageSale.getDatas() != null) {
						adapter = new SaleAdapter(pageSale.getDatas(), this);
						recyclerView.setAdapter(adapter);
					}

					if (pageSale.getTotalPage() > 1) {
						recyclerView.setCanAddMore(true);
					} else {
						recyclerView.setCanAddMore(false);
					}
					break;
				}
				case CommonConstant.REQUEST_NET_TWO:{
					PagesEntity<SaleEntity> pageSale = Parsers.getPageSale(data);
					adapter.addDatas(pageSale.getDatas());
					if (adapter.getPage() >= pageSale.getTotalPage()) {
						recyclerView.setCanAddMore(false);
					}
					break;
				}
				case CommonConstant.REQUEST_NET_THREE:{
					loadData(false);
					break;
				}
			}
		} else {
			ToastUtil.shortShow(this, result.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		recyclerView.resetStatus();
		super.mistake(requestCode, status, errorMessage);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_sale_cancel:
				SaleEntity saleEntity = (SaleEntity) v.getTag();
				if (saleEntity != null) {
					showProgressDialog();
					IdentityHashMap<String, String> params = new IdentityHashMap<>();
					params.put("sellout_id", saleEntity.getId());
					requestHttpData(Constants.Urls.URL_POST_SELL_INTEGRAL_CANCEL, CommonConstant.REQUEST_NET_THREE, FProtocol.HttpMethod.POST,params);
				}
				break;
		}
	}
}
