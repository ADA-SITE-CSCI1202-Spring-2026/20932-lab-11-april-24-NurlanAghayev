import java.util.Scanner;

class SharedResource {
    private int value;
    private boolean bChanged = false;

    public synchronized int get() {
        while (!bChanged) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return -1;
            }
        }

        bChanged = false;
        return value;
    }

    public synchronized void set(int value) {
        this.value = value;
        bChanged = true;
        notify();
    }
}

public class WaitNotifyTask {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter value for producer to send: ");
        int value = input.nextInt();

        SharedResource resource = new SharedResource();

        Thread consumer = new Thread(() -> {
            int received = resource.get();
            System.out.println("Consumer received: " + received);
        });

        Thread producer = new Thread(() -> {
            resource.set(value);
            System.out.println("Producer sent: " + value);
        });

        consumer.start();
        producer.start();

        try {
            consumer.join();
            producer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        input.close();
    }
}