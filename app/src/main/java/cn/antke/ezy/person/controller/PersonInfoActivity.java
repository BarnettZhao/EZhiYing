package cn.antke.ezy.person.controller;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.BitmapUtil;
import com.common.utils.DeviceUtil;
import com.common.utils.FileUtil;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.util.IdentityHashMap;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.main.controller.MainActivity;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.UserEntity;
import cn.antke.ezy.person.adapter.SelectPhotoAdapter;
import cn.antke.ezy.utils.ImageUtils;
import cn.antke.ezy.utils.PermissionUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_FROM;
import static cn.antke.ezy.common.CommonConstant.FROM_ACT_THREE;
import static cn.antke.ezy.common.CommonConstant.FROM_ACT_TWO;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_FOUR;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_THREE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_TWO;
import static cn.antke.ezy.common.CommonConstant.REQUEST_CAMERA_PERMISSION_CODE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_STORAGE_PERMISSION_CODE;
import static com.common.utils.BitmapUtil.IMAGE_CACHE_DIR;

/**
 * Created by zhaoweiwei on 2017/5/4.
 * 个人信息
 */

public class PersonInfoActivity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.personinfo_avatar_rl)
    private RelativeLayout avatarRl;
    @ViewInject(R.id.personinfo_avatar)
    private SimpleDraweeView avatar;
    @ViewInject(R.id.personinfo_usernumber)
    private TextView userNumber;
    @ViewInject(R.id.personinfo_realname)
    private TextView realName;
    @ViewInject(R.id.personinfo_nickname)
    private EditText nickNameEt;
    @ViewInject(R.id.personinfo_qr_code)
    private TextView qrCode;
    @ViewInject(R.id.personinfo_age)
    private EditText ageEt;
    @ViewInject(R.id.personinfo_constellation_rl)
    private RelativeLayout constellationRl;
    @ViewInject(R.id.personinfo_constellation)
    private TextView constellation;
    @ViewInject(R.id.personinfo_phone)
    private TextView phone;
    @ViewInject(R.id.personinfo_receiver_address_ll)
    private LinearLayout receiverAddressLl;
    @ViewInject(R.id.personinfo_receiver_address)
    private TextView receiverAddress;
    @ViewInject(R.id.personinfo_finished)
    private TextView finish;

    private Uri cameraUri;
    private String path;
    private String avatarPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_person_personinfo);
        ViewInjectUtils.inject(this);
        initView();
    }

    private void initView() {
        int from = getIntent().getIntExtra(EXTRA_FROM, FROM_ACT_THREE);
        if (FROM_ACT_THREE == from) {
            setLeftTitle(getString(R.string.person_personinfo));
        } else if (FROM_ACT_TWO == from) {
            setCenterTitleAndLeftText(getString(R.string.person_personinfo));
            mTxtLeft.setVisibility(View.GONE);
            setRightText(getString(R.string.jump));
            rightText.setOnClickListener(this);
        }

        ImageUtils.setSmallImg(avatar, UserCenter.getHeadPic(this));
        userNumber.setText(UserCenter.getUserCode(this));
        realName.setText(UserCenter.getUserName(this));
        phone.setText(UserCenter.getPhone(this));
        nickNameEt.setText(UserCenter.getNickName(this));
        ageEt.setText(UserCenter.getAge(this));
        if (!StringUtil.isEmpty(UserCenter.getDefaultAddress(this)) && !"nullnullnullnull".equals(UserCenter.getDefaultAddress(this))) {
            receiverAddress.setText(UserCenter.getDefaultAddress(this));
        } else {
            receiverAddress.setText("");
        }

        avatarRl.setOnClickListener(this);
        qrCode.setOnClickListener(this);
        constellationRl.setOnClickListener(this);
        receiverAddressLl.setOnClickListener(this);
        finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rigth_text:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.personinfo_avatar_rl:
                showHeadDialog();
                break;
            case R.id.personinfo_qr_code:
                startActivity(new Intent(this, MyQrCodeActivity.class));
                break;
            case R.id.personinfo_constellation_rl:
                startActivityForResult(new Intent(this, LogisticCompanyActivity.class), REQUEST_ACT_THREE);
                break;
            case R.id.personinfo_receiver_address_ll:
                startActivityForResult(new Intent(this, AddressListActivity.class), REQUEST_ACT_FOUR);
                break;
            case R.id.personinfo_finished:
                String nickName = nickNameEt.getText().toString();
                String age = ageEt.getText().toString();
                showProgressDialog();
                IdentityHashMap<String, String> params = new IdentityHashMap<>();
                params.put("nickName", nickName);
                params.put("age", age);
                params.put("head", avatarPic);
                requestHttpData(Constants.Urls.URL_POST_PERSONINFO_COMMIT, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
                break;
        }
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        UserEntity userEntity = Parsers.getUserInfo(data);
        UserCenter.savaUserInfo(this, userEntity);
        finish();
    }

    private void showHeadDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setAdapter(new SelectPhotoAdapter(this), (dialog, which) -> {
            if (0 == which) {
                if (PermissionUtils.isGetPermission(PersonInfoActivity.this, Manifest.permission.CAMERA)) {
                    openCamera();
                } else {
                    PermissionUtils.secondRequest(PersonInfoActivity.this, REQUEST_CAMERA_PERMISSION_CODE, Manifest.permission.CAMERA);
                }
            } else {
                if (PermissionUtils.isGetPermission(PersonInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    openPhotos();
                } else {
                    PermissionUtils.secondRequest(PersonInfoActivity.this, REQUEST_STORAGE_PERMISSION_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
            dialog.dismiss();
        });
        AlertDialog alertDialog1 = builder1.create();
        alertDialog1.setCanceledOnTouchOutside(true);
        alertDialog1.show();
    }

    private void openCamera() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = FileUtil.getDiskCacheFile(PersonInfoActivity.this, IMAGE_CACHE_DIR);
        file = new File(file.getPath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg");
        path = file.getPath();
        cameraUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(openCameraIntent, REQUEST_ACT_ONE);
    }

    private void openPhotos() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_ACT_TWO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && Manifest.permission.CAMERA.equals(permissions[0]) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    ToastUtil.shortShow(this, getString(R.string.personinfo_permission_photo_failed));
                }
                break;
            case REQUEST_STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0 && Manifest.permission.CAMERA.equals(permissions[0]) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openPhotos();
                } else {
                    ToastUtil.shortShow(this, getString(R.string.personinfo_permission_album_failed));
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case REQUEST_ACT_ONE://相机
                    DraweeController controller1 = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(ImageRequestBuilder.newBuilderWithSource(cameraUri)
                                    .setResizeOptions(new ResizeOptions(DeviceUtil.getWidth(this), DeviceUtil.getHeight(this)))
                                    .build())
                            .build();
                    avatar.setController(controller1);
                    avatarPic = BitmapUtil.bitmapToString(path, 60, 60);
                    break;
                case REQUEST_ACT_TWO://相册
                    Uri uri = data.getData();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(ImageRequestBuilder.newBuilderWithSource(uri)
                                    .setResizeOptions(new ResizeOptions(DeviceUtil.getWidth(this), DeviceUtil.getHeight(this)))
                                    .build())
                            .build();
                    String photoPath = getRealPathFromURI(uri);
                    avatar.setController(controller);
                    avatarPic = BitmapUtil.bitmapToString(photoPath, 60, 60);
                    break;
                case REQUEST_ACT_THREE://获取星座
                    String constellationResult = data.getStringExtra("constellation");
                    constellation.setText(constellationResult);
                    break;
                case REQUEST_ACT_FOUR://获取地址
                    String defaultAddress = data.getStringExtra("defaultAddress");
                    receiverAddress.setText(defaultAddress);
                    break;
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

//	//存储进SD卡
//	public void saveFile(Bitmap bm, String fileName) throws Exception {
//		File dirFile = new File(fileName);
////检测图片是否存在
//		if (dirFile.exists()) {
//			dirFile.delete();  //删除原图片
//		}
//		File myCaptureFile = new File(fileName);
//		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
////100表示不进行压缩，70表示压缩率为30%
//		bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//		bos.flush();
//		bos.close();
//	}
}
