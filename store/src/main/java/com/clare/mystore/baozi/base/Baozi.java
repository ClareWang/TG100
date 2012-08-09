package com.clare.mystore.baozi.base;

public class Baozi 
{
    protected Type type;
    protected Taste taste;
    protected int num;
    
        
    public Type getType() 
    {
    	return this.type;
    }
    public void setType(Type type) 
    {
    	this.type=type;
    }
    public Taste getTaste() 
    {
    	return this.taste;
    }
    public void setTaste(Taste taste) 
    {
    	this.taste=taste;
    }
	public int getNum() 
	{
		return num;
	}
	public void setNum(int num) 
	{
		this.num = num;
	}
	public Baozi(Type type) 
	{
		super();
		this.type = type;
		this.num = 1;
	}
	public Baozi(Type type, int num) 
	{
		super();
		this.type = type;
		this.num = num;
	}
	public Baozi() 
	{
		super();
	}
    
}

