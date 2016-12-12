import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by breynard on 01/12/16.
 */
public class Tester implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Tester.class);

    private final String team;
    private final int nbDuration;
    private final MyExecutorService myExecutorService;

    public Tester(String team, int nbDuration, MyExecutorService myExecutorService) {
        super();
        this.team = team;
        this.nbDuration = nbDuration;
        this.myExecutorService = myExecutorService;
    }

    public void run() {
        LOGGER.info("----------------------------------------------------> Start team {}", team);
        Random random = new Random();

        List<Long> durations = new ArrayList<Long>(nbDuration);
        for (int i = 0; i < nbDuration; i++) {
            durations.add((long) random.nextInt(4000));
        }

        try {
            myExecutorService.runTasks(team, durations.iterator());
        } catch (ExecutionException e) {
            LOGGER.warn("ExecutionException", e);
        } catch (InterruptedException e) {
            LOGGER.warn("InterruptedException", e);
        }
        LOGGER.info("----------------------------------------------------> End team {}", team);
    }
}
