package cn.antke.ezy.common;

/**
 * Created by liuzhichao on 2017/5/5.
 * 公共常量
 */
public interface CommonConstant {

	String EXTRA_FROM = "extra_from";
	String EXTRA_ID = "extra_id";
	String EXTRA_URL = "extra_url";
	String EXTRA_PIC_URL = "extra_pic_url";
	String EXTRA_TITLE = "extra_title";
	String EXTRA_TYPE = "extra_type";
	String EXTRA_ENTITY = "extra_entity";
	String EXTRA_PHONE = "extra_phone";
	String EXTRA_CODE = "extra_code";
	String EXTRA_NUM = "extra_num";
	String EXTRA_AMOUNT = "extra_amount";
	String REQUEST_NET_SUCCESS = "0";//网络请求成功
	String SEARCH_PRE_KEY_HISTORY = "search_pre_key_history";//搜索历史记录

	String PAGENUM = "current_page";
	String PAGESIZE = "page_size";
	String PAGE_SIZE_10 = "10";
	String PAGE_SIZE_20 = "20";

	//支付广播
	String ACTION_BROADCAST_PAY_RESULT = "broadcast_pay_result";

	//支付方式
	int TYPE_ALIPAY = 1;
	int TYPE_WXPAY = 2;

	//语言
	String LANGUAGE_SIMPLIFIED_CHINESE = "zh_CN";
	String LANGUAGE_TRADITIONAL_CHINESE = "zh_HK";
	String LANGUAGE_KOREAN = "ko";
	String LANGUAGE_JAPANESE = "ja";
	String LANGUAGE_RUSSIAN = "ru";
	String LANGUAGE_MALAY_MALAYSIA = "ms_MY";
	String LANGUAGE_ENGLISH = "en";

	//商品列表排序
	String ORDER_FILED_VOLUME = "sale_num";
	String ORDER_FILED_PRICE = "selling_price";
	String ORDER_TYPE_DESC = "desc";
	String ORDER_TYPE_ASC = "asc";

	//请求网络，请求码，必须要情况下可以追加自定义名称
	int REQUEST_NET_ONE = 1;
	int REQUEST_NET_TWO = 2;
	int REQUEST_NET_THREE = 3;
	int REQUEST_NET_FOUR = 4;
	int REQUEST_NET_FIVE = 5;
	int REQUEST_NET_SIX = 6;
	int REQUEST_NET_SEVEN = 7;
	int REQUEST_NET_EIGHT = 8;
	int REQUEST_NET_NINE = 9;
	int REQUEST_NET_TEN = 10;

	//使用startActivityForResult时的请求码
	int REQUEST_ACT_ONE = 1;
	int REQUEST_ACT_TWO = 2;
	int REQUEST_ACT_THREE = 3;
	int REQUEST_ACT_FOUR = 4;
	int REQUEST_ACT_FIVE = 5;

	//开店类型
	int TYPE_ENTERPRISE = 1;//企业
	int TYPE_BUSINESS = 2;//商家
	int TYPE_PERSONAL = 3;//个人
	int TYPE_PHYSICAL = 4;//实体

	//商品列表入口
	int FROM_HOME_PRODUCT = 1;
	int FROM_PLATE_PRODUCT = 2;
	int FROM_SPECIAL_PRODUCT = 3;
	int FROM_PERSONAL_PRODUCT = 4;

	//权限配置时
	int REQUEST_CAMERA_PERMISSION_CODE = 1;//相机
	int REQUEST_STORAGE_PERMISSION_CODE = 2;//内存

	//from
	int FROM_ACT_ONE = 1;
	int FROM_ACT_TWO = 2;
	int FROM_ACT_THREE = 3;
	int FROM_ACT_FOUR = 4;
	int FROM_ACT_FIVE = 5;

	//支付密码
	int FROM_RESET = 1;
	int FROM_BIND = 2;
	int FROM_PERSON = 3;

	//收货地址列表
	int FROM_CONFIRM_ORDER = 1;

	//选择规格
	int FROM_PRODUCT_DETAIL = 1;
	int FROM_SHOP_CAR_LIST = 2;
	int FROM_DRAW_DETAIL = 3;

	//上传图片地址
	String FRONT_PATH = "/sdcard/front.jpg";
	String BACK_PATH = "/sdcard/back.jpg";
	String AVATAR_PATH = "/sdcard/back.jpg";

	//编辑地址类型
	int TYPE_ADD = 1;//新增
	int TYPE_EDIT = 2;//编辑

	//订单状态：0 全部 1 待支付 2 已支付 3 已发货 4 已完成 5 取消 6、申请中；7、退款中；8、已退款；9、拒绝；10、取消
	int ORDERSTATE_ALL = 0;//全部
	int ORDERSTATE_PAYING = 1;//待支付
	int ORDERSTATE_DELIVING = 2;//待发货
	int ORDERSTATE_DELIVED = 3;//待收货
	int ORDERSTATE_FINISHED = 4;//完成
	int ORDERSTATE_CANCEL = 5;//取消
	int ORDERSTATE_REFUND = 6;//退款


	int TYPE_0 = 0;
	int TYPE_1 = 1;
	int TYPE_2 = 2;
	int TYPE_3 = 3;
	int TYPE_4 = 4;
	int TYPE_5 = 5;
	int TYPE_6 = 6;
	int TYPE_7 = 7;
}
