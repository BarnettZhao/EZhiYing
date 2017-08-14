package cn.antke.ezy.person.controller;

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
import android.text.TextUtils;
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
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.network.entities.PersonalStoreEntity;
import cn.antke.ezy.person.adapter.SelectPhotoAdapter;
import cn.antke.ezy.utils.ImageUtils;
import cn.antke.ezy.utils.PermissionUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_TWO;
import static cn.antke.ezy.common.CommonConstant.REQUEST_CAMERA_PERMISSION_CODE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_STORAGE_PERMISSION_CODE;
import static com.common.utils.BitmapUtil.IMAGE_CACHE_DIR;

/**
 * Created by zhaoweiwei on 2017/5/12.
 * 店铺详情
 */
public class StoreDetailActivity extends ToolBarActivity implements View.OnClickListener {

	@ViewInject(R.id.store_detail_pic)
	private SimpleDraweeView storePic;
	@ViewInject(R.id.store_detail_add)
	private View storeAdd;
	@ViewInject(R.id.store_detail_name)
	private EditText storeName;
	@ViewInject(R.id.store_detail_phone)
	private EditText storePhone;
	@ViewInject(R.id.store_detail_area)
	private EditText storeArea;
	@ViewInject(R.id.store_detail_address)
	private EditText storeAddress;
	@ViewInject(R.id.store_detail_contact_name)
	private TextView storeDetailContactName;
	@ViewInject(R.id.store_detail_contact)
	private TextView storeDetailContact;
	@ViewInject(R.id.store_detail_confirm)
	private View storeDetailConfirm;

	private Uri cameraUri;
	private String path;
	private String newStorePic;

	public static void startStoreDetailActivity(Context context) {
		Intent intent = new Intent(context, StoreDetailActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_store_detail);
		ViewInjectUtils.inject(this);
		initView();
		loadData();
	}

	private void initView() {
		setLeftTitle(getString(R.string.store_detail));
		storeAdd.setOnClickListener(this);
		storeDetailConfirm.setOnClickListener(this);
	}

	private void loadData() {
		showProgressDialog();
		requestHttpData(Constants.Urls.URL_POST_MY_STORE, CommonConstant.REQUEST_NET_ONE, FProtocol.HttpMethod.POST, null);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity result = Parsers.getResult(data);
		if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			switch (requestCode) {
				case CommonConstant.REQUEST_NET_ONE:
					PersonalStoreEntity personalStore = Parsers.getPersonalStore(data);
					storeName.setText(personalStore.getName());
					storePhone.setText(personalStore.getContact());
					storeArea.setText(personalStore.getArea());
					storeAddress.setText(personalStore.getAddress());
					storeDetailContactName.setText(personalStore.getContactName());
					storeDetailContact.setText(personalStore.getContact());
					ImageUtils.setImgUrl(storePic, personalStore.getPicUrl());
					break;
				case CommonConstant.REQUEST_NET_TWO:
					ToastUtil.shortShow(this, "修改成功");
					break;
			}
		} else {
			ToastUtil.shortShow(this, result.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		super.mistake(requestCode, status, errorMessage);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.store_detail_add://添加图片
				showHeadDialog();
				break;
			case R.id.store_detail_confirm://确定修改
				String name = storeName.getText().toString();
				String phone = storePhone.getText().toString();
				String area = storeArea.getText().toString();
				String address = storeAddress.getText().toString();
				if (TextUtils.isEmpty(name)) {
					ToastUtil.shortShow(this, "店铺名称不能为空");
					return;
				}
				if (TextUtils.isEmpty(phone)) {
					ToastUtil.shortShow(this, "联系方式不能为空");
					return;
				}
				if (TextUtils.isEmpty(area)) {
					ToastUtil.shortShow(this, "所在地不能为空");
					return;
				}
				if (TextUtils.isEmpty(address)) {
					ToastUtil.shortShow(this, "地址不能为空");
					return;
				}
				showProgressDialog();
				IdentityHashMap<String, String> params = new IdentityHashMap<>();
				params.put("first_pic", newStorePic);
				params.put("store_name", name);
				params.put("contact", phone);
				params.put("area_name", area);
				params.put("address", address);
				requestHttpData(Constants.Urls.URL_POST_ALTER_STORE, CommonConstant.REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
				break;
		}
	}

	private void showHeadDialog() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setAdapter(new SelectPhotoAdapter(this), (dialog, which) -> {
			if (0 == which) {
				if (PermissionUtils.isGetPermission(StoreDetailActivity.this, Manifest.permission.CAMERA)) {
					openCamera();
				} else {
					PermissionUtils.secondRequest(StoreDetailActivity.this, REQUEST_CAMERA_PERMISSION_CODE, Manifest.permission.CAMERA);
				}
			} else {
				if (PermissionUtils.isGetPermission(StoreDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
					openPhotos();
				} else {
					PermissionUtils.secondRequest(StoreDetailActivity.this, REQUEST_STORAGE_PERMISSION_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
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
		File file = FileUtil.getDiskCacheFile(StoreDetailActivity.this, IMAGE_CACHE_DIR);
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
					storePic.setController(controller1);
					newStorePic = BitmapUtil.bitmapToString(path, 315, 200);
					break;
				case REQUEST_ACT_TWO:
					Uri uri = data.getData();
					DraweeController controller = Fresco.newDraweeControllerBuilder()
							.setImageRequest(ImageRequestBuilder.newBuilderWithSource(uri)
									.setResizeOptions(new ResizeOptions(DeviceUtil.getWidth(this), DeviceUtil.getHeight(this)))
									.build())
							.build();
					String photoPath = getRealPathFromURI(uri);
					storePic.setController(controller);
					newStorePic = BitmapUtil.bitmapToString(photoPath, 315, 200);
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
}
