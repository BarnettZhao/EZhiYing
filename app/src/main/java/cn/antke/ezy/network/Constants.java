package cn.antke.ezy.network;

/**
 * Created by jacktian on 15/8/19.
 * 接口地址
 */
public class Constants {

    public static class Urls {
        // 测试环境域名
//		public static String URL_BASE_DOMAIN = "http://139.129.110.82:9090";
//		public static String URL_BASE_DOMAIN = "http://192.168.1.90:8081";
//        public static String URL_BASE_DOMAIN = "http://192.168.1.121:8080";
//		public static String URL_BASE_DOMAIN = "http://192.168.1.92:8081";
        // 正式环境域名
		public static String URL_BASE_DOMAIN = "http://api.shitongmall.com";
//        public static String URL_BASE_DOMAIN = "http://221.122.116.5:8083";

        //个人店铺商品
        public static String URL_POST_STORE_PRODUCT = URL_BASE_DOMAIN + "/sto reGoodsList";
        //首页
        public static String URL_POST_HOME = URL_BASE_DOMAIN + "/indexInfo";
        //地区
        public static String URL_POST_AREA = URL_BASE_DOMAIN + "/selectSiteInfo";
        //抽奖列表
        public static String URL_POST_DRAW_LIST = URL_BASE_DOMAIN + "/prizeGoodsList";
        //抽奖详情
        public static String URL_POST_DRAW_DETAIL = URL_BASE_DOMAIN + "/prizeGoodsDetails";
        //抽奖
        public static String URL_POST_DRAW_JOIN = URL_BASE_DOMAIN + "/prize";
        //中奖查询
        public static String URL_POST_DRAW_QUERY = URL_BASE_DOMAIN + "/prizeResultList";
        //板块列表
        public static String URL_POST_PLATE_LIST = URL_BASE_DOMAIN + "/indexCategory";
        //首页-商品列表
        public static String URL_POST_HOME_PRODUCT_LIST = URL_BASE_DOMAIN + "/channelGoodsList";
        //易购,板块-商品列表
        public static String URL_POST_PLATE_PRODUCT_LIST = URL_BASE_DOMAIN + "/goodsList";
        //店铺详情
        public static String URL_POST_STORE_DETAIL = URL_BASE_DOMAIN + "/storeGoodsList";
        //商品详情
        public static String URL_POST_PRODUCT_DETAIL = URL_BASE_DOMAIN + "/goodsDetails";
        //购物车列表
        public static String URL_POST_SHOP_CAR_LIST = URL_BASE_DOMAIN + "/myShoppingCart";
        //添加购物车
        public static String URL_POST_ADD_SHOP_CAR = URL_BASE_DOMAIN + "/addShoppingCart";
        //删除购物车
        public static String URL_POST_DELETE_SHOP_CAR = URL_BASE_DOMAIN + "/deleteShoppingCart";
        //修改购物车
        public static String URL_POST_ALTER_SHOP_CAR = URL_BASE_DOMAIN + "/editShoppingCart";
        //购物车购买
        public static String URL_POST_SHOP_CAR_ORDER = URL_BASE_DOMAIN + "/confirmOrderInfo";
        //商品详情立即购买
        public static String URL_POST_BUY_NOW = URL_BASE_DOMAIN + "/payNow";
        //下单
        public static String URL_POST_CREATE_ORDER = URL_BASE_DOMAIN + "/createOrder";
        //积分支付
        public static String URL_POST_INTEGRAL_PAY = URL_BASE_DOMAIN + "/subtractIntegral";
        //支付
        public static String URL_POST_PAYMENT = URL_BASE_DOMAIN + "/payment";
        //热门搜索
        public static String URL_POST_HOT_KEY = URL_BASE_DOMAIN + "/hotSearch";
        //交易大厅缴费信息查询
        public static String URL_POST_DEAL_INFO = URL_BASE_DOMAIN + "/tradeHallfee";
        //交易大厅缴费
        public static String URL_POST_PAY_DEAL = URL_BASE_DOMAIN + "/tradeHallPayment";
        //交易大厅
        public static String URL_POST_DEAL_HALL = URL_BASE_DOMAIN + "/tradeHall";
        //交易大厅买入卖出信息
        public static String URL_POST_DEAL_CONDITION = URL_BASE_DOMAIN + "/integralBuySell";
        //买入积分
        public static String URL_POST_BUY_INTEGRAL = URL_BASE_DOMAIN + "/purchaseIntegral";
        //卖出积分
        public static String URL_POST_SELL_INTEGRAL = URL_BASE_DOMAIN + "/sellout";
        //卖出积分列表
        public static String URL_POST_SELL_INTEGRAL_LIST = URL_BASE_DOMAIN + "/selloutList";
        //撤销卖出
        public static String URL_POST_SELL_INTEGRAL_CANCEL = URL_BASE_DOMAIN + "/selloutRevoke";
        //易购专区
        public static String URL_POST_SPECIAL = URL_BASE_DOMAIN + "/easyBuyGoods";
        //申请开店分类
        public static String URL_POST_STORE_CATEGORY = URL_BASE_DOMAIN + "/selectCategory";
        //申请开店
        public static String URL_POST_APPLY_STORE = URL_BASE_DOMAIN + "/openIndividualStore";
        //注册
        public static String URL_POST_USER_REGISTER = URL_BASE_DOMAIN + "/saveUser";
        //登录
        public static String URL_POST_USER_LOGIN = URL_BASE_DOMAIN + "/login";
        //获取用户编号
        public static String URL_POST_USER_USER_CODE = URL_BASE_DOMAIN + "/usercode";
        //获取推荐人信息
        public static String URL_POST_USER_RECOMMEND_CODE = URL_BASE_DOMAIN + "/recommender";
        //获取验证码
        public static String URL_POST_SMS_CODE = URL_BASE_DOMAIN + "/identifyCode";
        //绑定用户信息
        public static String URL_POST_BIND_INFO = URL_BASE_DOMAIN + "/bindPersonalInfo";
        //忘记密码重置
        public static String URL_POST_RESET_PASSWORD = URL_BASE_DOMAIN + "/modifyPwd";
        //验证支付密码
        public static String URL_POST_VERIFY_PWD = URL_BASE_DOMAIN + "/verifyPayPassword";
        //分类
        public static String URL_POST_CATEGORY = URL_BASE_DOMAIN + "/categrayList";
        //个人中心
        public static String URL_POST_PERSON_CENTER = URL_BASE_DOMAIN + "/userCenter";
        //收货地址列表
        public static String URL_POST_ADDRESS_LIST = URL_BASE_DOMAIN + "/receivingList";
        //添加更新地址
        public static String URL_POST_ADD_EDIT_ADDRESS = URL_BASE_DOMAIN + "/receivingSaveOrUpdate";
        //删除收货地址
        public static String URL_POST_DELETE_ADDRESS = URL_BASE_DOMAIN + "/deleteReceiving";
        //提交个人信息
        public static String URL_POST_PERSONINFO_COMMIT = URL_BASE_DOMAIN + "/setUserCenter";
        //获取个人二维码
        public static String URL_POST_GET_QRCODE = URL_BASE_DOMAIN + "/queryShare";
        //获取个人开店信息
        public static String URL_POST_MY_STORE = URL_BASE_DOMAIN + "/selectStoreInfo";
        //我的消息(消息列表)
        public static String URL_POST_MESSAGE_LIST = URL_BASE_DOMAIN + "/messageList";
        //修改个人店铺信息
        public static String URL_POST_ALTER_STORE = URL_BASE_DOMAIN + "/updateStoreInfo";
        //设置积分交易密码
        public static String URL_POST_SET_PASSWORD = URL_BASE_DOMAIN + "/setIntegralTransPwd";
        //订单列表
        public static String URL_POST_ORDER_LIST = URL_BASE_DOMAIN + "/orderList";
        //退款列表
        public static String URL_POST_REFUND_LIST = URL_BASE_DOMAIN + "/queryRefundOrderList";
        //订单详情
        public static String URL_POST_ORDER_DETAIL = URL_BASE_DOMAIN + "/queryOrderInfo";
        //退款相关订单详情
        public static String URL_POST_REFUND_DETAIL = URL_BASE_DOMAIN + "/queryRefundOrder";
        //取消订单
        public static String URL_POST_ORDER_CANCEL = URL_BASE_DOMAIN + "/cancelOrder";
        //取消退款
        public static String URL_POST_REFUND_CANCEL = URL_BASE_DOMAIN + "/cancelRefundOrder";
        //退款申请
        public static String URL_POST_ORDER_REFUND = URL_BASE_DOMAIN + "/refundOrder";
        //删除订单
        public static String URL_POST_ORDER_DELETE = URL_BASE_DOMAIN + "/delOrder";
        //确认收货
        public static String URL_POST_GET_CONFIRM = URL_BASE_DOMAIN + "/confirmReceipt";
        //查询物流
        public static String URL_POST_QUERY_LOGISTICS = URL_BASE_DOMAIN + "/querylogistics";
        //消费服务中心条件查询
        public static String URL_POST_CONSUMER_CONDITION = URL_BASE_DOMAIN + "/serviceCenterCondition";
        //积分列表查询
        public static String URL_POST_QUERY_INTEGRAL = URL_BASE_DOMAIN + "/queryIntegralList";
        //积分互转
        public static String URL_POST_INTEGRAL_EXCHANGE = URL_BASE_DOMAIN + "/giveIntegral";
        //债券兑换获取债券数
        public static String URL_POST_BOND_NUM = URL_BASE_DOMAIN + "/queryBond";
        //债券兑换
        public static String URL_POST_BOND_EXCHANGE = URL_BASE_DOMAIN + "/exchangeBond";
        //债权回收
        public static String URL_POST_BOND_RECYCLE = URL_BASE_DOMAIN + "/bondRecyle";
        //好友列表
        public static String URL_POST_FRIEND_LIST = URL_BASE_DOMAIN + "/queryUserList";
        //绑定消费服务中心查询
        public static String URL_POST_BIND_CONSUMER_QUERY = URL_BASE_DOMAIN + "/userBindingInfo";
        //绑定解绑消费服务中心
        public static String URL_POST_BIND_CONSUMER = URL_BASE_DOMAIN + "/bindingServiceCenter";
        //消费服务中心开通
        public static String URL_POST_OPEN_CONSUMER = URL_BASE_DOMAIN + "/openServiceCenter";
        //消费服务中心首页查询
        public static String URL_POST_CONSUMER_QUERY = URL_BASE_DOMAIN + "/queryServiceCenter";
        //消费服务中心待激活列表
        public static String URL_POST_ACTIVATION_LIST = URL_BASE_DOMAIN + "/activationList";
        //消费服务中心激活
        public static String URL_POST_ACTIVATE = URL_BASE_DOMAIN + "/activation";
        //积分充值
        public static String URL_POST_INTEGRAL_CHARGE = URL_BASE_DOMAIN + "/integralRecharge";
        //所有积分数查询
        public static String URL_POST_INTEGRALNUM_QUERY = URL_BASE_DOMAIN + "/queryAllIntegralInfo";
        //赠送列表
        public static String URL_POST_GIVE_LIST = URL_BASE_DOMAIN + "/doublyIntegralList";
        //积分激活复投
        public static String URL_POST_INTEGRAL_RECAST = URL_BASE_DOMAIN + "/activationIntegral";
        //积分赠送倍增
        public static String URL_POST_INTEGRAL_DOUBLY = URL_BASE_DOMAIN + "/doublyIntegral";
        //个人中心抽奖查询（用户每期中奖情况）
        public static String URL_POST_PRIZE_QUERY = URL_BASE_DOMAIN + "/userPrizeList";
        //物流公司列表
        public static String URL_POST_LOGISTICS = URL_BASE_DOMAIN + "/queryLogisticsList";
        //发货
        public static String URL_POST_STORE_DELIVER = URL_BASE_DOMAIN + "/storeOrderInvoice";
        //验证验证码
        public static String URL_POST_VERIFY_SMSCODE = URL_BASE_DOMAIN + "/checkMobileCode";
        //确认退款
        public static String URL_POST_AGREE_REFUND = URL_BASE_DOMAIN + "/agreeRefund";
        //修改登录密码
        public static String URL_POST_CHANGE_LOGIN_PWD = URL_BASE_DOMAIN + "/modifyLoginPw";
        //登录查询协议是否选中
        public static String URL_POST_LOGIN_PROTOCAL = URL_BASE_DOMAIN + "/checkLoginDate";
        //站内公告
        public static String URL_POST_NOTICE = URL_BASE_DOMAIN + "/queryNoticeList";
    }
}
