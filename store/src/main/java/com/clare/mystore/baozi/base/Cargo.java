package com.clare.mystore.baozi.base;

import java.util.ArrayList;
import java.util.List;

public class Cargo 
{

	private List<Baozi> baoziList;
	private Status status;

	public Cargo()
	{
		setStatus(Status.NULL);
		baoziList = new ArrayList<Baozi>();
	}
	
	public Status getStatus() 
	{
		return status;
	}

	public void setStatus(Status status) 
	{
		this.status = status;
	}

	public void addBaozi(Type type,int num)
	{
		for(int i=0;i<num;i++){
			Baozi baozi=new Baozi(type);
			this.baoziList.add(baozi);
		}
	}

	public List<Baozi> getBaoziList() 
	{
		return baoziList;
	}

}
