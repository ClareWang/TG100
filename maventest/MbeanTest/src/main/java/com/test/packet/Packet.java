package com.test.packet;

import java.io.Serializable;


public class Packet implements Serializable{
	private static final long serialVersionUID = 1L;
	
	Type type;
	long id;
	String message;
	
	public Packet()
	{
		type=Type.EMPTY;
	}
	
	public Type GetType()
	{
		return this.type;	
	}
	public void SetType(Type type)
	{
		this.type=type;
	}
	public long GetId()
	{
		return this.id;	
	}
	public void SetId(long id)
	{
		this.id=id;
	}
	public String GetMessage()
	{
		return this.message;
	}
	public void SetMessage(String message)
	{
		this.message=message;
	}

	@Override
	public String toString() {
		return "Packet [type=" + type + ", id=" + id + ", message=" + message + "]";
	}
	
	
}
