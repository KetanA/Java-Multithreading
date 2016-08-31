Coding Exercise:

The goal of this exercise is to assess your coding skills. Although this is a toy problem, please
make sure your solution is of production quality. You should be comfortable checking your
solution into your primary code base. Use proper names, check for errors, etc. Your code
should be representative of how you code on the job.

Constraints:
	• Your solution must be written in Java.
	• You may use any libraries and tools of your choice.
	• You must provide a solution that can be run on a unix system (preferably on the latest OS X
		release, but any unix system will do).
	• You must provide instructions on how to build and run your code.

Problem
	Make 100 HTTP GET requests to http://en.wikipedia.org/wiki/Main_Page and provide the
	following statistics for the response time:
		• 10th, 50th, 90th, 95th, 99th Percentile
		• Mean
		• Standard Deviation

Your solution must be concurrent. That is, you cannot make the requests in sequence. You
should be making at least 10 requests a time.

----------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------
Solution

I have created three java classes namely Crawler, CrawlerThreadManager and RequestHandler.
- Crawler: 
	Crawler class is responsible for creating a thread pool of fixed number of threads.
 	It uses an object of CrawlerThreadManager class, which implements Runnable.
 	After all the threads are processed and thread-pool is terminated, it prints the response time statistics.
 - CrawlerThreadManager:
 	CrawlerThreadManager class implements Runnable interface.
 	It overrides the run() method to make a call to RequestHandler methods.
 	Using synchronization and locks, it updates the shared objects viz., noOfCompletedRequests 
 	and completionTimes.
 - RequestHandler: 
 	RequestHandler class is responsible for making HTTP Get 
	requests to web resource specified by URL object.
	It parses response code and recursively fetches new 
	location if the response code is 'Redirect'.
	
----------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------
How To Run:
- create a new folder and copy all the three java files in it.
- on terminal window, go the the folder path and run this command:
	> javac *.java
- this will compile all the java files and create corresponding class files.
- now run following command:
	> java Crawler
- this command will execute the program and in the end it will print all the statistics of the 
  response time.
- logs are printed using Logger object. 
 - Mean time is calculated by averaging the response time for all the requests.
 - Standard deviation is calculated by taking the square root of the variance.
 
----------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------
END
