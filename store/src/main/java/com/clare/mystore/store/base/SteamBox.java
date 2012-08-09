package com.clare.mystore.store.base;

import java.util.ArrayList;
import java.util.List;
import java.lang.Thread;
import com.clare.mystore.baozi.base.*;

public class SteamBox implements Runnable{
	
	private String name;
	private List<Baozi> baoziList;
	private List<Baozi> baoziFinishList;
	private boolean status;
	private boolean waitToCook;
	
	public SteamBox(String name,List<Baozi> baoziFinishList) {
		this.name=name;
		baoziList=new ArrayList<Baozi>();
		this.baoziFinishList=baoziFinishList;
		status=true;
		waitToCook=true;
	}
	
	public void add(Baozi baozi){
		baoziList.add(baozi);
	}
	
	public void cook(){

		
		Thread cooking = new Thread(this,"cooking");
		if(waitToCook){
			waitToCook=false;
			cooking.start();
		}
	}
	
	public boolean checkStatus(){
		return status;
	}

	public List<Baozi> getBaoziList() {
		return baoziList;
	}

	@Override
	public void run() {
		System.out.println("cooking in 2s!");
		try {
			Thread.sleep(2000);//wait 2s to start;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("now cooking...");
		status=false;
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for(int i=0;i<baoziList.size();i++){
			System.out.println(name+" Type:"+baoziList.get(i).getType()+" Taste:"+baoziList.get(0).getTaste()+" is ready!");
			baoziFinishList.add(baoziList.get(i));
		}
		baoziList.clear();
		status=true;
		waitToCook=true;
	}
}