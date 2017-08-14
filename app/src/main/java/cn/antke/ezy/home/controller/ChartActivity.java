package cn.antke.ezy.home.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.common.viewinject.annotation.ViewInject;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.utils.ViewInjectUtils;

/**
 * Created by liuzhichao on 2017/4/28.
 * 图表测试
 */
public class ChartActivity extends AppCompatActivity {

	protected Typeface mTfLight;

	@ViewInject(R.id.lc_chart_line)
	private LineChart lcChartLine;

	public static void startChartActivity(Context context) {
		Intent intent = new Intent(context, ChartActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_chart);
		ViewInjectUtils.inject(this);

		//设置字体
		mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

		// no description text
		lcChartLine.getDescription().setEnabled(false);

		List<Entry> datas = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			float val = (float) (Math.random() * 600) + 3;
			datas.add(new Entry(i, val));
		}
		//折线图
		LineDataSet lineDataSet = new LineDataSet(datas, "图表1");
		//蚂蚁线
//		lineDataSet.enableDashedLine(10f, 5f, 0f);
//		lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);
		//折线颜色
		lineDataSet.setColor(Color.parseColor("#58b4aa"));
		//数据点圆圈颜色
		lineDataSet.setCircleColor(Color.parseColor("#58b4aa"));
		//数据点圆圈大小
		lineDataSet.setCircleRadius(5f);
		//数据点圆圈是否显示空心
		lineDataSet.setDrawCircleHole(true);
		//空心大小
		lineDataSet.setCircleHoleRadius(2f);
		//空心颜色
		lineDataSet.setCircleColorHole(Color.WHITE);
		//线宽，粗细
		lineDataSet.setLineWidth(2f);
		//数据文字大小
		lineDataSet.setValueTextSize(9f);
		//折线模式，CUBIC_BEZIER 弧形、平滑过渡
		lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

		//是否填充
		lineDataSet.setDrawFilled(true);
		//填充部分透明度
		lineDataSet.setFillAlpha(30);
		if (Utils.getSDKInt() >= 18) {
			// fill drawable only supported on api level 18 and above
//			Drawable drawable = ContextCompat.getDrawable(this, R.drawable.scan_mask);
//			lineDataSet.setFillDrawable(drawable);
			lineDataSet.setFillColor(Color.parseColor("#58b4aa"));
		} else {
			lineDataSet.setFillColor(Color.parseColor("#58b4aa"));
		}

		LineData lineData = new LineData(lineDataSet);
		lcChartLine.setData(lineData);

		//折线说明，色值、名称
		Legend legend = lcChartLine.getLegend();
		legend.setEnabled(false);

		//X轴
		XAxis xAxis = lcChartLine.getXAxis();
		xAxis.setTypeface(mTfLight);
		xAxis.setTextSize(12f);
		xAxis.setTextColor(Color.parseColor("#999999"));
		xAxis.setYOffset(0f);//x轴标值y方向偏移量，默认会向下偏移
		xAxis.setDrawGridLines(false);//区域内分隔线
		xAxis.setDrawAxisLine(true);//轴线
		//设置X轴所在位置
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

		//左Y轴
		YAxis leftAxis = lcChartLine.getAxisLeft();
		leftAxis.setTypeface(mTfLight);
		leftAxis.setTextSize(12f);
		leftAxis.setTextColor(Color.parseColor("#999999"));
		leftAxis.setDrawZeroLine(false);//0线是否显示
//		leftAxis.setAxisMaximum(200f);
//		leftAxis.setAxisMinimum(0f);
		leftAxis.setDrawGridLines(true);
		leftAxis.setGranularityEnabled(true);
		leftAxis.setDrawAxisLine(false);
		leftAxis.setGridColor(Color.parseColor("#eeeeee"));
		leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

		//不显示右Y轴
		lcChartLine.getAxisRight().setEnabled(false);
	}
}
