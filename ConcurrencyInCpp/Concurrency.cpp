/*
 *  Name: Phu Ha
 *
 * 	Project Name: CSC450-Project Milestone
 *
 *	Project Purpose: To understand the concept of Concurrency in C++
 *
 *	Algorithm Used: N/A
 *
 *	Program Inputs: N/A
 *
 *	Program Outputs: Count from 0 to 20 then back to 0
 *
 *	Program Limitations: N/A
 *
 *	Program Errors: N/A
 *
 *  Created on: March 3, 2024
 *      Author: Phu Ha
 */

#include<iostream>
#include<thread>
#include<mutex>
#include<condition_variable>
#include<chrono>

using namespace std::chrono;

std::mutex mtx; // create the mutex variable
std::condition_variable cv; // this variable allows threads to communicate

int shared_counter = 0; // this variable will be use by both threads

bool finish_counting = false; // condition to see if thread one is done running

// function for first thread
void countToTwenty()
{
	std::lock_guard<std::mutex> lock(mtx); // use lock_guard to lock shared resources
	// count to 20, print each value
	while (shared_counter < 20) {
		std::cout << shared_counter++ << std::endl;
	}
	finish_counting = true; // change the condition to true, thread one is finished
	cv.notify_all(); // notify other thread
}

// function for second thread
void countToZero()
{
	// we're using a unique_lock here instead of lock_guard because
	// condition_variable works with unqiue_lock
	std::unique_lock<std::mutex> lock(mtx);

	// this asks thread this thread to wait for until finish_counting is true
	cv.wait(lock, [] {return finish_counting; });

	// count to zero and print each value
	while (shared_counter >= 0) {
		std::cout << shared_counter-- << std::endl;
	}
}

int main()
{
	// record starting time of program
	auto start = high_resolution_clock::now();

	// create thread 1 and 2
	std::thread t1(countToTwenty);
	std::thread t2(countToZero);

	// wait for both thread to finish
	t1.join();
	t2.join();

	// record ending time of program
	auto stop = high_resolution_clock::now();

	// calculate time of program
	auto duration = duration_cast<microseconds>(stop - start);

	// print out the time program need to take
	std::cout << "Time taken by function: "
	         << duration.count() << " microseconds" << std::endl;

	return 0;
}


