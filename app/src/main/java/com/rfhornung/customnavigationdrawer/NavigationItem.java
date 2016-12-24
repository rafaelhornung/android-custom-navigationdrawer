package com.rfhornung.customnavigationdrawer;

import android.os.Parcel;
import android.os.Parcelable;

public class NavigationItem implements Parcelable
{
	private int id;

	private int count;

	private int iconId;

	private String title;

	public NavigationItem(int id, String title, int iconId, int count)
	{
		this.setId(id);
		this.setTitle(title);
		this.setCount(count);
		this.setIconId(iconId);
	}

	private NavigationItem()
	{

	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(this.getId());
		dest.writeInt(this.getCount());
		dest.writeInt(this.getIconId());
		dest.writeString(this.getTitle());
	}

	private NavigationItem(Parcel in)
	{
		this.setId(in.readInt());
		this.setCount(in.readInt());
		this.setIconId(in.readInt());
		this.setTitle(in.readString());
	}

	public final Parcelable.Creator<NavigationItem> CREATOR = new Parcelable.Creator<NavigationItem>()
	{
		@Override
		public NavigationItem createFromParcel(Parcel source)
		{
			return new NavigationItem(source);
		}

		@Override
		public NavigationItem[] newArray(int size)
		{
			return new NavigationItem[size];
		}
	};

	public int getId()
	{
		return id;
	}

	private void setId(int id)
	{
		this.id = id;
	}

	public int getCount()
	{
		return count;
	}

	private void setCount(int count)
	{
		this.count = count;
	}

	public int getIconId()
	{
		return iconId;
	}

	private void setIconId(int iconId)
	{
		this.iconId = iconId;
	}

	public String getTitle()
	{
		return title;
	}

	private void setTitle(String title)
	{
		this.title = title;
	}
}
