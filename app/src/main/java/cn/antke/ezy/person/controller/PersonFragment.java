package cn.antke.ezy.person.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.IdentityHashMap;

import cn.antke.ezy.R;
import cn.antke.ezy.base.BaseFragment;
import cn.antke.ezy.login.controller.LoginActivity;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.PersonCenterEntity;
import cn.antke.ezy.utils.DialogUtils;
import cn.antke.ezy.utils.ExitManager;
import cn.antke.ezy.utils.ImageUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_FROM;
import static cn.antke.ezy.common.CommonConstant.EXTRA_ID;
import static cn.antke.ezy.common.CommonConstant.EXTRA_TYPE;
import static cn.antke.ezy.common.CommonConstant.FROM_ACT_ONE;
import static cn.antke.ezy.common.CommonConstant.FROM_ACT_THREE;
import static cn.antke.ezy.common.CommonConstant.FROM_ACT_TWO;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_ALL;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_DELIVED;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_DELIVING;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_FINISHED;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_PAYING;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_REFUND;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;

/**
 * Created by liuzhichao on 2017/4/27.
 * 个人中心
 */
public class PersonFragment extends BaseFragment implements View.OnClickListener {
    @ViewInject(R.id.person_setting)
    private TextView setting;
    @ViewInject(R.id.person_avatar)
    private SimpleDraweeView avatar;
    @ViewInject(R.id.person_username)
    private TextView username;
    @ViewInject(R.id.person_useable_integral_ll)
    private TextView useableIntegralLl;
    @ViewInject(R.id.person_good_friend_ll)
    private LinearLayout goodFriendLl;
    @ViewInject(R.id.person_good_friend)
    private TextView goodFriend;
    @ViewInject(R.id.person_all_order)
    private RelativeLayout allOrder;
    @ViewInject(R.id.person_order_paying)
    private FrameLayout orderShop;
    @ViewInject(R.id.person_order_deliving)
    private FrameLayout orderCharge;
    @ViewInject(R.id.person_order_delivred)
    private FrameLayout orderGive;
    @ViewInject(R.id.person_order_finished)
    private FrameLayout integralTransfer;
    @ViewInject(R.id.person_order_refund)
    private FrameLayout tradingHall;
    @ViewInject(R.id.person_personal_store)
    private TextView personalStore;
    @ViewInject(R.id.person_voucher_center)
    private TextView voucherCenter;
    @ViewInject(R.id.person_my_message)
    private RelativeLayout myMessage;
    @ViewInject(R.id.person_my_message_num)
    private TextView myMessageNum;
    @ViewInject(R.id.person_share_member)
    private TextView shareMember;
    @ViewInject(R.id.person_consumer_service_bind)
    private TextView counsumerServiceBind;
    @ViewInject(R.id.person_consumer_service)
    private TextView counsumerService;
    @ViewInject(R.id.person_transfer_integral)
    private TextView transferIntegral;
    @ViewInject(R.id.person_transaction_password)
    private TextView transferPassword;
    @ViewInject(R.id.person_bond_exchange)
    private TextView bondExchange;
    @ViewInject(R.id.person_lottery_inquiry)
    private TextView lotteryInquiry;
    @ViewInject(R.id.person_notice)
    private TextView notice;

    private TextView payingDot, delivingDot, delivedDot, finishDot, refundDot;
    private String storeId;
    private String userType;
    private String storeStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_person, null);
        ViewInjectUtils.inject(this, view);
        initDot();
        initClickListener();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        if (UserCenter.isLogin(getActivity())) {
            ImageUtils.setSmallImg(avatar, UserCenter.getHeadPic(getActivity()));
            if (!StringUtil.isEmpty(UserCenter.getUserName(getActivity()))) {
                username.setText(UserCenter.getUserName(getActivity()));
            } else {
                String phone = UserCenter.getPhone(getActivity());
                if (!StringUtil.isEmpty(phone)) {
                    phone = UserCenter.getPhone(getActivity()).substring(0, 3) + "****" + phone.substring(7);
                    username.setText(phone);
                }
            }
            loadData();
        } else {
            setNoLoginData();
        }
    }

    private void setNoLoginData() {
        avatar.setImageResource(R.drawable.default_avatar_bg);
        username.setText(getString(R.string.person_click_login));
        setDotText("", payingDot);
        setDotText("", delivingDot);
        setDotText("", delivedDot);
        setDotText("", finishDot);
        setDotText("", refundDot);
        myMessageNum.setVisibility(View.GONE);

        goodFriend.setText("");
    }

    private void initDot() {
        payingDot = (TextView) orderShop.findViewById(R.id.order_red_dot);
        delivingDot = (TextView) orderCharge.findViewById(R.id.order_red_dot);
        delivedDot = (TextView) orderGive.findViewById(R.id.order_red_dot);
        finishDot = (TextView) integralTransfer.findViewById(R.id.order_red_dot);
        refundDot = (TextView) tradingHall.findViewById(R.id.order_red_dot);
    }

    private void loadData() {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("loginName", "");
        params.put("is_open", "1");
        requestHttpData(Constants.Urls.URL_POST_PERSON_CENTER, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    private void initClickListener() {
        setting.setOnClickListener(this);
        avatar.setOnClickListener(this);
        username.setOnClickListener(this);
        useableIntegralLl.setOnClickListener(this);
        goodFriendLl.setOnClickListener(this);
        allOrder.setOnClickListener(this);
        orderShop.setOnClickListener(this);
        orderCharge.setOnClickListener(this);
        orderGive.setOnClickListener(this);
        integralTransfer.setOnClickListener(this);
        tradingHall.setOnClickListener(this);
        personalStore.setOnClickListener(this);
        voucherCenter.setOnClickListener(this);
        myMessage.setOnClickListener(this);
        shareMember.setOnClickListener(this);
        counsumerServiceBind.setOnClickListener(this);
        counsumerService.setOnClickListener(this);
        transferIntegral.setOnClickListener(this);
        transferPassword.setOnClickListener(this);
        bondExchange.setOnClickListener(this);
        lotteryInquiry.setOnClickListener(this);
        notice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (UserCenter.isLogin(getActivity())) {
            switch (v.getId()) {
                case R.id.person_setting:
                    //设置
                    DialogUtils.showTwoBtnDialog(getActivity(),
                            getString(R.string.login_logout_title),
                            getString(R.string.login_logout_content),
                            null,
                            v1 -> {
                                setNoLoginData();
                                UserCenter.cleanLoginInfo(getActivity());
                                DialogUtils.closeDialog();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                ExitManager.instance.exit();
                            },
                            v12 -> DialogUtils.closeDialog());
                    break;
                case R.id.person_avatar:
                case R.id.person_username:
                    //个人信息
                    startActivity(new Intent(getActivity(), PersonInfoActivity.class).putExtra(EXTRA_FROM, FROM_ACT_THREE));
                    break;
                case R.id.person_useable_integral_ll:
                    //可用积分
                    startActivity(new Intent(getActivity(), IntegralActivity.class));
                    break;
                case R.id.person_good_friend_ll:
                    //好友
                    startActivity(new Intent(getActivity(), GoodFriendActivity.class));
                    break;
                case R.id.person_all_order:
                    //全部订单
                    startActivity(new Intent(getActivity(), OrderListActivity.class).putExtra(EXTRA_TYPE, ORDERSTATE_ALL));
                    break;
                case R.id.person_order_paying:
                    //待支付订单
                    startActivity(new Intent(getActivity(), OrderListActivity.class).putExtra(EXTRA_TYPE, ORDERSTATE_PAYING));
                    break;
                case R.id.person_order_deliving:
                    //待发货订单
                    startActivity(new Intent(getActivity(), OrderListActivity.class).putExtra(EXTRA_TYPE, ORDERSTATE_DELIVING));
                    break;
                case R.id.person_order_delivred:
                    //待收货订单
                    startActivity(new Intent(getActivity(), OrderListActivity.class).putExtra(EXTRA_TYPE, ORDERSTATE_DELIVED));
                    break;
                case R.id.person_order_finished:
                    //完成订单
                    startActivity(new Intent(getActivity(), OrderListActivity.class).putExtra(EXTRA_TYPE, ORDERSTATE_FINISHED));
                    break;
                case R.id.person_order_refund:
                    //退款订单
                    startActivity(new Intent(getActivity(), OrderListActivity.class).putExtra(EXTRA_TYPE, ORDERSTATE_REFUND));
                    break;
                case R.id.person_personal_store:
                    //个人店铺 0 待审核 1 正常[审核成功] 2 锁定 3 拒绝审核

                    if (storeStatus != null && !StringUtil.isEmpty(storeStatus)) {
                        switch (storeStatus) {
                            case "0":
                                ToastUtil.shortShow(getActivity(), "店铺正在审核中");
                                break;
                            case "1":
                                if (!StringUtil.isEmpty(storeId)) {
                                    startActivity(new Intent(getActivity(), StoreMyStoreActivity.class).putExtra(EXTRA_ID, storeId));
                                }
                                break;
                            case "2":
                                ToastUtil.shortShow(getActivity(), "店铺已被锁定");
                                break;
                            case "3":
                                ToastUtil.shortShow(getActivity(), "店铺拒绝审核");
                                break;
                        }
                    } else {
                        startActivity(new Intent(getActivity(), StoreApplyActivity.class));
                    }
                    break;
                case R.id.person_voucher_center:
                    //充值中心
                    startActivity(new Intent(getActivity(), ChargeCenterActivity.class));
                    break;
                case R.id.person_my_message:
                    //我的消息
                    startActivity(new Intent(getActivity(), MyMessageActivity.class));
                    break;
                case R.id.person_share_member:
                    //分享会员
                    startActivity(new Intent(getActivity(), ShareMemberActivity.class));
                    break;
                case R.id.person_consumer_service_bind:
                    //绑定消费服务中心
                    startActivity(new Intent(getActivity(), ConsumeServiceBindActivity.class));
                    break;
                case R.id.person_consumer_service:
                    //消费服务中心
                    if ("3".equals(userType)) {
                        startActivity(new Intent(getActivity(), ConsumerServiceActivity.class));
                    } else {
                        ProtocalActivity.startProtocalActivity(getActivity(), FROM_ACT_TWO);
                    }
                    break;
                case R.id.person_transfer_integral:
                    //转积分
                    startActivity(new Intent(getActivity(), TransferIntegralActivity.class));
                    break;
                case R.id.person_transaction_password:
                    //积分交易密码
                    startActivity(new Intent(getActivity(), IntegralPwdActivity.class));
                    break;
                case R.id.person_bond_exchange:
                    //债券兑换
                    startActivity(new Intent(getActivity(), BondExchangeActivity.class));
                    break;
                case R.id.person_lottery_inquiry:
                    //抽奖查询
                    startActivity(new Intent(getActivity(), LotteryInquiryActivity.class));
                    break;
                case R.id.person_notice:
                    //站内公告
                    startActivity(new Intent(getActivity(), MyMessageActivity.class).putExtra(EXTRA_FROM, FROM_ACT_ONE));
                    break;
            }
        } else {
            switch (v.getId()) {
                case R.id.person_setting:
                    //设置
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    break;
                case R.id.person_avatar:
                case R.id.person_username:
                    //个人信息
//					break;
                case R.id.person_useable_integral_ll:
                    //可用积分
//					break;
                case R.id.person_good_friend_ll:
                    //好友
//					break;
                case R.id.person_all_order:
                    //全部订单
//					break;
                case R.id.person_order_paying:
                    //购物订单
//					break;
                case R.id.person_order_deliving:
                    //充值订单
//					break;
                case R.id.person_order_delivred:
                    //赠送订单
//					break;
                case R.id.person_order_finished:
                    //积分互转
//					break;
                case R.id.person_order_refund:
                    //交易大厅
//					break;
                case R.id.person_personal_store:
                    //个人店铺
//					break;
                case R.id.person_voucher_center:
                    //充值中心
//					break;
                case R.id.person_my_message:
                    //我的消息
//					break;
                case R.id.person_share_member:
                    //分享会员
//					break;
                case R.id.person_consumer_service_bind:
                    //绑定消费服务中心
//					break;
                case R.id.person_consumer_service:
                    //消费服务中心
//					break;
                case R.id.person_transfer_integral:
                    //转积分
//					break;
                case R.id.person_transaction_password:
                    //积分交易密码
//					break;
                case R.id.person_bond_exchange:
                    //债券兑换
                case R.id.person_lottery_inquiry:
                    //抽奖查询
                case R.id.person_notice:
                    //站内公告
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    break;
            }
        }
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        PersonCenterEntity entity = Parsers.getOrderNum(data);
        if (entity != null) {
            storeId = entity.getStoreId();
            userType = entity.getUserType();
            storeStatus = entity.getStoreStatus();
            UserCenter.setStoreName(getActivity(), entity.getStoreName());
            UserCenter.setStorePic(getActivity(), entity.getStorePic());
            String payingNum = entity.getPayingNum();
            String delivingNum = entity.getDelivingNum();
            String delivedNum = entity.getDelivedNum();
//			String finishNum = entity.ge();
            String refundNum = entity.getRefundNum();
            setDotText(payingNum, payingDot);
            setDotText(delivingNum, delivingDot);
            setDotText(delivedNum, delivedDot);
//			setDotText(finishNum, finishDot);
            setDotText(refundNum, refundDot);

            String messageNum = entity.getMessageNum();
            if (StringUtil.isEmpty(messageNum) || messageNum.equals("0")) {
                myMessageNum.setVisibility(View.GONE);
            } else if (Integer.parseInt(messageNum) > 99) {
                myMessageNum.setVisibility(View.VISIBLE);
                myMessageNum.setText(getString(R.string.more_than_99));
            } else {
                myMessageNum.setVisibility(View.VISIBLE);
                myMessageNum.setText(messageNum);
            }

            goodFriend.setText(entity.getFriendNum());
        }
    }

    private void setDotText(String num, TextView view) {
        if ("0".equals(num) || StringUtil.isEmpty(num)) {
            view.setVisibility(View.GONE);
        } else if (Integer.parseInt(num) > 99) {
            view.setVisibility(View.VISIBLE);
            view.setText(getString(R.string.more_than_99));
        } else {
            view.setVisibility(View.VISIBLE);
            view.setText(num);
        }
    }
}
