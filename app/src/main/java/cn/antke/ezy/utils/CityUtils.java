package cn.antke.ezy.utils;

import android.content.Context;

import com.common.utils.PreferencesUtils;
import com.common.utils.StringUtil;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by jacktian on 15/9/12.
 */
public class CityUtils {

    public static CityUtils instance = null;
    public final static String CITY_PRE_KEY_DB_VERSION = "city_pre_db_version";
    public final static String CITY_PROPERTIES_DB_VERSION = "db_version";
    public final static String CITY_PRE_KEY_AREA_VERSION = "city_pre_area_version";
    public final static String CITY_PROPERTIES_AREA_VERSION = "area_version";


    private CityUtils(){

    }

    public static synchronized CityUtils getInstance(){
        if (instance == null){
            new CityUtils();
        }

        return instance;
    }


    public static void intCityVersion(Context context){
//        String oldAreaVersion = getCityAreaVersion(context);
//        if (StringUtil.isEmpty(oldAreaVersion)){
            Properties properties = new Properties();
            try {
                properties.load(context.getAssets().open("city.properties"));
                String areaVersion = (String)properties.get(CITY_PROPERTIES_AREA_VERSION);
                setCityAreaVersion(context, areaVersion);
                int DBversion = Integer.parseInt((String)properties.get(CITY_PROPERTIES_DB_VERSION));
                setCityDBVersion(context, DBversion);
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }

    }

    public static void setCityDBVersion(Context context, int version){
        PreferencesUtils.putInt(context, CITY_PRE_KEY_DB_VERSION, version);
    }

    public static int getCityDBVersion(Context context){
        return PreferencesUtils.getInt(context, CITY_PRE_KEY_DB_VERSION);
    }

    public static void setCityAreaVersion(Context context, String version){
        PreferencesUtils.putString(context, CITY_PRE_KEY_AREA_VERSION, version);
    }

    public static String getCityAreaVersion(Context context){
        return PreferencesUtils.getString(context, CITY_PRE_KEY_AREA_VERSION, "1");
    }

    public static String getCityShortName(String name){
        String split = "市";
        if (!StringUtil.isEmpty(name) && name.contains(split)){
            name = name.substring(0, name.indexOf(split));
        }
        return name;
    }
}
