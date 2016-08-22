package ketana.coding.Apple;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author KetanA
 * Created with Eclipse IDE. Date: 08/14/2016 Time: 11:36 AM
 * Crawler class is responsible for creating a thread pool of fixed number of threads.
 * It uses an object of CrawlerThreadManager class, which implements Runnable.
 * After all the threads are processed and thread-pool is terminated, it prints the response time statistics. 
 */
public class Crawler {
	private final int NUM_THREADS = 10;
	private final int NUM_REQUESTS = 100;
	private final int THREAD_TIMEOUT = 40;
	private final Logger logger = Logger.getLogger(Crawler.class.getName());
	
	public static void main(String[] args) throws InterruptedException {
		Crawler crawler = new Crawler();
		crawler.makeHttpRequests();
	}

	// Creates a thread-pool of fixed number of threads(10) using ExecutorService.
	// Executes these threads using an object of CrawlerThreadManager, 
	// which implements Runnable.  It waits for the termination of all threads
	// for fixed duration and then ultimately attempts to terinate all
	// executing tasks.
	public void makeHttpRequests() {
		ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
		for (int i = 0; i < NUM_REQUESTS; i++) {
			Runnable worker = new CrawlerThreadManager(executor);
			executor.execute(worker);
		}
		try {
			// This will make the executor accept no new threads
			// and finish all existing threads in the queue
			executor.shutdown();
			if(executor.awaitTermination(THREAD_TIMEOUT, TimeUnit.SECONDS)) {
				logger.info("All threads have been terminated successfully!");
			} else {
				logger.warning("Executor threadpool did not terminate. Timeout elapsed before termination.");
			}
		} catch (InterruptedException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		} finally {
			//Attempt to stop all running threads.
			executor.shutdownNow();
			logger.info("Terminated all threads.");
			printStatistics();
		}
	}
	
	/**
	 *  Displays statistics for the response times for given requests.
	 *  Calculates mean response time, standard deviation.
	 */
	public void printStatistics() {
		System.out.println("\n\n================================================================================================");
		System.out.println("Statistics: Response time in milliseconds");
		System.out.println("================================================================================================");
		System.out.println("Response time for 10th request: " + CrawlerThreadManager.completionTimes.get(10));
		System.out.println("Response time for 50th request: " + CrawlerThreadManager.completionTimes.get(50));
		System.out.println("Response time for 90th request: " + CrawlerThreadManager.completionTimes.get(90));
		System.out.println("Response time for 95th request: " + CrawlerThreadManager.completionTimes.get(95));
		System.out.println("Response time for 99th request: " + CrawlerThreadManager.completionTimes.get(99));
		System.out.println("Response time for 99th request: " + CrawlerThreadManager.completionTimes.get(100));
		
		//calculating Mean response time
		long totalTime = 0;
		int numOfRequests = CrawlerThreadManager.completionTimes.keySet().size();
		System.out.println("Number of requests: " + numOfRequests);
		for(int i : CrawlerThreadManager.completionTimes.keySet()) {
			totalTime += CrawlerThreadManager.completionTimes.get(i);
		}
		double meanTime = (double)totalTime/numOfRequests;
		System.out.println("\nMean response time: " + meanTime);
		
		//calculating Standard deviation
		long variance = 0;
		for(int i : CrawlerThreadManager.completionTimes.keySet()) {
			double diff = (CrawlerThreadManager.completionTimes.get(i)-meanTime);
			variance += diff*diff;
		}
		variance = variance/numOfRequests;
		System.out.println("\nStandard deviation: " + (float)Math.sqrt(variance));
		System.out.println("================================================================================================");
	}
}