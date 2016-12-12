/**
 * Created by breynard on 01/12/16.
 */
public final class Main {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        MyExecutorService myExecutorService = new MyExecutorService();

        String team1 = "Team 1";
        Tester tester1 = new Tester(team1, 100, myExecutorService);
        new Thread(tester1).start();
        for (int i = 0; i < 5; i++) {
            myExecutorService.debug();
            Thread.sleep(1000);
        }
        String team2 = "Team 2";
        Tester tester2 = new Tester(team2, 10, myExecutorService);
        new Thread(tester2).start();
        for (int i = 0; i < 5; i++) {
            myExecutorService.debug();
            Thread.sleep(1000);
        }
        String team3 = "Team 3";
        Tester tester3 = new Tester(team3, 10, myExecutorService);
        new Thread(tester3).start();
        String team4 = "Team 4";
        Tester tester4 = new Tester(team4, 100, myExecutorService);
        new Thread(tester4).start();
        while (true) {
            myExecutorService.debug();
            Thread.sleep(1000);
        }
    }
}
