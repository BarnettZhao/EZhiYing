package cn.antke.ezy.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Liu_ZhiChao on 2015/11/10 15:17.
 * 涉及金额的格式化
 */
public class MoneyFormatUtil {

	/**
	 * 处理输入框只能输入小数后两位
	 * @param etAmount
	 * @return
	 */
	public static void addTextWatcher(final EditText etAmount){
		etAmount.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						etAmount.setText(s);
						etAmount.setSelection(s.length());
					}
				}
				if (s.toString().trim().equals(".")) {
					s = "0" + s;
					etAmount.setText(s);
					etAmount.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						etAmount.setText(s.subSequence(0, 1));
						etAmount.setSelection(1);
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
}
