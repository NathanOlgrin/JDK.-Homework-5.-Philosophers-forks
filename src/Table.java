import java.util.concurrent.CountDownLatch;

public class Table extends Thread{
    private final int PHILOSOPHER_COUNT = 5;
    private Forks[] forks;
    private Philosophers[] philosophers;
    private CountDownLatch cdl;


    public Table() {
        forks = new Forks[PHILOSOPHER_COUNT];
        philosophers = new Philosophers[PHILOSOPHER_COUNT];
        cdl = new CountDownLatch(PHILOSOPHER_COUNT);
        init();
    }

    @Override
    public void run() {
        System.out.println("Заседание макаронных мудрецов объявляется открытым");
        try {
            thinkingProcess();
            cdl.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Все философы накушались");
    }

    public synchronized boolean tryGetForks(int leftFork, int rightFork) {
        if (!forks[leftFork].isUsage_flag() && !forks[rightFork].isUsage_flag()) {
            forks[leftFork].setUsage_flag(true);
            forks[rightFork].setUsage_flag(true);
            return true;
        }
        return false;
    }

    public void putForks(int leftFork, int rightFork){
        forks[leftFork].setUsage_flag(false);
        forks[rightFork].setUsage_flag(false);
    }

    private void init() {
        for (int i = 0; i < PHILOSOPHER_COUNT; i++) {
            forks[i] = new Forks();
        }

        for (int i = 0; i < PHILOSOPHER_COUNT; i++) {
            philosophers[i] = new Philosophers("Philosopher №" + i, this,
                    i, (i + 1) % PHILOSOPHER_COUNT, cdl);
        }
    }

    private void thinkingProcess() {
        for (Philosophers philosopher : philosophers) {
            philosopher.start();
        }
    }
}
