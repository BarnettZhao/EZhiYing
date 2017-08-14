package cn.antke.ezy.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.common.network.FProtocol;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.LogisticCompanyEntity;
import cn.antke.ezy.network.entities.PagesEntity;
import cn.antke.ezy.person.adapter.ConstellationAdapter;

import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;

/**
 * Created by zhaoweiwei on 2017/5/6.
 * 星座
 */

public class LogisticCompanyActivity extends ToolBarActivity {

    private ListView constellationList;
    private List<LogisticCompanyEntity> entities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_person_constellation);
        setLeftTitle(getString(R.string.personinfo_logistics_company));
        constellationList = (ListView) findViewById(R.id.constellation_list);

        loadData();

        constellationList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = getIntent();
            intent.putExtra("name", entities.get(position).getLogisticName());
            intent.putExtra("id", entities.get(position).getLogisticId());
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void loadData() {
        showProgressDialog();
        requestHttpData(Constants.Urls.URL_POST_LOGISTICS, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, null);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        PagesEntity<LogisticCompanyEntity> pagesEntity = Parsers.getLogisticCompanys(data);
        if (pagesEntity != null) {
            entities = pagesEntity.getDatas();
            if (entities.size() > 0) {
                ConstellationAdapter adapter = new ConstellationAdapter(this, entities);
                constellationList.setAdapter(adapter);
            }
        }
    }
}
