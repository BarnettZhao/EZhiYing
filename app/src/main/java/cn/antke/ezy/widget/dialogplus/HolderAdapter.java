package cn.antke.ezy.widget.dialogplus;

import android.widget.BaseAdapter;

/**
 * @author Orhan Obut
 */
public interface HolderAdapter extends Holder {

  void setAdapter(BaseAdapter adapter);

  void setOnItemClickListener(OnHolderListener listener);
}
