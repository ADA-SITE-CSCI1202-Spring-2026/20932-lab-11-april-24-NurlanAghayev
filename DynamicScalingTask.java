import java.util.Scanner;

class MathTask implements Runnable {
    private final long iterations;

    public MathTask(long iterations) {
        this.iterations = iterations;
    }

    @Override
    public void run() {
        double sum = 0;

        for (long i = 0; i < iterations; i++) {
            sum += Math.pow(i, 3) + i * i;
        }
    }
}

public class DynamicScalingTask {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int coreCount = Runtime.getRuntime().availableProcessors();
        System.out.println("Available logical processors: " + coreCount);

        System.out.print("Enter number of iterations per thread: ");
        long iterations = input.nextLong();

        measureThreads(1, iterations);
        measureThreads(coreCount, iterations);

        input.close();
    }

    public static void measureThreads(int threadCount, long iterations) {
        Thread[] threads = new Thread[threadCount];

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new MathTask(iterations));
            threads[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long endTime = System.currentTimeMillis();

        System.out.println(threadCount + " thread(s) took: " +
                (endTime - startTime) + " ms");
    }
}