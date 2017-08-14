package cn.antke.ezy.home.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import com.common.network.FProtocol;
import com.common.utils.PreferencesUtils;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.BaseActivity;
import cn.antke.ezy.common.CommonConfig;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.home.adapter.HistorySearchAdapter;
import cn.antke.ezy.home.adapter.HotSearchAdapter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.product.controller.ProductListActivity;
import cn.antke.ezy.utils.ViewInjectUtils;
import cn.antke.ezy.widget.flowlayout.FlowTagLayout;

import static cn.antke.ezy.common.CommonConstant.SEARCH_PRE_KEY_HISTORY;

/**
 * Created by liuzhichao on 2017/5/10.
 * 搜索界面
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {

	@ViewInject(R.id.et_search_input)
	private EditText etSearchInput;
	@ViewInject(R.id.tv_search_cancel)
	private View tvSearchCancel;
	@ViewInject(R.id.ftl_search_hot_search)
	private FlowTagLayout ftlSearchHotSearch;
	@ViewInject(R.id.lv_search_history)
	private ListView lvSearchHistory;
	@ViewInject(R.id.ll_search_clean_history)
	private View llSearchCleanHistory;

	private List<String> historyDatas = new ArrayList<>();
	private List<String> hotDatas;
	private HistorySearchAdapter historyAdapter;
	private HotSearchAdapter hotAdapter;

	public static void startSearchActivity(Context context) {
		Intent intent = new Intent(context, SearchActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_search);
		ViewInjectUtils.inject(this);
		initView();
		loadData();
	}

	private void initView() {
		//历史搜索
		historyAdapter = new HistorySearchAdapter(this, historyDatas);
		lvSearchHistory.setAdapter(historyAdapter);

		hotAdapter = new HotSearchAdapter(this);
		ftlSearchHotSearch.setAdapter(hotAdapter);

		etSearchInput.setOnEditorActionListener((v, actionId, event) -> {
			if (EditorInfo.IME_ACTION_SEARCH == actionId) {
				search();
			}
			return false;
		});
		ftlSearchHotSearch.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_NONE);
		ftlSearchHotSearch.setOnTagClickListener((parent, view, position) -> {
			etSearchInput.setText(hotDatas.get(position));
			etSearchInput.setSelection(hotDatas.get(position).length());
			search();
		});
		lvSearchHistory.setOnItemClickListener((parent, view, position, id) -> {
			etSearchInput.setText(historyDatas.get(position));
			etSearchInput.setSelection(historyDatas.get(position).length());
			search();
		});

		tvSearchCancel.setOnClickListener(this);
		llSearchCleanHistory.setOnClickListener(this);
	}

	private void loadData() {
		requestHttpData(Constants.Urls.URL_POST_HOT_KEY, CommonConstant.REQUEST_NET_ONE, FProtocol.HttpMethod.POST, null);
	}

	private void search() {
		String content = etSearchInput.getText().toString().trim();
		setSearchStoreHistory(content);
		ProductListActivity.startProductListActivity(this, CommonConstant.FROM_PLATE_PRODUCT, "", "", content);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getSearchStoreHistory();
	}

	@Override
	public void success(int requestCode, String data) {
		Entity result = Parsers.getResult(data);
		if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			switch (requestCode) {
				case CommonConstant.REQUEST_NET_ONE:
					hotDatas = Parsers.getHotWord(data);
					hotAdapter.onlyAddAll(hotDatas);
					break;
			}
		} else {
			ToastUtil.shortShow(this, result.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		ToastUtil.shortShow(this, errorMessage);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_search_cancel:
				finish();
				break;
			case R.id.ll_search_clean_history:
				//清除历史记录
				PreferencesUtils.removeSharedPreferenceByKey(this, SEARCH_PRE_KEY_HISTORY);
				historyAdapter.clear();
				llSearchCleanHistory.setVisibility(View.GONE);
				break;
		}
	}

	public void getSearchStoreHistory() {
		String historyStr = PreferencesUtils.getString(this, SEARCH_PRE_KEY_HISTORY);
		if (historyDatas != null) {
			historyDatas.clear();
			List<String> historys = new ArrayList<>();
			if (!StringUtil.isEmpty(historyStr)) {
				Collections.addAll(historys, historyStr.split(","));
				for (int i = 0; i < historys.size() && i < CommonConfig.SEARCH_HISTORY_COUNT; i++) {
					historyDatas.add(historys.get(i));
					historyAdapter.notifyDataSetChanged();
				}
			}
			if (historys.size() > 0) {
				llSearchCleanHistory.setVisibility(View.VISIBLE);
			} else {
				llSearchCleanHistory.setVisibility(View.GONE);
			}
		}
	}

	public void setSearchStoreHistory(String history) {
		StringBuilder historyStr = new StringBuilder();
		if (historyDatas != null) {
			if (historyDatas.contains(history)) {
				historyDatas.remove(history);
			}
			if (historyDatas.size() >= CommonConfig.SEARCH_HISTORY_COUNT) {
				historyDatas.remove(historyDatas.size() - 1);
			}
			historyStr.append(history);
			for (String str : historyDatas) {
				historyStr.append(",");
				historyStr.append(str);
			}
			historyDatas.add(0, history);
		}
		PreferencesUtils.putString(this, SEARCH_PRE_KEY_HISTORY, historyStr.toString());
	}
}
