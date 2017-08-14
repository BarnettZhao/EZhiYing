package cn.antke.ezy.person.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.viewinject.annotation.ViewInject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.BarcodeFormat;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.ShareEntity;
import cn.antke.ezy.utils.CommonShareUtil;
import cn.antke.ezy.utils.ImageUtils;
import cn.antke.ezy.utils.QRCodeUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_THREE;

/**
 * Created by zhaoweiwei on 2017/5/6.
 * 我的二维码
 */

public class MyQrCodeActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.myqrcode_avatar)
    private SimpleDraweeView avatar;
    @ViewInject(R.id.myqrcode_usercode)
    private TextView userCode;
    @ViewInject(R.id.myqrcode_username)
    private TextView userName;
    @ViewInject(R.id.myqrcode_qrcode)
    private SimpleDraweeView qrQrCode;

    private String qrCode;
    private String img;
    private String content;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_person_myqrcode);
        ViewInjectUtils.inject(this);
        setLeftTitle(getString(R.string.personinfo_qrcode));
        setRightTitle(R.drawable.qrcode_share);
        mBtnTitleRight.setOnClickListener(this);

        ImageUtils.setSmallImg(avatar, UserCenter.getHeadPic(this));
        userCode.setText(getString(R.string.register_usernumber_text_add, UserCenter.getUserCode(this)));
        userName.setText(UserCenter.getUserName(this));
        getQrcode();
    }

    private void getQrcode() {
        showProgressDialog();
        requestHttpData(Constants.Urls.URL_POST_GET_QRCODE, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, null);
    }

    @Override
    public void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        ShareEntity entity = Parsers.getShareInfo(data);
        if (entity != null) {
            title = entity.getTitle();
            content = entity.getDesc();
            img = entity.getImg();
            qrCode = entity.getUrl();
            int qrWidth = qrQrCode.getLayoutParams().width;
            int qrHeight = qrQrCode.getLayoutParams().height;
            //二维码
            try {
                QRCodeUtils.setQvImageView(qrQrCode, qrCode, BarcodeFormat.QR_CODE, qrWidth, qrHeight);
            } catch (OutOfMemoryError e) {
                System.gc();
                QRCodeUtils.setQvImageView(qrQrCode, qrCode, BarcodeFormat.QR_CODE, qrWidth, qrHeight);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_button:
                CommonShareUtil.share(MyQrCodeActivity.this, content, title, img, qrCode);
                break;
        }
    }
}
