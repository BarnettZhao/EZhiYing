package cn.antke.ezy.draw.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.widget.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.draw.adapter.DrawListAdapter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.DrawQueryEntity;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.network.entities.PagesEntity;

/**
 * Created by liuzhichao on 2017/5/15.
 * 中奖查询
 */
public class DrawQueryActivity extends ToolBarActivity {

	private RefreshRecyclerView rrvDrawList;
	private DrawListAdapter adapter;

	public static void startDrawQueryActivity(Context context) {
		Intent intent = new Intent(context, DrawQueryActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_draw_query);
		initView();
		loadData(false);
	}

	private void initView() {
		setLeftTitle(getString(R.string.draw_list));
		rrvDrawList = (RefreshRecyclerView) findViewById(R.id.rrv_draw_list);
		rrvDrawList.setHasFixedSize(true);
		rrvDrawList.setMode(RefreshRecyclerView.Mode.BOTH);
		rrvDrawList.setLayoutManager(new LinearLayoutManager(this));
		rrvDrawList.setOnRefreshAndLoadMoreListener(new RefreshRecyclerView.OnRefreshAndLoadMoreListener() {
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
		params.put(CommonConstant.PAGESIZE, CommonConstant.PAGE_SIZE_10);
		int page = 1;
		int requestCode = CommonConstant.REQUEST_NET_ONE;
		if (isMore) {
			page = adapter.getPage() + 1;
			requestCode = CommonConstant.REQUEST_NET_TWO;
		}
		params.put(CommonConstant.PAGENUM, String.valueOf(page));
		requestHttpData(Constants.Urls.URL_POST_DRAW_QUERY, requestCode, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		rrvDrawList.resetStatus();
		Entity result = Parsers.getResult(data);
		if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			switch (requestCode) {
				case CommonConstant.REQUEST_NET_ONE:{
					PagesEntity<DrawQueryEntity> drawQueryPage = Parsers.getDrawQueryPage(data);
					List<DrawQueryEntity> drawQueryEntities = drawQueryPage.getDatas();
					if (drawQueryEntities == null) {
						drawQueryEntities = new ArrayList<>();
					}
					drawQueryEntities.add(0, new DrawQueryEntity(getString(R.string.no), getString(R.string.register_usernumber_text), getString(R.string.prize)));
					adapter = new DrawListAdapter(drawQueryEntities);
					rrvDrawList.setAdapter(adapter);
					if (drawQueryPage.getTotalPage() > 1) {
						rrvDrawList.setCanAddMore(true);
					} else {
						rrvDrawList.setCanAddMore(false);
					}
					break;
				}
				case CommonConstant.REQUEST_NET_TWO:{
					PagesEntity<DrawQueryEntity> drawQueryPage = Parsers.getDrawQueryPage(data);
					List<DrawQueryEntity> drawQueryEntities = drawQueryPage.getDatas();
					if (drawQueryEntities == null) {
						drawQueryEntities = new ArrayList<>();
					}
					adapter.addDatas(drawQueryEntities);
					if (adapter.getPage() >= drawQueryPage.getTotalPage()) {
						rrvDrawList.setCanAddMore(false);
					}
					break;
				}
			}
		} else {
			ToastUtil.shortShow(this, result.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		rrvDrawList.resetStatus();
		super.mistake(requestCode, status, errorMessage);
	}
}
