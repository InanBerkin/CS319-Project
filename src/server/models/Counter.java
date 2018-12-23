package server.models;

import java.util.concurrent.atomic.AtomicBoolean;

public class Counter extends Thread {

    public static final boolean FORWARD = true;
    public static final boolean BACKWARD = false;

    private AtomicBoolean isActive;
    private CounterSignable callback;
    private int count;
    private int step;
    private boolean direction;

    public Counter(boolean direction, int step, CounterSignable callback) {
        this.isActive = new AtomicBoolean();
        this.count = 0;
        this.step = step;
        this.direction = direction;
        this.callback = callback;
    }

    public Counter(boolean direction, int count, int step, CounterSignable callback) {
        this.isActive = new AtomicBoolean(false);
        this.count = count;
        this.step = step;
        this.direction = direction;
        this.callback = callback;
    }

    public void startCounter() {
        isActive.set(true);
        start();
    }

    public void stopCounter() {
        callback.counterInterrupted();
        isActive.set(false);
    }

    @Override
    public void run() {
        while (isActive.get()) {
            callback.counterSignal(count);
            count();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        interrupt();
    }

    private void count() {
        if (direction) {
            count += step;
        }
        else {
            count -= step;

            if (count < 0) {
                callback.counterStopped();
                isActive.set(false);
            }
        }
    }
}
