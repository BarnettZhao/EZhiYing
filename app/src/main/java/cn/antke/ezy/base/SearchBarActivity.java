package cn.antke.ezy.base;

import android.support.annotation.LayoutRes;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import cn.antke.ezy.R;
import cn.antke.ezy.widget.ClearEditText;


/**
 * Created by liuzhichao on 2016/11/30.
 * 带搜索的头部
 */
public abstract class SearchBarActivity extends BaseActivity {

	protected View btnBack;
	protected ClearEditText editSearch;
	protected View btnStartSearch;

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		super.setContentView(R.layout.base_search_frame);
		ViewGroup root = (ViewGroup) findViewById(R.id.frame_container);
		View.inflate(this, layoutResID, root);
		initSearchBar();
		handleLogic();
	}

	private void initSearchBar() {
		btnBack = findViewById(R.id.btn_back);
		editSearch = (ClearEditText) findViewById(R.id.edit_search);
		btnStartSearch = findViewById(R.id.btn_start_search);
	}

	protected void handleLogic() {
		backClick();
		editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (EditorInfo.IME_ACTION_SEARCH == actionId) {
					search();
					return true;
				}
				return false;
			}
		});
		btnStartSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				search();
			}
		});
	}


	protected void backClick() {
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	protected abstract void search();
}
