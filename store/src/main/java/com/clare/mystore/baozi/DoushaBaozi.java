package com.clare.mystore.baozi;

import com.clare.mystore.baozi.base.*;

public class DoushaBaozi extends Baozi
{
	
	public DoushaBaozi()
	{
		this.type=Type.DOUSHA;
		this.num=1;
	}
	public DoushaBaozi(int num)
	{
		this.type=Type.DOUSHA;
		this.num=num;
	}

	public DoushaBaozi(Taste taste)
	{
		this.type=Type.DOUSHA;
		this.taste=taste;
		this.num=1;
	}
	
	public DoushaBaozi(Taste taste,int num)
	{
		this.type=Type.DOUSHA;
		this.taste=taste;
		this.num=num;
	}
}