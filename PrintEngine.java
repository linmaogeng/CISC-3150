import java.util.concurrent.locks.*;

public class PrintEngine {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new PE().print();
	}
}

class PE{
	int letter;
	Lock L = new ReentrantLock();
	Object lock = new Object();
	
	Condition pe = L.newCondition();
	Condition one = L.newCondition();
	Condition two = L.newCondition();
	Condition three = L.newCondition();
	Condition four = L.newCondition();
	
	Thread t1 = new Thread(new First(this));
	Thread t2 = new Thread(new Second(this));
	Thread t3 = new Thread(new Third(this));
	Thread t4 = new Thread(new Forth(this));
	
	
	private synchronized void printNext() throws Exception{	
		if(letter >= 91) 
			pe.signal();
		else
			System.out.println(Thread.currentThread().getId()+ " " + (char)letter++);
	}
	
	public void print() {

		letter = 65;
		
		L.lock();
		try{
			t1.start();
			pe.await();
			t2.start();
			pe.await();
			t3.start();
			pe.await();
			t4.start();
			pe.await();
			one.signal();
			pe.await();
			System.exit(0);

		}catch(Exception t){
			System.out.println("Print Engine Failed!");
		}
		finally{
			L.unlock();
		}

	}
	
	public void printOne(){
		L.lock();
		try{
			pe.signal();
			one.await();
			while(true) {
				printNext();
				two.signal();	
				one.await();
			}
		}catch(Exception t){
			
		}
		finally{
			L.unlock();
		}
	}
	
	public void printTwo(){
		L.lock();
		try{
			pe.signal();
			two.await();
			while(true) {
				printNext();
				three.signal();	
				two.await();
			}
		}catch(Exception t){
			
		}
		finally{
			L.unlock();
		}
	}
	
	public void printThree(){
		L.lock();
		try{
			pe.signal();
			three.await();
			while(true) {
				printNext();
				four.signal();	
				three.await();
			}
		}catch(Exception t){
			
		}
		finally{
			L.unlock();
		}
	}
	
	public void printFour(){
		L.lock();
		try{
			pe.signal();
			four.await();
			while(true) {
				printNext();
				one.signal();	
				four.await();
			}
		}catch(Exception t){
			
		}
		finally{
			L.unlock();
		}
	}
}

class First implements Runnable{
	
	PE printEngine;
	
	First(PE pe){
		printEngine = pe;
	}
	
	public void run() {
		printEngine.printOne();
	}
	
}

class Second implements Runnable{
	
	PE printEngine;
	
	Second(PE pe){
		printEngine = pe;
	}
	
	public void run() {
		printEngine.printTwo();
	}
	
}


class Third implements Runnable{
	
	PE printEngine;
	
	Third(PE pe){
		printEngine = pe;
	}
	
	public void run() {
		printEngine.printThree();
	}
	
}


class Forth implements Runnable{
	
	PE printEngine;
	
	Forth(PE pe){
		printEngine = pe;
	}
	
	public void run() {
		printEngine.printFour();
	}
	
}

