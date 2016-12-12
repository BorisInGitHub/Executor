import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by breynard on 01/12/16.
 */
public class MyExecutorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyExecutorService.class);

    private final java.util.concurrent.ExecutorService executorService;

    public MyExecutorService() {
        super();

        // TODO : Mettre la queue ayant la même capacité (ou inférieure) que le threadPoolExecutor ... Si on met 1 ou 0, cela réduit le delta.

        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(1, true);
        executorService = new ThreadPoolExecutor(3, 3, 1L, TimeUnit.HOURS, workQueue);
        RejectedExecutionHandler block = new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                try {
                    executor.getQueue().put(r);
                } catch (InterruptedException e) {
                    LOGGER.warn("InterruptedException", e);
                }
            }
        };

        ((ThreadPoolExecutor) executorService).setRejectedExecutionHandler(block);
    }

    public void debug() {
        LOGGER.warn("MyExecutorService : {}", ((ThreadPoolExecutor) executorService).getActiveCount());
    }

    public long runTasks(String team, Iterator<Long> durations) throws ExecutionException, InterruptedException {
        // TODO utiliser un completion service ??? cf https://openclassrooms.com/courses/le-framework-executor

        CompletionService<Long> completionService = new ExecutorCompletionService<Long>(executorService);

        int nbTasks = 0;
        AtomicInteger counter = new AtomicInteger(0);
        while (durations.hasNext()) {
            nbTasks++;
            Long duration = durations.next();
            completionService.submit(new MyWaitingTask(team, duration, counter));
        }

        // Pour avoir un état des lieux interessant, il faurdati faire 2 thread, un de lecture et un d'écriture ... (avec un atomicBoolean readEnded et un atomicInteger readCount)

        LOGGER.warn("Team {} SUBMITTED DONE.", team);
        long totalWait = 0l;
        for (int i = 0; i < nbTasks; i++) {
            // TODO : Il faut compter le nombre de submit pour faire le même nombre de get
            Future<Long> take = completionService.take();
            totalWait += take.get();
        }

        LOGGER.warn("Team {} TERMINATED in {}ms.", team, totalWait);
        return totalWait;
    }
}
