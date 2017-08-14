package cn.antke.ezy.home.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.home.adapter.LanguageAreaAdapter;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.main.controller.MainActivity;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.AreaEntity;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.utils.ConfigUtils;
import cn.antke.ezy.utils.ExitManager;
import cn.antke.ezy.utils.ViewInjectUtils;

/**
 * Created by liuzhichao on 2017/5/10.
 * 选择语言和地区
 */
public class LanguageAndAreaActivity extends ToolBarActivity {

    @ViewInject(R.id.tv_area_current)
    private TextView tvAreaCurrent;
    @ViewInject(R.id.lv_language_list)
    private ListView lvLanguageList;
    @ViewInject(R.id.tv_language_current)
    private TextView tvLanguageCurrent;
    @ViewInject(R.id.lv_area_list)
    private ListView lvAreaList;
    @ViewInject(R.id.tv_area_confirm)
    private View tvAreaConfirm;

    private List<AreaEntity> areaEntities;
    private List<AreaEntity> areaEntityList;
    private LanguageAreaAdapter areaAdapter;
    private LanguageAreaAdapter languageAdapter;
    private int currentArea;
    private int currentLanguage;
    private String language;

    public static void startLanguageActivity(Context context, Fragment fragment, int requestCode) {
        Intent intent = new Intent(context, LanguageAndAreaActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_language_and_area);
        ViewInjectUtils.inject(this);
        initView();
        loadData();
    }

    private void initView() {
        setLeftTitle(getString(R.string.language_and_area));
        mBtnTitleLeft.setImageResource(R.drawable.close_white_icon);
        language = ConfigUtils.getPreLanguage(this);

        lvAreaList.setOnItemClickListener((parent, view, position, id) -> {
            if (areaEntities != null && areaEntities.size() > 0) {
                AreaEntity areaEntity = areaEntities.get(position);
                if (!areaEntity.isSelected()) {
                    //上一个选择取消
                    areaEntities.get(currentArea).setSelected(false);
                    //修改当前地区
                    currentArea = position;
                    //选择新的地区
                    areaEntity.setSelected(true);
                    if (areaAdapter != null) {
                        areaAdapter.notifyDataSetChanged();
                    }
                    tvAreaCurrent.setText(getString(R.string.language_current_area, areaEntity.getName()));
                }
            }
        });
        lvLanguageList.setOnItemClickListener((parent, view, position, id) -> {
            if (areaEntityList != null && areaEntityList.size() > 0) {
                AreaEntity areaEntity = areaEntityList.get(position);
                if (!areaEntity.isSelected()) {
                    areaEntityList.get(currentLanguage).setSelected(false);
                    currentLanguage = position;
                    areaEntity.setSelected(true);
                    if (languageAdapter != null) {
                        languageAdapter.notifyDataSetChanged();
                    }
                    tvLanguageCurrent.setText(getString(R.string.language_current_language, areaEntity.getName()));
                }
            }
        });
        tvAreaConfirm.setOnClickListener(v -> {
            if (areaEntities != null && areaEntities.size() > currentArea) {
                AreaEntity areaEntity = areaEntities.get(currentArea);
                //地区存id
                ConfigUtils.setPreArea(this, areaEntity.getId());
                UserCenter.setSiteName(this, areaEntity.getName());
                UserCenter.setSiteId(this, areaEntity.getId());
            }
            if (areaEntityList != null && areaEntityList.size() > currentLanguage) {
                AreaEntity areaEntity = areaEntityList.get(currentLanguage);

                if (language == null) {
                    saveLanguage(areaEntity.getLanguageCode());
                } else {
                    if (!language.equals(areaEntity.getLanguageCode())) {
                        saveLanguage(areaEntity.getLanguageCode());
                    }
                }
            }
            if (!this.isFinishing()) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void saveLanguage(String languageCode) {
        ConfigUtils.setPreLanguage(this, languageCode);
        ExitManager.instance.exit();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void loadData() {
        showProgressDialog();
        requestHttpData(Constants.Urls.URL_POST_AREA, CommonConstant.REQUEST_NET_ONE, FProtocol.HttpMethod.POST, null);

        //因为在部分手机上有bug，所以在这里创建adapter并添加假数据，等加载完成之后再清除
        areaEntities = new ArrayList<>();
        areaEntities.add(new AreaEntity("", ""));
        areaAdapter = new LanguageAreaAdapter(this, areaEntities);
        lvAreaList.setAdapter(areaAdapter);

        //初始化语言
        areaEntityList = new ArrayList<>();
        areaEntityList.add(new AreaEntity("简体中文", CommonConstant.LANGUAGE_SIMPLIFIED_CHINESE));
        areaEntityList.add(new AreaEntity("繁体中文", CommonConstant.LANGUAGE_TRADITIONAL_CHINESE));
        areaEntityList.add(new AreaEntity("韩文", CommonConstant.LANGUAGE_KOREAN));
        areaEntityList.add(new AreaEntity("日文", CommonConstant.LANGUAGE_JAPANESE));
        areaEntityList.add(new AreaEntity("马来文", CommonConstant.LANGUAGE_MALAY_MALAYSIA));
        areaEntityList.add(new AreaEntity("俄文", CommonConstant.LANGUAGE_RUSSIAN));
        areaEntityList.add(new AreaEntity("英文", CommonConstant.LANGUAGE_ENGLISH));
        if (!TextUtils.isEmpty(language)) {
            for (int i = 0; i < areaEntityList.size(); i++) {
                if (language.equals(areaEntityList.get(i).getLanguageCode())) {
                    currentLanguage = i;
                }
            }
        }
        areaEntityList.get(currentLanguage).setSelected(true);
        tvLanguageCurrent.setText(getString(R.string.language_current_language, areaEntityList.get(currentLanguage).getName()));
        languageAdapter = new LanguageAreaAdapter(this, areaEntityList);
        lvLanguageList.setAdapter(languageAdapter);
    }

    @Override
    public void success(int requestCode, String data) {
        closeProgressDialog();
        areaAdapter.clear();//清除所有数据
        Entity result = Parsers.getResult(data);
        if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
            if (CommonConstant.REQUEST_NET_ONE == requestCode) {
                String preArea = ConfigUtils.getPreArea(this);//地区id
                areaEntities = Parsers.getAreaList(data);//新的数据源
                if (areaEntities != null && areaEntities.size() > 0) {
                    if (!TextUtils.isEmpty(preArea)) {
                        for (int i = 0; i < areaEntities.size(); i++) {
                            if (preArea.equals(areaEntities.get(i).getId())) {
                                currentArea = i;
                            }
                        }
                    }
                    tvAreaCurrent.setText(getString(R.string.language_current_area, areaEntities.get(currentArea).getName()));
                    areaEntities.get(currentArea).setSelected(true);//设置默认选中，根据本地配置变化
                    areaAdapter.addDatas(areaEntities);//添加所有数据
                }
            }
        } else {
            ToastUtil.shortShow(this, result.getResultMsg());
        }
    }

    @Override
    public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
        closeProgressDialog();
        areaAdapter.clear();//清除所有数据
    }
}
