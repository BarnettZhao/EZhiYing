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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.CategoryItemEntity;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.person.adapter.SelectPhotoAdapter;
import cn.antke.ezy.person.adapter.StoreCategoryAdapter;
import cn.antke.ezy.utils.CommonTools;
import cn.antke.ezy.utils.ExitManager;
import cn.antke.ezy.utils.InputMethodUtil;
import cn.antke.ezy.utils.PermissionUtils;
import cn.antke.ezy.utils.ViewInjectUtils;
import cn.antke.ezy.widget.dialogplus.DialogPlus;
import cn.antke.ezy.widget.dialogplus.GridHolder;

import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_ACT_TWO;
import static cn.antke.ezy.common.CommonConstant.REQUEST_CAMERA_PERMISSION_CODE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_STORAGE_PERMISSION_CODE;
import static com.common.utils.BitmapUtil.IMAGE_CACHE_DIR;

/**
 * Created by zhaoweiwei on 2017/5/11.
 * 店铺申请信息
 */
public class StoreApplyInfoActivity extends ToolBarActivity implements View.OnClickListener {

	@ViewInject(R.id.store_applyinfo_name)
	private EditText storeNameEt;
	@ViewInject(R.id.store_applyinfo_category_ll)
	private LinearLayout storeCategoryLl;
	@ViewInject(R.id.store_applyinfo_category)
	private TextView storeCategory;
	@ViewInject(R.id.store_applyinfo_phone)
	private EditText storePhoneEt;
	@ViewInject(R.id.store_applyinfo_area)
	private EditText storeAreaEt;
	@ViewInject(R.id.store_applyinfo_address)
	private EditText storeAddressEt;
	@ViewInject(R.id.ll_applyinfo_business_license)
	private View llApplyinfoBusinessLicense;
	@ViewInject(R.id.ll_applyinfo_organization_code)
	private View llApplyinfoOrganizationCode;
	@ViewInject(R.id.ll_applyinfo_account_permit)
	private View llApplyinfoAccountPermit;
	@ViewInject(R.id.ll_applyinfo_hygiene)
	private View llApplyinfoHygiene;
	@ViewInject(R.id.store_applyinfo_business_license)
	private SimpleDraweeView businessLicense;
	@ViewInject(R.id.store_applyinfo_organization_code)
	private SimpleDraweeView organizaitonCode;
	@ViewInject(R.id.store_applyinfo_account_permit)
	private SimpleDraweeView accountPermit;
	@ViewInject(R.id.store_applyinfo_idcard_front)
	private SimpleDraweeView idcardFront;
	@ViewInject(R.id.store_applyinfo_idcard_back)
	private SimpleDraweeView idcardBack;
	@ViewInject(R.id.store_applyinfo_hygiene)
	private SimpleDraweeView storeApplyinfoHygiene;
	@ViewInject(R.id.store_applyinfo_commit)
	private View storeApplyinfoCommit;

	private List<CategoryItemEntity> category;
	private String name;
	private String phone;
	private String area;
	private String address;
	private String categoryIds = "";
	private String categoryNames = "";
	private String typeValue;
	private Uri cameraUri;
	private int currentId;
	private String path;
	private String businessPic;
	private String organizationPic;
	private String accountPermitPic;
	private String idcardFrontPic;
	private String idcardBackPic;
	private String contactsFrontPic;
	private String contactsBackPic;
	private String hygienePic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_store_apply_info);
		ExitManager.instance.addApplyStoreActivity(this);
		ViewInjectUtils.inject(this);
		initView();
		loadData();
	}

	private void initView() {
		int type = getIntent().getIntExtra(CommonConstant.EXTRA_TYPE, CommonConstant.TYPE_PERSONAL);
		switch (type) {
			case CommonConstant.TYPE_PERSONAL:
				typeValue = "3";
				setLeftTitle(getString(R.string.store_personal));
				llApplyinfoBusinessLicense.setVisibility(View.GONE);
				llApplyinfoAccountPermit.setVisibility(View.GONE);
				llApplyinfoOrganizationCode.setVisibility(View.GONE);
				llApplyinfoHygiene.setVisibility(View.GONE);
				break;
			case CommonConstant.TYPE_ENTERPRISE:
				typeValue = "1";
				setLeftTitle(getString(R.string.store_company));
				break;
			case CommonConstant.TYPE_BUSINESS:
				typeValue = "2";
				setLeftTitle(getString(R.string.store_business));
				break;
			case CommonConstant.TYPE_PHYSICAL:
				typeValue = "4";
				setLeftTitle(getString(R.string.store_entity));
				llApplyinfoOrganizationCode.setVisibility(View.GONE);
				llApplyinfoAccountPermit.setVisibility(View.GONE);
				break;
		}

		storeCategoryLl.setOnClickListener(this);
		businessLicense.setOnClickListener(this);
		organizaitonCode.setOnClickListener(this);
		accountPermit.setOnClickListener(this);
		idcardFront.setOnClickListener(this);
		idcardBack.setOnClickListener(this);
		storeApplyinfoHygiene.setOnClickListener(this);
		storeApplyinfoCommit.setOnClickListener(this);
	}

	private void loadData() {
		showProgressDialog();
		requestHttpData(Constants.Urls.URL_POST_STORE_CATEGORY, CommonConstant.REQUEST_NET_ONE, FProtocol.HttpMethod.POST, null);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity result = Parsers.getResult(data);
		if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			switch (requestCode) {
				case CommonConstant.REQUEST_NET_ONE:
					category = Parsers.getCategory(data).getCategoryItemEntities();
					break;
				case CommonConstant.REQUEST_NET_TWO:
					ToastUtil.shortShow(this, "申请店铺信息提交成功，请耐心等待审核");
					ExitManager.instance.closeApplyStoreActivity();
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
			case R.id.store_applyinfo_category_ll://选择分类
				InputMethodUtil.closeInputMethod(this);
				if (category != null && category.size() > 0) {
					StoreCategoryAdapter adapter = new StoreCategoryAdapter(this, category);
					DialogPlus dialogPlus = DialogPlus.newDialog(this).setAdapter(adapter).setHeader(R.layout.layout_store_apply_header)
							.setGravity(Gravity.BOTTOM).setContentHolder(new GridHolder(4)).setOnItemClickListener((dialog, item, view, position) -> {
								CategoryItemEntity categoryItemEntity = category.get(position);
								categoryItemEntity.setChecked(!categoryItemEntity.isChecked());
								adapter.notifyDataSetChanged();
							}).setPadding(CommonTools.dp2px(this, 6), 0, CommonTools.dp2px(this, 6),0).create();
					dialogPlus.findViewById(R.id.tv_apply_header_cancel).setOnClickListener(v1 -> dialogPlus.dismiss());
					dialogPlus.findViewById(R.id.tv_apply_header_confirm).setOnClickListener(v12 -> {
						categoryIds = "";
						categoryNames = "";
						for (CategoryItemEntity categoryItemEntity : category) {
							if (categoryItemEntity.isChecked()) {
								categoryIds += (categoryItemEntity.getCategoryId() + ",");
								categoryNames += (categoryItemEntity.getCategoryName() + "/");
							}
						}
						if (TextUtils.isEmpty(categoryIds)) {
							ToastUtil.shortShow(this, "请选择分类");
							storeCategory.setText(categoryNames);
						} else {
							storeCategory.setText(categoryNames.substring(0, categoryNames.length() - 1));
							dialogPlus.dismiss();
						}
					});
					dialogPlus.show();
				}
				break;
			case R.id.store_applyinfo_business_license://营业执照
				showHeadDialog();
				currentId = R.id.store_applyinfo_business_license;
				break;
			case R.id.store_applyinfo_organization_code://组织代码证，非必填
				showHeadDialog();
				currentId = R.id.store_applyinfo_organization_code;
				break;
			case R.id.store_applyinfo_account_permit://开户许可证
				showHeadDialog();
				currentId = R.id.store_applyinfo_account_permit;
				break;
			case R.id.store_applyinfo_idcard_front://法人身份证正面
				showHeadDialog();
				currentId = R.id.store_applyinfo_idcard_front;
				break;
			case R.id.store_applyinfo_idcard_back://法人身份证反面
				showHeadDialog();
				currentId = R.id.store_applyinfo_idcard_back;
				break;
			case R.id.store_applyinfo_hygiene://卫生许可证，非个人且类型为食品时必填
				showHeadDialog();
				currentId = R.id.store_applyinfo_hygiene;
				break;
			case R.id.store_applyinfo_commit:
				name = storeNameEt.getText().toString().trim();
				phone = storePhoneEt.getText().toString().trim();
				area = storeAreaEt.getText().toString().trim();
				address = storeAddressEt.getText().toString().trim();
				if (TextUtils.isEmpty(name)) {
					ToastUtil.shortShow(this, "店铺名称不能为空");
					return;
				}
				if (TextUtils.isEmpty(categoryIds)) {
					ToastUtil.shortShow(this, "请选择店铺分类");
					return;
				}
//				if (TextUtils.isEmpty(phone)) {
//					ToastUtil.shortShow(this, "联系方式不能为空");
//					return;
//				}
				if (TextUtils.isEmpty(area)) {
					ToastUtil.shortShow(this, "所在地不能为空");
					return;
				}
				if (TextUtils.isEmpty(address)) {
					ToastUtil.shortShow(this, "详细地址不能为空");
					return;
				}
				applyStore();
				break;
		}
	}

	private void applyStore() {
		showProgressDialog();
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("store_type", typeValue);//1:企业店铺2：商家店铺3：个体店铺4:实体店铺
		params.put("store_name", name);
		params.put("store_category", categoryIds);
		params.put("phone", phone);
		params.put("home", area);
		params.put("address", address);
		params.put("business_license", businessPic);//营业执照
		params.put("organization_certificate", organizationPic);//组织机构代码证
		params.put("issuing_bank", accountPermitPic);//开户行许可证
		params.put("idCardZ", idcardFrontPic);//身份证正面
		params.put("idCardF", idcardBackPic);//身份证反面
		params.put("hygienic_license", hygienePic);//卫生许可
		requestHttpData(Constants.Urls.URL_POST_APPLY_STORE, CommonConstant.REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
	}

	private void showHeadDialog() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setAdapter(new SelectPhotoAdapter(this), (dialog, which) -> {
			if (0 == which) {
				if (PermissionUtils.isGetPermission(StoreApplyInfoActivity.this, Manifest.permission.CAMERA)) {
					openCamera();
				} else {
					PermissionUtils.secondRequest(StoreApplyInfoActivity.this, REQUEST_CAMERA_PERMISSION_CODE, Manifest.permission.CAMERA);
				}
			} else {
				if (PermissionUtils.isGetPermission(StoreApplyInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
					openPhotos();
				} else {
					PermissionUtils.secondRequest(StoreApplyInfoActivity.this, REQUEST_STORAGE_PERMISSION_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
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
		File file = FileUtil.getDiskCacheFile(StoreApplyInfoActivity.this, IMAGE_CACHE_DIR);
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
					updatePic(cameraUri, path);
					break;
				case REQUEST_ACT_TWO:
					Uri uri = data.getData();
					String photoPath = getRealPathFromURI(uri);
					updatePic(uri, photoPath);
					break;
			}
		}
	}

	private void updatePic(Uri uri, String path) {
		DraweeController controller = Fresco.newDraweeControllerBuilder()
				.setImageRequest(ImageRequestBuilder.newBuilderWithSource(uri)
						.setResizeOptions(new ResizeOptions(DeviceUtil.getWidth(this), DeviceUtil.getHeight(this)))
						.build())
				.build();
		switch (currentId) {
			case R.id.store_applyinfo_business_license://营业执照
				businessLicense.setController(controller);
				businessPic = BitmapUtil.bitmapToString(path, 315, 200);
				break;
			case R.id.store_applyinfo_organization_code://组织代码证，非必填
				organizaitonCode.setController(controller);
				organizationPic = BitmapUtil.bitmapToString(path, 315, 200);
				break;
			case R.id.store_applyinfo_account_permit://开户许可证
				accountPermit.setController(controller);
				accountPermitPic = BitmapUtil.bitmapToString(path, 315, 200);
				break;
			case R.id.store_applyinfo_idcard_front://法人身份证正面
				idcardFront.setController(controller);
				idcardFrontPic = BitmapUtil.bitmapToString(path, 315, 200);
				break;
			case R.id.store_applyinfo_idcard_back://法人身份证反面
				idcardBack.setController(controller);
				idcardBackPic = BitmapUtil.bitmapToString(path, 315, 200);
				break;
			case R.id.store_applyinfo_hygiene://卫生许可证，非个人且类型为食品时必填
				storeApplyinfoHygiene.setController(controller);
				hygienePic = BitmapUtil.bitmapToString(path, 315, 200);
				break;
		}
	}
}
