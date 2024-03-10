package concurrency;

/*
 *  Name: Phu Ha
 *
 * 	Project Name: CSC450- Portfolio Project
 *
 *	Project Purpose: Utilizing the concept of concurrency with the use of two threads
 *
 *	Description: There are four classes. One that include the main method to execute the program
 *		Two classes that implement the interface Runnable to be used as threads. One class is the 
 *		Counter class that will be served as the data used by the two threads.
 *
 *	Algorithm Used: N/A
 *
 *	Program Inputs: N/A
 *
 *	Program Outputs: Count from 0 to 20, then count from 20 back to 0
 *
 *	Program Limitations: N/A
 *
 *	Program Errors: N/A
 *
 *  Created on: Mar 6, 2024
 *      Author: phuth
 */

// class with the main method to execute the program
public class Concurrency {
	public static void main(String[] args) {
		
		// take note of the start time of the program
		long start = System.nanoTime();
		
		// create a counter object
		Counter counter = new Counter();
		// create the two threads that will use the counter object
		Thread thread1 = new Thread(new CountToTwenty(counter));
		Thread thread2 = new Thread(new CountToZero(counter));
		// start thread one
		thread1.start();
		// join thread one to make sure the program waits for it to finish before continuing
		try {
			thread1.join();
		} catch (InterruptedException e) {}
		// start and join thread two
		thread2.start();
		try {
			thread2.join();
		} catch(InterruptedException e) {}
		
		// take note of the end time of the program
		long finish = System.nanoTime();

		// calculate the elapse time
		long timeElapsed = finish - start;
		
		System.out.println("Time the program took to run: " + timeElapsed/1000 + " microseconds");
	}
}

// Counter class, serve as the shared data for the two threads
class Counter {
	// use volatile here to be more thread safe, will notify other threads if the value changes
	private volatile int count;
	
	public Counter() {
		count = 0;
	}
	// The following methods include the synchronized keyword to be thread safe
	// method to increment count by 1
	public synchronized void increment() {count++;}
	// method to decrement count by 1
	public synchronized void decrement() {count--;}
	
	// return count, doesn't have to be synchronized as count already has the keyword volatile
	public int getCount() {return count;}
}
	
// Thread 1, This thread will increase count in Counter class to 20
class CountToTwenty implements Runnable {
	private Counter counter;
	
	public CountToTwenty(Counter counter) {
		this.counter = counter;
	}
	
	@Override
	public void run() {
		while (counter.getCount() < 20) {
			System.out.println(counter.getCount());
			counter.increment();
		}
	}
}
	
// Thread 2, decrease count in Counter class to zero
class CountToZero implements Runnable {
	private Counter counter;
	
	public CountToZero(Counter counter) {
		this.counter = counter;
	}
	
	@Override
	public void run() {
		while(counter.getCount() >= 0) {
			System.out.println(counter.getCount());
			counter.decrement();
		}
	}
}


