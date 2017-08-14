package cn.antke.ezy.network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.antke.ezy.network.entities.ActivationPageEntity;
import cn.antke.ezy.network.entities.AddressPageEntity;
import cn.antke.ezy.network.entities.AreaEntity;
import cn.antke.ezy.network.entities.BondExchangeEntity;
import cn.antke.ezy.network.entities.CategoryEntity;
import cn.antke.ezy.network.entities.ConsumerBindQueryEntity;
import cn.antke.ezy.network.entities.ConsumerQueryEntity;
import cn.antke.ezy.network.entities.ConsumerServicePageEntity;
import cn.antke.ezy.network.entities.CreateOrderEntity;
import cn.antke.ezy.network.entities.DealBuySellEntity;
import cn.antke.ezy.network.entities.DealConditionEntity;
import cn.antke.ezy.network.entities.DealHallEntity;
import cn.antke.ezy.network.entities.DrawEntity;
import cn.antke.ezy.network.entities.DrawQueryEntity;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.network.entities.HomeEntity;
import cn.antke.ezy.network.entities.IntegralDetailEntity;
import cn.antke.ezy.network.entities.IntegralEntity;
import cn.antke.ezy.network.entities.IntegralGivePageEntity;
import cn.antke.ezy.network.entities.LogisticCompanyEntity;
import cn.antke.ezy.network.entities.LogisticEnity;
import cn.antke.ezy.network.entities.LotteryInquiryEntity;
import cn.antke.ezy.network.entities.MyMessageEntity;
import cn.antke.ezy.network.entities.OrderConfirmEntity;
import cn.antke.ezy.network.entities.OrderDetailEntity;
import cn.antke.ezy.network.entities.OrderListPageEntity;
import cn.antke.ezy.network.entities.PagesEntity;
import cn.antke.ezy.network.entities.PayEntity;
import cn.antke.ezy.network.entities.PersonCenterEntity;
import cn.antke.ezy.network.entities.PersonalStoreEntity;
import cn.antke.ezy.network.entities.PlateDetailEntity;
import cn.antke.ezy.network.entities.ProductDetailEntity;
import cn.antke.ezy.network.entities.ProductEntity;
import cn.antke.ezy.network.entities.RecommenderEntity;
import cn.antke.ezy.network.entities.SaleEntity;
import cn.antke.ezy.network.entities.ShareEntity;
import cn.antke.ezy.network.entities.ShareMemberPageEntity;
import cn.antke.ezy.network.entities.ShopCarEntity;
import cn.antke.ezy.network.entities.StoreDetailEntity;
import cn.antke.ezy.network.entities.UserEntity;
import cn.antke.ezy.network.json.GsonObjectDeserializer;

/**
 * Created by jacktian on 15/8/30.
 * json解析
 */
public class Parsers {

    private static Gson gson = GsonObjectDeserializer.produceGson();

    /**
     * @return 所有请求的公共部分，业务层的返回码和返回提示
     */
    public static Entity getResult(String data) {
        Entity result = gson.fromJson(data, new TypeToken<Entity>() {
        }.getType());
        if (result == null) {
            result = new Entity();
        }
        return result;
    }

    /**
     * @return 首页
     */
    public static HomeEntity getHomeEntity(String data) {
        return gson.fromJson(data, HomeEntity.class);
    }

    /**
     * @return 搜索热词
     */
    public static List<String> getHotWord(String data) {
        List<String> words = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            words = gson.fromJson(jsonObject.optString("words_list"), new TypeToken<List<String>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return words;
    }

    /**
     * @return 抽奖列表分页数据
     */
    public static PagesEntity<DrawEntity> getDrawPage(String data) {
        PagesEntity<DrawEntity> drawPages = new PagesEntity<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            List<DrawEntity> drawEntities = gson.fromJson(jsonObject.optString("goods_list"), new TypeToken<List<DrawEntity>>() {
            }.getType());
            drawPages.setDatas(drawEntities);
            drawPages.setTotalPage(jsonObject.optInt("total_page"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return drawPages;
    }

    /**
     * @return 抽奖详情
     */
    public static DrawEntity getDraw(String data) {
        return gson.fromJson(data, DrawEntity.class);
    }

    /**
     * @return 中奖查询
     */
    public static PagesEntity<DrawQueryEntity> getDrawQueryPage(String data) {
        PagesEntity<DrawQueryEntity> drawQueryPages = new PagesEntity<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            List<DrawQueryEntity> drawQueryEntities = gson.fromJson(jsonObject.optString("prizeWin_list"), new TypeToken<List<DrawQueryEntity>>() {
            }.getType());
            drawQueryPages.setDatas(drawQueryEntities);
            drawQueryPages.setTotalPage(jsonObject.optInt("total_page"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return drawQueryPages;
    }

    /**
     * @return 板块列表
     */
    public static List<PlateDetailEntity> getPlateDetailList(String data) {
        List<PlateDetailEntity> plateDetailEntities = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            plateDetailEntities = gson.fromJson(jsonObject.optString("categroy_list"), new TypeToken<List<PlateDetailEntity>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return plateDetailEntities;
    }

    /**
     * @return 店铺详情
     */
    public static StoreDetailEntity getStoreDetail(String data) {
        return gson.fromJson(data, StoreDetailEntity.class);
    }

    /**
     * @return 首页-分类-商品列表
     */
    public static PagesEntity<ProductDetailEntity> getProductPage(String data) {
        PagesEntity<ProductDetailEntity> productPages = new PagesEntity<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            String val = jsonObject.optString("goods_list");
            List<ProductDetailEntity> productEntities = gson.fromJson(val, new TypeToken<List<ProductEntity>>() {
            }.getType());
            productPages.setDatas(productEntities);
            productPages.setTotalPage(jsonObject.optInt("total_page"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return productPages;
    }

    /**
     * @return 商品详情
     */
    public static ProductDetailEntity getProductDetail(String data) {
        return gson.fromJson(data, ProductDetailEntity.class);
    }

    /**
     * @return 获取地区列表
     */
    public static List<AreaEntity> getAreaList(String data) {
        List<AreaEntity> areaEntities = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            areaEntities = gson.fromJson(jsonObject.optString("site_info"), new TypeToken<List<AreaEntity>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return areaEntities;
    }

    /**
     * @return 购物车列表
     */
    public static PagesEntity<ShopCarEntity> getShopCarPage(String data) {
        PagesEntity<ShopCarEntity> shopCarEntityPages = new PagesEntity<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            List<ShopCarEntity> shopCarEntities = gson.fromJson(jsonObject.optString("store_list"), new TypeToken<List<ShopCarEntity>>() {
            }.getType());
            shopCarEntityPages.setDatas(shopCarEntities);
            shopCarEntityPages.setTotalPage(jsonObject.optInt("total_page"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shopCarEntityPages;
    }

    /**
     * @return 确认订单页
     */
    public static OrderConfirmEntity getOrderConfirm(String data) {
        return gson.fromJson(data, OrderConfirmEntity.class);
    }

    /**
     * @return 创建订单
     */
    public static CreateOrderEntity getCreateOrder(String data) {
        return gson.fromJson(data, CreateOrderEntity.class);
    }

    /**
     * @return 交易大厅
     */
    public static DealHallEntity getDealHall(String data) {
        return gson.fromJson(data, DealHallEntity.class);
    }

    /**
     * @return 交易大厅缴费信息
     */
    public static DealConditionEntity getDealCondition(String data) {
        return gson.fromJson(data, DealConditionEntity.class);
    }

    /**
     * @return 支付
     */
    public static PayEntity getPay(String data) {
        return gson.fromJson(data, PayEntity.class);
    }

    /**
     * @return 积分购买卖出信息
     */
    public static DealBuySellEntity getDealBuySell(String data) {
        return gson.fromJson(data, DealBuySellEntity.class);
    }

    /**
     * @return 积分卖出列表
     */
    public static PagesEntity<SaleEntity> getPageSale(String data) {
        PagesEntity<SaleEntity> saleEntityPages = new PagesEntity<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            List<SaleEntity> saleEntities = gson.fromJson(jsonObject.optString("sellout_list"), new TypeToken<List<SaleEntity>>() {
            }.getType());
            saleEntityPages.setDatas(saleEntities);
            saleEntityPages.setTotalPage(jsonObject.optInt("total_page"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return saleEntityPages;
    }

    /**
     * 获取token
     **/
    public static UserEntity getUserInfo(String data) {
        return gson.fromJson(data, new TypeToken<UserEntity>() {
        }.getType());
    }

    /**
     * 获取用户编码
     **/
    public static String getUserCode(String data) {
        String userCode = null;
        if (data != null) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                userCode = jsonObject.optString("userCode");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return userCode;
    }

    /**
     * 获取推荐人信息
     **/
    public static RecommenderEntity getRecommender(String data) {
        return gson.fromJson(data, new TypeToken<RecommenderEntity>() {
        }.getType());
    }

    /**
     * @return 个人店铺信息
     */
    public static PersonalStoreEntity getPersonalStore(String data) {
        return gson.fromJson(data, PersonalStoreEntity.class);
    }

    /**
     * 分类
     **/
    public static CategoryEntity getCategory(String data) {
        return gson.fromJson(data, new TypeToken<CategoryEntity>() {
        }.getType());
    }

    /**
     * 个人中心
     **/
    public static PersonCenterEntity getOrderNum(String data) {
        return gson.fromJson(data, new TypeToken<PersonCenterEntity>() {
        }.getType());
    }

    /**
     * 地址列表
     **/
    public static AddressPageEntity getAddressPage(String data) {
        return gson.fromJson(data, AddressPageEntity.class);
    }

    /**
     * 获取二维码
     **/

    public static ShareEntity getShareInfo(String data) {
        return gson.fromJson(data, ShareEntity.class);
    }

    /**
     * 订单列表
     **/

    public static OrderListPageEntity getOrders(String data) {
        return gson.fromJson(data, new TypeToken<OrderListPageEntity>() {
        }.getType());
    }

    /**
     * 订单列表
     **/

    public static OrderDetailEntity getOrderDetail(String data) {
        return gson.fromJson(data, new TypeToken<OrderDetailEntity>() {
        }.getType());
    }

    /**
     * @return 积分查询
     */
    public static IntegralDetailEntity getIntegral(String data) {
        return gson.fromJson(data, new TypeToken<IntegralDetailEntity>() {
        }.getType());
    }


    /**
     * @return 债券兑换
     */
    public static BondExchangeEntity getBond(String data) {
        return gson.fromJson(data, new TypeToken<BondExchangeEntity>() {
        }.getType());
    }

    /**
     * @return 好友列表
     */
    public static ShareMemberPageEntity getMemberPage(String data) {
        return gson.fromJson(data, new TypeToken<ShareMemberPageEntity>() {
        }.getType());
    }

    /**
     * @return 绑定消费服务中心查询
     */
    public static ConsumerBindQueryEntity getBindInfo(String data) {
        return gson.fromJson(data, new TypeToken<ConsumerBindQueryEntity>() {
        }.getType());
    }

    /**
     * @return 消费服务中心条件查询
     */
    public static ConsumerQueryEntity getConsumerInfo(String data) {
        return gson.fromJson(data, new TypeToken<ConsumerQueryEntity>() {
        }.getType());
    }

    /**
     * @return 消费服务中心首页查询
     */
    public static ConsumerServicePageEntity getConsumerPage(String data) {
        return gson.fromJson(data, new TypeToken<ConsumerServicePageEntity>() {
        }.getType());
    }

    /**
     * @return 消费服务中心激活列表
     */
    public static ActivationPageEntity getActivationList(String data) {
        return gson.fromJson(data, new TypeToken<ActivationPageEntity>() {
        }.getType());
    }

    /**
     * @return 积分数查询
     */
    public static IntegralEntity getIntegralNum(String data) {
        return gson.fromJson(data, new TypeToken<IntegralEntity>() {
        }.getType());
    }

    /**
     * @return 积分数查询
     */
    public static IntegralGivePageEntity getGiveList(String data) {
        return gson.fromJson(data, new TypeToken<IntegralGivePageEntity>() {
        }.getType());
    }

    /**
     * @return 物流查询
     */
    public static LogisticEnity getLogistics(String data) {
        return gson.fromJson(data, new TypeToken<LogisticEnity>() {
        }.getType());
    }

    /**
     * @return 物流查询
     */
    public static String getUserName(String data) {
        String userCode = null;
        if (data != null) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                userCode = jsonObject.optString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return userCode;
    }


    /**
     * @return 个人每期抽奖查询
     */
    public static PagesEntity<LotteryInquiryEntity> getLottery(String data) {
        PagesEntity<LotteryInquiryEntity> lotteryPageEntity = new PagesEntity<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            List<LotteryInquiryEntity> lotteryInquiryEntities = gson.fromJson(jsonObject.optString("prizeWin_list"), new TypeToken<List<LotteryInquiryEntity>>() {
            }.getType());
            lotteryPageEntity.setDatas(lotteryInquiryEntities);
            lotteryPageEntity.setTotalPage(jsonObject.optInt("total_page"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lotteryPageEntity;
    }

    /**
     * @return 物流公司
     */
    public static PagesEntity<LogisticCompanyEntity> getLogisticCompanys(String data) {
        PagesEntity<LogisticCompanyEntity> logisticCompanyEntityPagesEntity = new PagesEntity<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            List<LogisticCompanyEntity> logisticCompanyEntities = gson.fromJson(jsonObject.optString("logistics_list"), new TypeToken<List<LogisticCompanyEntity>>() {
            }.getType());
            logisticCompanyEntityPagesEntity.setDatas(logisticCompanyEntities);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return logisticCompanyEntityPagesEntity;
    }

    /**
     * @return 消息
     */
    public static PagesEntity<MyMessageEntity> getMessages(String data) {
        PagesEntity<MyMessageEntity> myMessageEntityPagesEntity = new PagesEntity<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            List<MyMessageEntity> myMessageEntities = gson.fromJson(jsonObject.optString("message_list"), new TypeToken<List<MyMessageEntity>>() {
            }.getType());
            myMessageEntityPagesEntity.setDatas(myMessageEntities);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myMessageEntityPagesEntity;
    }

    /**
     * 登录协议是否选中
     **/
    public static String isCheckedProtocal(String data) {
        String status = null;//1：没看过协议 2：看过协议
        if (data != null) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                status = jsonObject.optString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return status;
    }
}