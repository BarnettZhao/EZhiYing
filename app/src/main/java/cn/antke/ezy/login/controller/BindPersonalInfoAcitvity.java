package cn.antke.ezy.login.controller;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.BitmapUtil;
import com.common.utils.DeviceUtil;
import com.common.utils.FileUtil;
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
import cn.antke.ezy.person.adapter.SelectPhotoAdapter;
import cn.antke.ezy.person.controller.PersonInfoActivity;
import cn.antke.ezy.utils.PermissionUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_FROM;
import static cn.antke.ezy.common.CommonConstant.FROM_ACT_TWO;
import static cn.antke.ezy.common.CommonConstant.FROM_BIND;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_TWO;
import static cn.antke.ezy.common.CommonConstant.REQUEST_CAMERA_PERMISSION_CODE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_STORAGE_PERMISSION_CODE;
import static com.common.utils.BitmapUtil.IMAGE_CACHE_DIR;

/**
 * Created by zhaoweiwei on 2017/5/4.
 * 绑定个人信息
 */
public class BindPersonalInfoAcitvity extends ToolBarActivity implements View.OnClickListener {

    @ViewInject(R.id.bindinfo_name)
    private EditText nameEt;
    @ViewInject(R.id.bindinfo_phone)
    private EditText phoneEt;
    @ViewInject(R.id.bindinfo_identify_number)
    private TextView identifyNumberEt;
    @ViewInject(R.id.bindinfo_identify_front)
    private SimpleDraweeView identifyFront;
    @ViewInject(R.id.bindinfo_identify_back)
    private SimpleDraweeView identifyBack;
    @ViewInject(R.id.bindinfo_confirm)
    private TextView confirm;

    private Uri cameraUri;
    private int currentPic;
    private String path;
    private String frontPic, backPic;

    public static void startBindPersonalInfoAcitvity(Context context) {
        Intent intent = new Intent(context, BindPersonalInfoAcitvity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bind_personal_info);
        ViewInjectUtils.inject(this);
        initView();
    }

    private void initView() {
        setLeftTitle(getString(R.string.bindinfo_personal_info));
        setRightText(getString(R.string.jump));

        rightText.setOnClickListener(this);
        identifyFront.setOnClickListener(this);
        identifyBack.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rigth_text:
                startActivity(new Intent(this, PersonInfoActivity.class).putExtra(EXTRA_FROM, FROM_ACT_TWO));
                finish();
                break;
            case R.id.bindinfo_identify_front:
                showHeadDialog();
                currentPic = 1;
                break;
            case R.id.bindinfo_identify_back:
                showHeadDialog();
                currentPic = 2;
                break;
            case R.id.bindinfo_confirm:
                bindInfo();
                break;
        }
    }

    private void bindInfo() {
        String name = nameEt.getText().toString();
        String idNumber = identifyNumberEt.getText().toString();
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("name", name);
        params.put("certCode", idNumber);
        params.put("front", frontPic);
        params.put("rear", backPic);
        requestHttpData(Constants.Urls.URL_POST_BIND_INFO, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                String userName = Parsers.getUserName(data);
                UserCenter.setUserName(this, userName);
                if (UserCenter.isSetPwd(this)) {
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    SetPassWordActivity.startSetPassWordActivity(this, FROM_BIND);
                }
                finish();
                break;
        }
    }

    private void showHeadDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setAdapter(new SelectPhotoAdapter(this), (dialog, which) -> {
            if (0 == which) {
                if (PermissionUtils.isGetPermission(BindPersonalInfoAcitvity.this, Manifest.permission.CAMERA)) {
                    openCamera();
                } else {
                    PermissionUtils.secondRequest(BindPersonalInfoAcitvity.this, REQUEST_CAMERA_PERMISSION_CODE, Manifest.permission.CAMERA);
                }
            } else {
                if (PermissionUtils.isGetPermission(BindPersonalInfoAcitvity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    openPhotos();
                } else {
                    PermissionUtils.secondRequest(BindPersonalInfoAcitvity.this, REQUEST_STORAGE_PERMISSION_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
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
        File file = FileUtil.getDiskCacheFile(BindPersonalInfoAcitvity.this, IMAGE_CACHE_DIR);
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
                case REQUEST_ACT_ONE:
                    DraweeController controller1 = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(ImageRequestBuilder.newBuilderWithSource(cameraUri)
                                    .setResizeOptions(new ResizeOptions(DeviceUtil.getWidth(this), DeviceUtil.getHeight(this)))
                                    .build())
                            .build();
                    if (1 == currentPic) {
                        identifyFront.setController(controller1);
                        frontPic = BitmapUtil.bitmapToString(path, 315, 200);
                    } else {
                        identifyBack.setController(controller1);
                        backPic = BitmapUtil.bitmapToString(path, 315, 200);
                    }
                    break;
                case REQUEST_ACT_TWO:
                    Uri uri = data.getData();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(ImageRequestBuilder.newBuilderWithSource(uri)
                                    .setResizeOptions(new ResizeOptions(DeviceUtil.getWidth(this), DeviceUtil.getHeight(this)))
                                    .build())
                            .build();
                    String photoPath = getRealPathFromURI(uri);
                    if (1 == currentPic) {
                        identifyFront.setController(controller);
                        frontPic = BitmapUtil.bitmapToString(photoPath, 315, 200);
                    } else {
                        identifyBack.setController(controller);
                        backPic = BitmapUtil.bitmapToString(photoPath, 315, 200);
                    }
                    break;
            }
        }
    }

//	private void compressPic(DraweeController controller, String path) {
//		Bitmap bitmap = BitmapUtil.getSmallBitmap(path, 600, 370);
//
//		try {
//			if (1 == currentPic) {
//				identifyFront.setController(controller);
//				saveFile(bitmap, FRONT_PATH);
//			} else {
//				identifyBack.setController(controller);
//				saveFile(bitmap, BACK_PATH);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

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
//
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
