package cn.antke.ezy.login.utils;

import android.content.Context;

import com.common.utils.PreferencesUtils;
import com.common.utils.StringUtil;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.UserEntity;

/**
 * Created by zhaoweiwei on 2017/5/5.
 * 用户信息
 */
public class UserCenter {

    private static final String USER_ID = "user_id";
    private static final String SITE_ID = "site_id";
    private static final String SITE_NAME = "site_name";
    private static final String USER_SITE_ID = "user_site_id";
    private static final String DEFAULT_ADDRESS = "default_address";
    private static final String AGE = "age";
    private static final String PHONE = "phone";
    private static final String HEAD_PIC = "head_pic";
    private static final String USER_NAME = "user_name";
    private static final String NICK_NAME = "nick_name";
    private static final String USER_CODE = "user_code";
    private static final String SET_PASS = "set_pass";
	private static final String STORE_ID = "store_id";
	private static final String STORE_NAME = "store_name";
	private static final String STORE_PIC = "store_pic";
	private static final String PERSON_FIRST = "person_first";

    public static void savaUserInfo(Context context, UserEntity userEntity) {
        PreferencesUtils.putString(context, USER_ID, userEntity.getId());
        PreferencesUtils.putString(context, SITE_ID, userEntity.getSiteId());
        PreferencesUtils.putString(context, SITE_NAME, userEntity.getSiteName());
        PreferencesUtils.putString(context, USER_SITE_ID, userEntity.getSiteId());
        PreferencesUtils.putString(context, DEFAULT_ADDRESS, userEntity.getProvince() + userEntity.getCity() + userEntity.getDistrict() + userEntity.getAddress());
        PreferencesUtils.putString(context, AGE, userEntity.getAge());
        PreferencesUtils.putString(context, PHONE, userEntity.getContact());
        PreferencesUtils.putString(context, HEAD_PIC, userEntity.getAvatar());
        PreferencesUtils.putString(context, USER_NAME, userEntity.getName());
        PreferencesUtils.putString(context, NICK_NAME, userEntity.getNickName());
        PreferencesUtils.putString(context, USER_CODE, userEntity.getUserCode());
        PreferencesUtils.putBoolean(context, SET_PASS, userEntity.isSetPwd());
        PreferencesUtils.putBoolean(context, PERSON_FIRST, userEntity.getIsFirst());
	    PreferencesUtils.putString(context, STORE_ID, userEntity.getStoreId());
    }

    public static void cleanLoginInfo(Context context) {
        PreferencesUtils.removeSharedPreferenceByKey(context, USER_ID);
        PreferencesUtils.removeSharedPreferenceByKey(context, SITE_ID);
        PreferencesUtils.removeSharedPreferenceByKey(context, SITE_NAME);
        PreferencesUtils.removeSharedPreferenceByKey(context, USER_SITE_ID);
        PreferencesUtils.removeSharedPreferenceByKey(context, DEFAULT_ADDRESS);
        PreferencesUtils.removeSharedPreferenceByKey(context, AGE);
        PreferencesUtils.removeSharedPreferenceByKey(context, PHONE);
        PreferencesUtils.removeSharedPreferenceByKey(context, HEAD_PIC);
        PreferencesUtils.removeSharedPreferenceByKey(context, USER_NAME);
        PreferencesUtils.removeSharedPreferenceByKey(context, NICK_NAME);
        PreferencesUtils.removeSharedPreferenceByKey(context, USER_CODE);
        PreferencesUtils.removeSharedPreferenceByKey(context, SET_PASS);
        PreferencesUtils.removeSharedPreferenceByKey(context, PERSON_FIRST);
        PreferencesUtils.removeSharedPreferenceByKey(context, STORE_ID);
    }

    public static String getUserId(Context context) {
        return PreferencesUtils.getString(context, USER_ID);
    }

    public static String getUserSiteId(Context context) {
        return PreferencesUtils.getString(context, USER_SITE_ID);
    }

    public static String getSiteId(Context context) {
        return PreferencesUtils.getString(context, SITE_ID);
    }

	public static void setSiteId(Context context, String siteId) {
		PreferencesUtils.putString(context, SITE_ID, siteId);
	}

	public static void setDefaultAddress(Context context, String address) {
		PreferencesUtils.putString(context, DEFAULT_ADDRESS, address);
	}

    public static String getDefaultAddress(Context context) {
        return PreferencesUtils.getString(context, DEFAULT_ADDRESS);
    }

    public static String getAge(Context context) {
        return PreferencesUtils.getString(context, AGE);
    }

    public static String getPhone(Context context) {
        return PreferencesUtils.getString(context, PHONE);
    }

    public static String getHeadPic(Context context) {
        return PreferencesUtils.getString(context, HEAD_PIC);
    }

    public static void setUserName(Context context,String userNanme) {
        PreferencesUtils.putString(context, USER_NAME, userNanme);
    }

    public static String getUserName(Context context) {
        return PreferencesUtils.getString(context, USER_NAME);
    }

    public static String getNickName(Context context) {
        return PreferencesUtils.getString(context, NICK_NAME);
    }

    public static String getStoreId(Context context) {
	    return PreferencesUtils.getString(context, STORE_ID);
    }

    public static String getUserCode(Context context) {
        return PreferencesUtils.getString(context, USER_CODE);
    }

    public static void setStoreName(Context context, String storeName) {
	    PreferencesUtils.putString(context, STORE_NAME, storeName);
    }

    public static void setStorePic(Context context, String storePic) {
	    PreferencesUtils.putString(context, STORE_PIC, storePic);
    }

    public static String getStoreName(Context context) {
	    return PreferencesUtils.getString(context, STORE_NAME);
    }

    public static String getStorePic(Context context) {
	    return PreferencesUtils.getString(context, STORE_PIC);
    }

    public static boolean isLogin(Context context) {
        String token = PreferencesUtils.getString(context, USER_ID);
        return !StringUtil.isEmpty(token);
    }

    public static void setIsSetPwd(Context context, boolean isSetPwd) {
        PreferencesUtils.putBoolean(context, SET_PASS, isSetPwd);
    }

    public static boolean isSetPwd(Context context){
        return PreferencesUtils.getBoolean(context,SET_PASS);
    }

    public static String getSiteName(Context context) {
        return PreferencesUtils.getString(context, SITE_NAME, context.getString(R.string.mainland));
    }

    public static void setSiteName(Context context, String areaName) {
        PreferencesUtils.putString(context, SITE_NAME, areaName);
    }

    public static boolean personFirst(Context context){
        return PreferencesUtils.getBoolean(context,PERSON_FIRST);
    }

    public static void setPersonFirst(Context context,boolean isFirst){
        PreferencesUtils.putBoolean(context,PERSON_FIRST,isFirst);
    }
}
