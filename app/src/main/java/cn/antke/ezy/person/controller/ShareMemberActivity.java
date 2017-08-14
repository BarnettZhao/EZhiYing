package cn.antke.ezy.person.controller;

import android.os.Bundle;
import android.widget.ListView;

import com.common.viewinject.annotation.ViewInject;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.network.entities.ShareMemberEntity;
import cn.antke.ezy.utils.ViewInjectUtils;
import cn.antke.ezy.widget.SideBar;

/**
 * Created by zhaoweiwei on 2017/5/16.
 * 分享会员
 */

public class ShareMemberActivity extends ToolBarActivity {
	@ViewInject(R.id.share_member_list)
	private ListView memberList;
	@ViewInject(R.id.share_member_sidebar)
	private SideBar memberSideBar;

	private List<ShareMemberEntity> entityList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_share_member);
		ViewInjectUtils.inject(this);
		setLeftTitle(getString(R.string.person_share_member));
//		initData();

		memberSideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack(){

			@Override
			public void onSelectStr(int index, String selectStr) {
				for (int i = 0; i <entityList.size() ; i++) {
					if (selectStr.equalsIgnoreCase(entityList.get(i).getFirstLetter())){
						memberList.setSelection(i);
						return;
					}
				}
			}
		});
	}

//	private void initData() {
//		entityList = new ArrayList<>();
//		entityList.add(new ShareMemberEntity("亳州")); // 亳[bó]属于不常见的二级汉字
//		entityList.add(new ShareMemberEntity("大娃"));
//		entityList.add(new ShareMemberEntity("二娃"));
//		entityList.add(new ShareMemberEntity("三娃"));
//		entityList.add(new ShareMemberEntity("四娃"));
//		entityList.add(new ShareMemberEntity("五娃"));
//		entityList.add(new ShareMemberEntity("六娃"));
//		entityList.add(new ShareMemberEntity("七娃"));
//		entityList.add(new ShareMemberEntity("喜羊羊"));
//		entityList.add(new ShareMemberEntity("美羊羊"));
//		entityList.add(new ShareMemberEntity("懒羊羊"));
//		entityList.add(new ShareMemberEntity("沸羊羊"));
//		entityList.add(new ShareMemberEntity("暖羊羊"));
//		entityList.add(new ShareMemberEntity("慢羊羊"));
//		entityList.add(new ShareMemberEntity("灰太狼"));
//		entityList.add(new ShareMemberEntity("红太狼"));
//		entityList.add(new ShareMemberEntity("孙悟空"));
//		entityList.add(new ShareMemberEntity("黑猫警长"));
//		entityList.add(new ShareMemberEntity("舒克"));
//		entityList.add(new ShareMemberEntity("贝塔"));
//		entityList.add(new ShareMemberEntity("海尔"));
//		entityList.add(new ShareMemberEntity("阿凡提"));
//		entityList.add(new ShareMemberEntity("邋遢大王"));
//		entityList.add(new ShareMemberEntity("哪吒"));
//		entityList.add(new ShareMemberEntity("没头脑"));
//		entityList.add(new ShareMemberEntity("不高兴"));
//		entityList.add(new ShareMemberEntity("蓝皮鼠"));
//		entityList.add(new ShareMemberEntity("大脸猫"));
//		entityList.add(new ShareMemberEntity("大头儿子"));
//		entityList.add(new ShareMemberEntity("小头爸爸"));
//		entityList.add(new ShareMemberEntity("蓝猫"));
//		entityList.add(new ShareMemberEntity("淘气"));
//		entityList.add(new ShareMemberEntity("叶峰"));
//		entityList.add(new ShareMemberEntity("楚天歌"));
//		entityList.add(new ShareMemberEntity("江流儿"));
//		entityList.add(new ShareMemberEntity("Tom"));
//		entityList.add(new ShareMemberEntity("Jerry"));
//		entityList.add(new ShareMemberEntity("12345"));
//		entityList.add(new ShareMemberEntity("54321"));
//		entityList.add(new ShareMemberEntity("_(:з」∠)_"));
//		entityList.add(new ShareMemberEntity("……%￥#￥%#"));
//		Collections.sort(entityList); // 对entityList进行排序，需要让ShareMemberEntity实现Comparable接口重写compareTo方法
////		ShareMemberAdapter adapter = new ShareMemberAdapter(this, entityList,this);
////		memberList.setAdapter(adapter);
//	}
}
