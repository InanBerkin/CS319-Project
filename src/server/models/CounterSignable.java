package server.models;

public interface CounterSignable {

    public void counterStopped();
    public void counterSignal(int count);
    public void counterInterrupted();

}
