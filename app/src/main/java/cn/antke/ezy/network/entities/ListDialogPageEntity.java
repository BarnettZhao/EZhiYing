package cn.antke.ezy.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/5/24.
 */

public class ListDialogPageEntity implements Parcelable{
	private List<ListDialogEntity> entities;

	protected ListDialogPageEntity(Parcel in) {
		entities = in.createTypedArrayList(ListDialogEntity.CREATOR);
	}

	public ListDialogPageEntity(List<ListDialogEntity> entities) {
		this.entities = entities;
	}

	public ListDialogPageEntity() {
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(entities);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ListDialogPageEntity> CREATOR = new Creator<ListDialogPageEntity>() {
		@Override
		public ListDialogPageEntity createFromParcel(Parcel in) {
			return new ListDialogPageEntity(in);
		}

		@Override
		public ListDialogPageEntity[] newArray(int size) {
			return new ListDialogPageEntity[size];
		}
	};

	public List<ListDialogEntity> getEntities() {
		return entities;
	}

	public void setEntities(List<ListDialogEntity> entities) {
		this.entities = entities;
	}
}
