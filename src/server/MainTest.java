package server;

import server.models.Counter;
import server.models.CounterSignable;

public class MainTest implements CounterSignable {

    public MainTest() {
        Counter counter = new Counter(Counter.BACKWARD, 10, 1, this);
        counter.start();
    }

    @Override
    public void counterStopped() {
        System.out.println("» Counter is stopped!");
    }

    @Override
    public void counterSignal(int count) {
        System.out.println("Count: " + count);
    }

    public static void main(String[] args) {
        new MainTest();
    }
}
