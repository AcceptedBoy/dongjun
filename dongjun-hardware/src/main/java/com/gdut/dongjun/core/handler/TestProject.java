package com.gdut.dongjun.core.handler;

public class TestProject {

	public static void main(String[] args) {
		Clerk cl = new Clerk();
		Producer p = new Producer(cl);
		Consumer c = new Consumer(cl);
		new Thread(p, "生产者 A").start();
		new Thread(c, "消费者 B").start();
		new Thread(p, "生产者 C").start();
		new Thread(c, "消费者 D").start();
	}

}

class Clerk {

	private int product = 0;

	public synchronized void get() {
		while (product >= 10) {
			try {
				this.wait();
				System.out.println("满货");
			} catch (InterruptedException e) {
			}
		}
		System.out.println(Thread.currentThread().getName() + " : " + ++product);

		this.notifyAll();

	}

	public synchronized void sale() {
		while (product <= 0) {
			System.out.println("缺货");
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(Thread.currentThread().getName() + " : " + --product);
		this.notifyAll();

	}
}

class Producer implements Runnable {
	private Clerk clerk;

	public Producer(Clerk c) {
		this.clerk = c;
	}

	@Override
	public void run() {
		for (int i = 0; i < 20; i++) {
			clerk.get();
		}
	}
}

class Consumer implements Runnable {
	private Clerk clerk;

	public Consumer(Clerk c) {
		this.clerk = c;
	}

	@Override
	public void run() {
		for (int i = 0; i < 20; i++) {
			clerk.sale();
		}
	}
}
