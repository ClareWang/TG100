package com.clare.mystore.store.base;

import java.util.List;

import com.clare.mystore.manager.Manager;
import com.clare.mystore.baozi.base.Baozi;
import com.clare.mystore.baozi.base.Cargo;

public abstract class Store {
	
	protected final int MAX_BOX_NUMBER = 3;
	
	protected String name;
	protected int num;
	protected SteamBox [] box;
	protected List<Cargo> cargoList;
	protected List<Baozi> baoziWaitList;
	protected List<Baozi> baoziFinishList;
	protected List<Manager> observerGroup;
	
	public abstract String getName();
	public abstract int getNum();
	public abstract void addObserver(Manager manager);
	
	abstract public boolean buy(Cargo cargo);
	
	protected void notifyObserver()
	{
		for(Manager m:observerGroup)
		{
			m.notifyManager(num);
		}
	}
}
