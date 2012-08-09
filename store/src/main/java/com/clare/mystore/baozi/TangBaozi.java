package com.clare.mystore.baozi;

import com.clare.mystore.baozi.base.*;

public class TangBaozi extends Baozi
{
	
	public TangBaozi()
	{
		this.type=Type.TANG;
		this.num=1;
	}
	public TangBaozi(int num)
	{
		this.type=Type.TANG;
		this.num=num;
	}

	public TangBaozi(Taste taste)
	{
		this.type=Type.TANG;
		this.taste=taste;
		this.num=1;
	}
	public TangBaozi(Taste taste,int num)
	{
		this.type=Type.TANG;
		this.taste=taste;
		this.num=num;
	}
}