package cn.antke.ezy.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zww on 2017/6/17.
 */

public class ListDialogEntity implements Parcelable {
private String itemContent;
private boolean isChecked;

    protected ListDialogEntity(Parcel in) {
        itemContent = in.readString();
        isChecked = in.readByte() != 0;
    }

    public ListDialogEntity(String itemContent, boolean isChecked) {
        this.itemContent = itemContent;
        this.isChecked = isChecked;
    }

    public static final Creator<ListDialogEntity> CREATOR = new Creator<ListDialogEntity>() {
        @Override
        public ListDialogEntity createFromParcel(Parcel in) {
            return new ListDialogEntity(in);
        }

        @Override
        public ListDialogEntity[] newArray(int size) {
            return new ListDialogEntity[size];
        }
    };

    public String getItemContent(){
        return itemContent;
        }

public void setItemContent(String itemContent){
        this.itemContent=itemContent;
        }

public boolean isChecked(){
        return isChecked;
        }

public void setChecked(boolean checked){
        isChecked=checked;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemContent);
        parcel.writeByte((byte) (isChecked ? 1 : 0));
    }
}
