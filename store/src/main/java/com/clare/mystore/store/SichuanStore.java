package com.clare.mystore.store;

import java.util.ArrayList;

import com.clare.mystore.manager.Manager;
import com.clare.mystore.store.base.*;
import com.clare.mystore.baozi.*;
import com.clare.mystore.baozi.base.*;

public class SichuanStore extends Store implements Runnable
{
	
	private Thread cook=new Thread(this);
	
	public SichuanStore() 
	{
		this.name="sichuanStore";
		this.num=0;
		this.box=new SteamBox[MAX_BOX_NUMBER];
		this.baoziWaitList=new ArrayList<Baozi>();
		this.baoziFinishList=new ArrayList<Baozi>();
		observerGroup = new ArrayList<Manager>();
		for(int i=0;i<MAX_BOX_NUMBER;i++)
			box[i]=new SteamBox(this.name+"-box"+(i+1),baoziFinishList);
		this.cargoList=new ArrayList<Cargo>();
		cook.start();
	}
	
	
	@Override
	public boolean buy(Cargo cargo) 
	{
		if(cargo.getBaoziList().size()>30)
		{
			System.out.println("The most number of baozi you can buy is 30!\n");
			return false;
		}
		cargoList.add(cargo);
		if((cargo.getBaoziList().size()+this.num)/10-this.num/10>0)
		{
			this.num+=cargo.getBaoziList().size();
			notifyObserver();
		}
		else
		{
			this.num+=cargo.getBaoziList().size();
		}
		System.out.println(this.name+" has received the cargo, the number of baozi is "+cargo.getBaoziList().size());
		return true;
	}
	
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public int getNum()
	{
		return this.num;
	}

	@Override
	public void run() 
	{
		while(true)
		{
			if(cargoList.size()>0)
			{
				for(int i=0;i<cargoList.size();i++)
				{
					switch (cargoList.get(i).getStatus())
					{
					case WAITING:
						addBaoziIntoList(cargoList.get(i));
						cargoList.get(i).setStatus(Status.DOING);
						break;
					case DOING:
						cook();
						break;
					case FINISHED:
						break;
					case OVER:
						cargoList.remove(i);
						break;
					default:
						System.out.println("Wrong status! Set it into FINISHED.");
						cargoList.get(i).setStatus(Status.FINISHED);
					}
					try 
					{
						Thread.sleep(30);
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
				}
				
				for(int i=0;i<cargoList.size();i++)
				{
					if(cargoList.get(i).getStatus()==Status.DOING)
					{
						if(baoziFinishList.size()>=cargoList.get(i).getBaoziList().size())
						{
							for(int j=0;j<cargoList.get(i).getBaoziList().size();j++)
							{
								baoziFinishList.remove(0);
							}
							cargoList.get(i).setStatus(Status.FINISHED);
						}
						break;
					}
				}	
			}
			try 
			{
				Thread.sleep(500);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
			
		}
	}

	@Override
	public void addObserver(Manager manager) 
	{
		this.observerGroup.add(manager);
	}


	private void addBaoziIntoList(Cargo cargo) 
	{
		for(int i=0;i<cargo.getBaoziList().size();i++)
		{
			Baozi baozi;
			switch (cargo.getBaoziList().get(i).getType())
			{
			case DOUSHA:
				baozi=new DoushaBaozi(Taste.SPICY);
				break;
			case TANG:
				baozi=new TangBaozi(Taste.SPICY);
				break;
			case NIUROU:
				baozi=new NiurouBaozi(Taste.SPICY);
				break;
			case ZHUROU:
				baozi=new ZhurouBaozi(Taste.SPICY);
				break;
			default:
				System.out.println("choose the wrong type!");
				baozi=new Baozi();
			}
			baoziWaitList.add(baozi);
		}
		
	}
	
	
	public void cook()
	{
		for(int i=0;i<box.length;i++)
		{
			if(box[i].checkStatus())
			{
				while(box[i].getBaoziList().size()<=10)
				{
					if(baoziWaitList.isEmpty())
						break;
					box[i].add(baoziWaitList.get(0));
					System.out.println("Type: " +baoziWaitList.get(0).getType()+" into "+this.name+"-box"+(i+1));
					baoziWaitList.remove(0);		
				}
			}
			if(box[i].getBaoziList().size()>0)
				box[i].cook();
		}
	}
}

