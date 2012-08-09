package com.clare.mystore.test;

import com.clare.mystore.manager.Manager;
import com.clare.mystore.store.*;
import com.clare.mystore.store.base.Store;
import com.clare.mystore.baozi.base.Type;
import com.clare.mystore.consumer.Consumer;


public class Test 
{
	public static void main(String[] args) throws InterruptedException 
	{
		Store [] store = new Store [2];
		store[0]=new ShanghaiStore();
		store[1]=new SichuanStore();
		Manager manager=new Manager(store);
		manager.work();
		
		Consumer consumer1=new Consumer("clare");
		consumer1.addBaozi(Type.DOUSHA, 3);
		consumer1.addBaozi(Type.NIUROU, 2);
		consumer1.buyBaozi(store[0]);
		
		Thread.sleep(1000);
		Consumer consumer2=new Consumer("TG");
		consumer2.addBaozi(Type.TANG, 3);
		consumer2.buyBaozi(store[0]);
		
		Thread.sleep(2000);
		Consumer consumer3=new Consumer("abc");
		consumer3.addBaozi(Type.ZHUROU, 5);
		consumer3.buyBaozi(store[0]);

		
		Thread.sleep(12000);
		manager.stop();
	}
}
