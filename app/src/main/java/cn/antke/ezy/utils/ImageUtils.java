package cn.antke.ezy.utils;

import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.common.utils.StringUtil;

/**
 * Created by Liu_Zhichao on 2016/9/2 11:20.
 * 图片处理工具类
 */
public class ImageUtils {

	/**
	 * 七牛图片处理
	 */
	public static void setSmallImg(ImageView imageView, String imgUrl) {
		if (imageView == null || imgUrl == null) {
			return;
		}
		if (StringUtil.isEmpty(imgUrl)) {
			imageView.setImageURI(Uri.parse(imgUrl));
			return;
		}
		try {
			ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
			if (!imgUrl.contains("?")) {
				int width = layoutParams.width;
				int height = layoutParams.height;
				if (width < 0) {
					width = 0;
				}
				if (height < 0) {
					height = 0;
				}
				imgUrl += "?imageView2/0/w/" + width + "/h/" + height;
			}
			imageView.setImageURI(Uri.parse(imgUrl));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 普通加载图片，过滤掉null值
	 * @param imageView 图片控件
	 * @param imgUrl 图片地址
	 */
	public static void setImgUrl(ImageView imageView, String imgUrl) {
		if (imageView == null || imgUrl == null) {
			return;
		}
		imageView.setImageURI(Uri.parse(imgUrl));
	}
}
