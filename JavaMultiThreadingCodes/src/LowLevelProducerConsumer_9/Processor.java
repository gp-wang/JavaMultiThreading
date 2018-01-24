package LowLevelProducerConsumer_9;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Codes with minor comments are from
 * <a href="http://www.caveofprogramming.com/youtube/">
 * <em>http://www.caveofprogramming.com/youtube/</em>
 * </a>
 * <br>
 * also freely available at
 * <a href="https://www.udemy.com/java-multithreading/?couponCode=FREE">
 * <em>https://www.udemy.com/java-multithreading/?couponCode=FREE</em>
 * </a>
 *
 * @author Z.B. Celik <celik.berkay@gmail.com>
 */
@SuppressWarnings("InfiniteLoopStatement")
public class Processor {

    private LinkedList<Integer> buffer = new LinkedList<>();
    private final int LIMIT = 10;
    private final Object lock = new Object();

    private final List<Integer> list = IntStream.range(0,10000).boxed().collect(Collectors.toList());
    private int pointer=0;

    public int sum=0;

    public void produce() throws InterruptedException {

        while (true) {
            synchronized (lock) {
                //whenever the thread is notified starts again from the loop
                while (buffer.size() == LIMIT) {
                    lock.wait();// wait() is also true
                }

                if(pointer>=10000) break;

                Integer value = list.get(pointer++);
                buffer.add(value);

                System.out.println("Producer added: " + value + " queue size is " + buffer.size());

                lock.notifyAll();
            }
        }
    }

    public void consume() throws InterruptedException {

        while (true) {
            synchronized (lock) {
                while (buffer.size() == 0) {
                    lock.wait();
                }

                if(pointer>=10000) break;

                int value = buffer.remove(0);
                System.out.print("Removed value by consumer is: " + value);
                System.out.println(" Now buffer size is: " + buffer.size());
                sum+=value;
                lock.notifyAll();
            }

        }
    }
}
