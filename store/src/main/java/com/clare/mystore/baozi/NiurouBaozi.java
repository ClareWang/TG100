package com.clare.mystore.baozi;

import com.clare.mystore.baozi.base.*;

public class NiurouBaozi extends Baozi
{
	
	public NiurouBaozi()
	{
		this.type=Type.NIUROU;
		this.num=1;
	}
	public NiurouBaozi(int num)
	{
		this.type=Type.NIUROU;
		this.num=num;
	}

	public NiurouBaozi(Taste taste)
	{
		this.type=Type.NIUROU;
		this.taste=taste;
		this.num=1;
	}
	public NiurouBaozi(Taste taste,int num)
	{
		this.type=Type.NIUROU;
		this.taste=taste;
		this.num=num;
	}
}