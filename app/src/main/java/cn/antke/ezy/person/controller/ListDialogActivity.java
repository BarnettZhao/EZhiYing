package cn.antke.ezy.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.BaseActivity;
import cn.antke.ezy.network.entities.ListDialogEntity;
import cn.antke.ezy.network.entities.ListDialogPageEntity;
import cn.antke.ezy.person.adapter.ListDialogAdapter;
import cn.antke.ezy.utils.CommonTools;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_ENTITY;

/**
 * Created by zhaoweiwei on 2017/2/27.
 */

public class ListDialogActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.order_cancel_cancel)
    private TextView orderCancel;
    @ViewInject(R.id.order_cancel_ok)
    private TextView orderOk;
    @ViewInject(R.id.order_cancel_list)
    private ListView orderCancelList;

    private String cancelReason;
    private ListDialogPageEntity pageEntity;
    private List<ListDialogEntity> entities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_person_refund_reason);
        ViewInjectUtils.inject(this);
        initStyle();
        Intent intent = getIntent();
        pageEntity = intent.getParcelableExtra(EXTRA_ENTITY);
        entities = pageEntity.getEntities();
        orderCancel.setOnClickListener(this);
        orderOk.setOnClickListener(this);
        setDatas();
    }

    private void setDatas() {
        ListDialogAdapter adapter = new ListDialogAdapter(this, entities);
        orderCancelList.setAdapter(adapter);
        orderCancelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (ListDialogEntity entity : entities) {
                    entity.setChecked(false);
                }
                entities.get(position).setChecked(true);
                adapter.notifyDataSetChanged();
                cancelReason = entities.get(position).getItemContent();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_cancel_cancel:
                finish();
                break;
            case R.id.order_cancel_ok:
                Intent intent = getIntent();
                intent.putExtra(EXTRA_ENTITY, cancelReason);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    private void initStyle() {
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams wl = window.getAttributes();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = CommonTools.dp2px(this, 284);
        this.onWindowAttributesChanged(wl);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.actionsheet_dialog_out);
    }
}
