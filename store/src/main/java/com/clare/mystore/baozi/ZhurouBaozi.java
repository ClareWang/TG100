package com.clare.mystore.baozi;

import com.clare.mystore.baozi.base.*;

public class ZhurouBaozi extends Baozi
{
	
	public ZhurouBaozi()
	{
		this.type=Type.ZHUROU;
	    this.num=1;
	}
	public ZhurouBaozi(int num)
	{
		this.type=Type.ZHUROU;
		this.num=num;
	}
	
	public ZhurouBaozi(Taste taste)
	{
		this.type=Type.ZHUROU;
		this.taste=taste;
		this.num=1;
	}
	public ZhurouBaozi(Taste taste,int num)
	{
		this.type=Type.ZHUROU;
		this.taste=taste;
		this.num=num;
	}
}
