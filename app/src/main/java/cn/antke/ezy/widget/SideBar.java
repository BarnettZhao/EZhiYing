package cn.antke.ezy.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by zhaoweiwei on 2017/5/16.
 * 通讯录字母列表
 */
public class SideBar extends AppCompatTextView {
	private String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z"};
	private Paint textPaint;
	private Paint bigTextPaint;

	private Canvas canvas;
	private int itemH;
	private int w;
	/**
	 * 普通情况下字体大小
	 */
	float singleTextH;
	/**
	 * 滑动的Y
	 */
	private float eventY = 0;

	private ISideBarSelectCallBack callBack;

	public SideBar(Context context) {
		this(context, null);
	}

	public SideBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(getCurrentTextColor());
		textPaint.setTextSize(getTextSize());
		textPaint.setTextAlign(Paint.Align.CENTER);
		bigTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bigTextPaint.setColor(getCurrentTextColor());
		bigTextPaint.setTextSize(getTextSize() * 2);
		bigTextPaint.setTextAlign(Paint.Align.CENTER);
	}

	public void setOnStrSelectCallBack(ISideBarSelectCallBack callBack) {
		this.callBack = callBack;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				if (event.getX() > (w - getPaddingRight() - singleTextH - 10)) {
					eventY = event.getY();
					invalidate();
					return true;
				} else {
					eventY = 0;
					invalidate();
					break;
				}
			case MotionEvent.ACTION_CANCEL:
				eventY = 0;
				invalidate();
				return true;
			case MotionEvent.ACTION_UP:
				if (event.getX() > (w - getPaddingRight() - singleTextH - 10)) {
					eventY = 0;
					invalidate();
					return true;
				} else
					break;
		}
		return super.onTouchEvent(event);
	}


	@Override
	protected void onDraw(Canvas canvas) {
		this.canvas = canvas;
		DrawView(eventY);
	}

	private void DrawView(float y) {
		int currentSelectIndex = -1;
		if (y != 0) {
			for (int i = 0; i < letters.length; i++) {
				float currentItemY = itemH * i;
				float nextItemY = itemH * (i + 1);
				if (y >= currentItemY && y < nextItemY) {
					currentSelectIndex = i;
					if (callBack != null) {
						callBack.onSelectStr(currentSelectIndex, letters[i]);
					}
					//画大的字母
					Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
					float testSize = fontMetrics.descent - fontMetrics.ascent;
					canvas.drawText(letters[i], w - getPaddingRight() - testSize, singleTextH + itemH * i, bigTextPaint);
				}
			}
		}
		drawLetters(y, currentSelectIndex);
	}

	private void drawLetters(float y, int index) {
		//第一次进来没有缩放情况，默认画原图
		if (index == -1) {
			w = getMeasuredWidth();
			int h = getMeasuredHeight();
			itemH = h / letters.length;
			Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
			singleTextH = fontMetrics.descent - fontMetrics.ascent;
			for (int i = 0; i < letters.length; i++) {
				canvas.drawText(letters[i], w - getPaddingRight(), singleTextH + itemH * i, textPaint);
			}
			//触摸的时候画缩放图
		} else {
			//遍历所有字母
			for (int i = 0; i < letters.length; i++) {
				//要画的字母的起始Y坐标
				float currentItemToDrawY = singleTextH + itemH * i;
				float centerItemToDrawY;
				if (index < i) {
					centerItemToDrawY = singleTextH + itemH * (index + 1);
				} else {
					centerItemToDrawY = singleTextH + itemH * (index - 1);
				}
				float delta = 1 - Math.abs((y - currentItemToDrawY) / (centerItemToDrawY - currentItemToDrawY));
				float maxRightX = w - getPaddingRight();
				//如果大于0，表明在y坐标上方
//				scaleTextPaint.setTextSize(getTextSize() + getTextSize() * delta);
				float drawX = maxRightX - delta;
				//超出边界直接花在边界上
				if (drawX > maxRightX)
					canvas.drawText(letters[i], maxRightX, singleTextH + itemH * i, textPaint);
//				else
//					canvas.drawText(letters[i], drawX, singleTextH + itemH * i, scaleTextPaint);
			}
		}
	}

	public interface ISideBarSelectCallBack {
		void onSelectStr(int index, String selectStr);
	}

}

