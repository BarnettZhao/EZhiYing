package cn.antke.ezy.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.utils.StringUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import cn.antke.ezy.R;
import cn.antke.ezy.person.controller.IntegralPwdActivity;
import cn.antke.ezy.widget.PasswordInputView;

/**
 * Created by zhaoweiwei on 2017/5/4.
 * 弹窗
 */

public class DialogUtils {

    private static AlertDialog dialog;

    public static void showRecommendInfoDialog(Context context, String number, String name, String phone, View.OnClickListener okClickListener, View.OnClickListener cancelClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_recommender_info, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        TextView recommendNumber = (TextView) view.findViewById(R.id.recommend_number);
        TextView recommendName = (TextView) view.findViewById(R.id.recommend_name);
        TextView recommendPhone = (TextView) view.findViewById(R.id.recommend_phone);
        TextView btnOk = (TextView) view.findViewById(R.id.btn_ok);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        recommendNumber.setText(number);
        recommendName.setText(name);
        recommendPhone.setText(phone);
        btnOk.setOnClickListener(okClickListener);
        btnCancel.setOnClickListener(cancelClickListener);
        dialog.setCancelable(false);
        dialog.show();
        setAlertDialogWidth(context);
    }

    /*
    * content 和contentSsb一个只能填一个*/
    public static void showTwoBtnDialog(Context context, String title, String content, SpannableStringBuilder contentSsb, View.OnClickListener okClickListener, View.OnClickListener cancelClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_two_button, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        TextView titleTv = (TextView) view.findViewById(R.id.dialog_title);
        TextView contentTv = (TextView) view.findViewById(R.id.dialog_content);
        TextView btnOk = (TextView) view.findViewById(R.id.btn_ok);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);

        titleTv.setText(title);
        if (!StringUtil.isEmpty(content)) {
            contentTv.setText(content);
        }
        if (contentSsb != null) {
            contentTv.setText(contentSsb);
        }
        btnOk.setOnClickListener(okClickListener);
        btnCancel.setOnClickListener(cancelClickListener);
        dialog.setCancelable(true);
        dialog.show();
        setAlertDialogWidth(context);
    }

    public static void showOneBtnDialog(Context context, String title, String content, String btn, View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_one_button, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        TextView titleTv = (TextView) view.findViewById(R.id.dialog_onebtn_title);
        TextView contentTv = (TextView) view.findViewById(R.id.dialog_onebtn_content);
        TextView btnTv = (TextView) view.findViewById(R.id.dialog_onebtn_btn);

        titleTv.setText(title);
        contentTv.setText(content);
        btnTv.setText(btn);

        btnTv.setOnClickListener(onClickListener);

        dialog.setCancelable(true);
        dialog.show();
        setAlertDialogWidth(context);
    }

    public static void showFriendDialog(Context context, String avatarUrl, String name, String gapNum, String friendNum) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_friend, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        SimpleDraweeView avatar = (SimpleDraweeView) view.findViewById(R.id.dialog_friend_avatar);
        TextView nameTv = (TextView) view.findViewById(R.id.dialog_friend_name);
        TextView gapTv = (TextView) view.findViewById(R.id.dialog_friend_gap);
        TextView friendTv = (TextView) view.findViewById(R.id.dialog_friend_friend);

        ImageUtils.setSmallImg(avatar, avatarUrl);
        nameTv.setText(name);
        gapTv.setText(gapNum);
        friendTv.setText(friendNum);
        dialog.setCancelable(true);
        dialog.show();
        setAlertDialogWidth(context);
    }

    public static void showPwdInputDialog(Context context, DialogEtClickListenner dialogEtClickListenner, View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_password, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        ImageView closeView = (ImageView) view.findViewById(R.id.dialog_pwd_close);
        PasswordInputView pwdInputView = (PasswordInputView) view.findViewById(R.id.dialog_pwd_pwd);
        TextView setBtn = (TextView) view.findViewById(R.id.dialog_pwd_set);
        TextView confirmBtn = (TextView) view.findViewById(R.id.dialog_pwd_confirm);
        TextView forgetBtn = (TextView) view.findViewById(R.id.dialog_pwd_forget);

        closeView.setOnClickListener(onClickListener);

        setBtn.setOnClickListener(v -> context.startActivity(new Intent(context, IntegralPwdActivity.class)));

        forgetBtn.setOnClickListener(v -> context.startActivity(new Intent(context, IntegralPwdActivity.class)));

        confirmBtn.setOnClickListener(v -> dialogEtClickListenner.onClick(v, pwdInputView));

        dialog.setCancelable(false);
        dialog.show();
        setAlertDialogWidth(context);
    }

    public interface DialogEtClickListenner {
        void onClick(View v, EditText editText);
    }

    public static void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private static void setAlertDialogWidth(Context context) {
        if (dialog.getWindow() != null) {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = CommonTools.dp2px(context, 270f);
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(params);
        }
    }
}
