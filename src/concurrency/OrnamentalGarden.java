package concurrency;

import static net.mindview.util.Print.print;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Count{
	private int count =0;
	private Random rand = new Random();
	//remove the synchronized keyword to see counting failed
	public synchronized int increment() {
		int temp=count;
		if(rand.nextBoolean())
			Thread.yield();//yield():收益：改善线程之间的相对进展，防止过度使用cpu；
		//从执行状态变为可执行状态（就绪），让掉CPU占用重新竞争
		return (count= ++temp);
	}
	public synchronized int value() {
		return count;
	}
}

class Entrance implements Runnable{
	private static Count count = new Count();
	private static List<Entrance> entrances = 
			new ArrayList<Entrance>();
	private int number = 0;
	//don't need to synchronization to read
	private final int id;
	private static volatile boolean canceled= false;
	//Atomic operation on a volatile field;
	public static void cancel() {
		canceled = true;
	}
	public Entrance(int id) {
		this.id = id;
		//prevent to garbage collection of dead tasks
		entrances.add(this);
	}
	
	public void run(){
		while(!canceled) {
			synchronized(this) {
				++number;
			}
			print(this+" total:"+count.increment());
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			}catch(InterruptedException e) {
				print("sleep interrupted");
			}
		}
		print("stoping "+this);
	}
	public synchronized int getValue() {
		return number;
	}
	public String toString() {
		return "Entrance"+id+" : "+getValue();
	}
	public static int getTotalCount() {
		return count.value();
	}
	public static int sumEntrances() {
		int sum = 0;
		for(Entrance entrance:entrances)
			sum += entrance.getValue();
		return sum;
	}
}
public class OrnamentalGarden {
	public static void main(String[] args) throws Exception{
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i=0;i<5;i++)
			exec.execute(new Entrance(i));
		//
		TimeUnit.MILLISECONDS.sleep(3000);
		Entrance.cancel();
		exec.shutdown();
		//awaitTermination()：when thread is interrupted ,this trigger first!
		//if all the task end before timeout,return true!
		if(!exec.awaitTermination(250, TimeUnit.MILLISECONDS))
			print("Some tasks were not terminated!");
		print("Total: "+Entrance.getTotalCount());
		print("Sum of Entrances: "+Entrance.sumEntrances());
	}
}
