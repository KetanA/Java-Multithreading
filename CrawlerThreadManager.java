package ketana.coding.Apple;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

/**
 * @author KetanA
 * Created with Eclipse IDE. Date: 08/14/2016 Time: 11:25 AM
 * CrawlerThreadManager class implements Runnable interface.
 * It overrides the run() method to make a call to RequestHandler methods.
 * Using synchronization and locks, it updates the shared objects viz., noOfCompletedRequests and completionTimes.
 */
public class CrawlerThreadManager implements Runnable {
	
	// noOfCompletedRequests: Variable to keep count of number of completed HTTP GET requests.
	// lock: An Object used as a lock in synchronized block while updating noOfCompletedRequests. 
    // completionTimes: ConcurrentHashMap to store response time for every request.
	// ConcurrentHashMap is used specifically to make it thread-safe.
	private static int noOfCompletedRequests;
    private final Object lock = new Object();
    private final ThreadPoolExecutor threadPoolExecutor;
    public static Map<Integer, Long> completionTimes = new ConcurrentHashMap<Integer, Long>();
    private final Logger logger = Logger.getLogger(CrawlerThreadManager.class.getName());

    // Constructor for CrawlerThreadManager class 
    // Initializing a ThreadPoolExecutor object.
     
    CrawlerThreadManager(ExecutorService executorService) {
		if (executorService instanceof ThreadPoolExecutor) {
		    threadPoolExecutor = (ThreadPoolExecutor) executorService;
		} else {
		    threadPoolExecutor = null;
		}
    }

    /**
     * Overriding run() method of Runnable.
     * This method makes a call to getHTML() function in RequestHandler class.
     */
    @Override
    public void run() {
    	long startTime = System.currentTimeMillis();
		RequestHandler requestHandler = new RequestHandler();
		try {
			 requestHandler.sendRequest();
			 logger.info("Number of active threads: " + threadPoolExecutor.getActiveCount());
			 logger.info("Current thread-pool size: " + threadPoolExecutor.getPoolSize());
			 logger.info("Current executing thread name: " + Thread.currentThread().getName());
		} catch (Exception e) {
			logger.severe("Exception: failed to call getHTML() function in RequestHandler.");
			e.printStackTrace();
		}
        
        // Using synchronized block to update 'noOfCompletedRequests' count
		// and update completionTimes hashMap object.
        synchronized (lock) {
        	noOfCompletedRequests++;
        	completionTimes.put(noOfCompletedRequests, (System.currentTimeMillis()-startTime));
        	logger.info("Number of completed requests: " + noOfCompletedRequests);
        }
    }
}