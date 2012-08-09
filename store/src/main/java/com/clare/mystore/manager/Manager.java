package com.clare.mystore.manager;


import com.clare.mystore.store.base.*;
import java.lang.Thread;

public class Manager implements Runnable
{
	private	int [] num;
	private int total;
	private Store [] store;
	Thread my = new Thread(this,"manager");
		
	public Manager(Store [] store) 
	{
		this.store=store;
		this.num=new int [store.length];
		total=0;
	}
	
	public void work() 
	{
		my.start();
		for(Store s:store)
		{
			s.addObserver(this);
		}
	}
	
	public void stop()
	{
		System.exit(0);
	}
	
	@Override
	public void run() 
	{		
		int temp=0;
		boolean flag=false;
		while(true)
		{
			for(int i=0;i<store.length;i++)
			{
				num[i]=store[i].getNum();
			}
			total=num[0];
			for(int i=1;i<store.length;i++)
			{
				total+=num[i];
			}
			
			if(total-temp*10>=10)
			{
				temp++;
				flag=true;
			}
			if(flag)
			{
				System.out.println("#########################################");
				System.out.println("#                                       #");
				System.out.println("# Manager: congratulations!             #");
				System.out.println("# The total number of baozi is: "+total+"\t#");
				System.out.println("#                                       #");
				System.out.println("#########################################");
				flag=false;
			}
		}
	}
	
	public void notifyManager(int num)
	{
		System.out.println("Current sale: " + num);
	}
}