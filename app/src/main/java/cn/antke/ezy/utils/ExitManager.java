package cn.antke.ezy.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Liu_ZhiChao on 2015/9/8 15:11.
 * 统一关闭activity
 */
public class ExitManager {

	public static ExitManager instance = new ExitManager();

	private List<Activity> activityList = new LinkedList<>();
	private List<Activity> buyNowList = new LinkedList<>();
	private List<Activity> applyStoreList = new LinkedList<>();

	private ExitManager(){
	}

	public void addActivity(Activity activity){
		activityList.add(activity);
	}

	public void exit(){
		if (activityList != null && activityList.size() > 0){
			for(Activity activity : activityList){
				if(!activity.isFinishing()){
					activity.finish();
				}
			}
			activityList.clear();
		}
	}

	/**
	 * 返回到指定的历史activity
	 * @param act Activity.class
	 */
	public void toActivity(Class<?> act) {
		if (activityList != null && activityList.size() > 0){
			List<Activity> listAct = null;
			for(int i=activityList.size()-1; i>=0; i--){
				Activity activity = activityList.get(i);
				if(activity.getClass().getSimpleName().equals(act.getSimpleName())) {
					break;
				} else if(!activity.isFinishing()){
					activity.finish();
					if(listAct == null) {
						listAct = new ArrayList<>();
					}
					listAct.add(activity);
				}
			}
			if(listAct != null && listAct.size() > 0) {
				activityList.removeAll(listAct);
			}
		}
	}

	public List<Activity> getActivities(){
		return activityList;
	}

	public void remove(Activity activity) {
		if (activityList != null && activityList.contains(activity)){
			activityList.remove(activity);
		}
	}

	public void addBuyNowActivity(Activity activity) {
		buyNowList.add(activity);
	}

	public void closeBuyNowActivity() {
		if (buyNowList != null && buyNowList.size() > 0) {
			for (Activity activity : buyNowList) {
				if (!activity.isFinishing()) {
					activity.finish();
				}
			}
			buyNowList.clear();
		}
	}

	public void addApplyStoreActivity(Activity activity) {
		applyStoreList.add(activity);
	}

	public void closeApplyStoreActivity() {
		if (applyStoreList != null && applyStoreList.size() > 0) {
			for (Activity activity : applyStoreList) {
				if (!activity.isFinishing()) {
					activity.finish();
				}
			}
			applyStoreList.clear();
		}
	}
}
