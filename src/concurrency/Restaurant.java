package concurrency;
import static net.mindview.util.Print.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
class Meal{
	private final int orderNum;
	public Meal(int orderNum) {this.orderNum = orderNum;}
	public String toString() {return "Meal"+orderNum;}
}

class WaitPerson implements Runnable{
	private Restaurant res;
	public WaitPerson(Restaurant res) {this.res = res;}
	public void run() {
		try {
			while(!Thread.interrupted()) {
				synchronized (this) {
					while(res.meal==null)
						wait();
				}
				print("waitPerson got: "+res.meal);
				synchronized(res.chef) {
					res.meal = null;
					res.chef.notifyAll();
				}
			}
		}catch(InterruptedException e) {
			print("waitPerson interrupted!");
		}
	}
}
class Chef implements Runnable{
	private int count = 0;
	private Restaurant res;
	public Chef(Restaurant res) {this.res = res;}
	public void run() {
		try {
			while(!Thread.interrupted()) {
				synchronized (this) {
					while(res.meal!=null)
						wait();
				}
				if(++count == 10) {
					print("out fo food,Closing!");
					res.exec.shutdownNow();
				}
				printnb("Order up!");
				synchronized (res.waitPerson) {
					res.meal = new Meal(count);
					res.waitPerson.notifyAll();
				}
				TimeUnit.MILLISECONDS.sleep(100);
			}
		}catch(InterruptedException e) {
			print("Chef interrupted!");
		}
	}
}
public class Restaurant {
	Meal meal;
	ExecutorService exec = Executors.newCachedThreadPool();
	WaitPerson waitPerson = new WaitPerson(this);
	Chef chef = new Chef(this);
	
	public Restaurant() {
		exec.execute(chef);
		exec.execute(waitPerson);
	}
	public static void main(String[] args) {
		new Restaurant();
	}
}
