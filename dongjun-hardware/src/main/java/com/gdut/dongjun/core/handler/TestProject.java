package com.gdut.dongjun.core.handler;

public class TestProject {
	
	public static void main(String[] args) {
		Good g = new Good();
		Consumer c = new Consumer(g);
		Producer p = new Producer(g);
		new Thread(c).start();
		new Thread(p).start();
	}
}

class Good {
	public int product = 0;
}

class Consumer implements Runnable {
	
	private Good g;
	public Consumer(Good g) {
		this.g = g;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			while (g.product == 0) {
				//无商品时等待
				try {
					g.wait();
					System.out.println("缺货了");
				} catch (InterruptedException e) {
				}
			}
			System.out.println(Thread.currentThread().getName() + " 消费 : " + --g.product);
			g.notifyAll();
		}
	}
	
}

class Producer implements Runnable {
	
	private Good g;
	public Producer(Good g) {
		this.g = g;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			while (g.product >= 5) {
				try {
					//产品过剩时候等待
					g.wait();
					System.out.println("产品过剩");
				} catch (InterruptedException e) {
				}
			}
			System.out.println(Thread.currentThread().getName() + " 生产 : " + ++g.product);
			g.notifyAll();
		}
	}
}
