import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Philosophers extends Thread{
    private String name;
    private int leftFork;
    private int rightFork;
    private int countEat;
    private CountDownLatch cdl;
    private Table table;

    private Random random;

    public Philosophers(String name, Table table, int leftFork, int rightFork, CountDownLatch cdl) {
        this.table = table;
        this.name = name;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.cdl = cdl;
        countEat = 0;
        random = new Random();
    }

    @Override
    public void run() {
        while(countEat <3){
            while (countEat < 3) {
                try {
                    thinking();
                    eating();
                } catch (InterruptedException e) {
                    e.fillInStackTrace();
                }
            }

            System.out.println(name + " наелся до отвала");
            cdl.countDown();
        }
    }

    private void eating() throws InterruptedException {
        if (table.tryGetForks(leftFork, rightFork)) {
            System.out.println(name + " уплетает вермишель, используя вилки: " + leftFork
                    + " и " + rightFork);
            sleep(random.nextLong(3000, 6000));
            table.putForks(leftFork, rightFork);
            System.out.println(name + " покушал, можно и помыслить. " +
                    "Не забыв при этом вернуть вилки " + leftFork + " и " + rightFork);
            countEat++;
        }

    }

    private void thinking() throws InterruptedException {
        sleep(random.nextLong(100, 2000));
    }
}
