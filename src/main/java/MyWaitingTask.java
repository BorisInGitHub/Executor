import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tâche d'attente de n secondes
 * Created by breynard on 01/12/16.
 */
public class MyWaitingTask implements Callable<Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyWaitingTask.class);

    private final int taskId;
    private final String team;
    private final long waitingTimeInMs;

    public MyWaitingTask(String team, long waitingTimeInMs, AtomicInteger COUNTER) {
        super();
        this.team = team;
        taskId = COUNTER.getAndIncrement();
        this.waitingTimeInMs = waitingTimeInMs;
    }

    public Long call() {
        LOGGER.warn("Team {} \t| Start task {} : {}ms", team, taskId, waitingTimeInMs);
        long startTime = System.currentTimeMillis();
        long endTime = startTime + waitingTimeInMs;
        long pendingTime = waitingTimeInMs;
        while (pendingTime > 0) {
            try {
                Thread.sleep(pendingTime);
            } catch (InterruptedException e) {
                LOGGER.warn("InterruptedException", e);
            }
            pendingTime = endTime - System.currentTimeMillis();
        }
        long l = System.currentTimeMillis() - startTime;
        LOGGER.warn("Team {} \t| End task   {} ({}ms but {}ms)", team, taskId, waitingTimeInMs, l);
        return l;
    }
}
