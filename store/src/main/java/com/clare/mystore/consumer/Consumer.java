package com.clare.mystore.consumer;

import com.clare.mystore.store.base.Store;
import com.clare.mystore.baozi.base.Cargo;
import com.clare.mystore.baozi.base.Status;
import com.clare.mystore.baozi.base.Type;

public class Consumer implements Runnable
{
	
	private Cargo cargo;
	private Thread my;
	private String name;


	
	public Consumer(String name)
	{
		this.name=name;
		my = new Thread(this,this.name);
		this.cargo = new Cargo();
	}
	
   
    @Override
	public void run() 
    {
    	while(true)
    	{
    		if(cargo.getStatus()==Status.FINISHED)
    		{
    			System.out.println(this.name+": I have got my Baozi.");
    			cargo.setStatus(Status.OVER);
    			try 
    			{
					Thread.sleep(3000);
				} 
    			catch (InterruptedException e) 
    			{
					e.printStackTrace();
				}
    			break;
    		}
			else
			{
				try 
				{
					Thread.sleep(100);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}		
    	}
	}
    
    
    
    
    
    public void buyBaozi(Store store)
    {
    	System.out.println(this.name+": I will buy this baozi in "+store.getName()+"\n");
    	cargo.setStatus(Status.WAITING);
    	if(store.buy(cargo))
    	   	my.start();
    	else
    		System.out.println(this.name+": I can't buy baozi??!!\n");
    }
    
    public void addBaozi(Type type,int number)
    {
    	cargo.addBaozi(type, number);
    	System.out.println(this.name+": I am going to buy Baozi.");
    	System.out.println("Type:"+type+" Number:"+number+"\n");
    }

	public Cargo getCargo() 
	{
		return cargo;
	}

	public void setCargo(Cargo cargo) 
	{
		this.cargo = cargo;
	}
 
}
