package net.bakulin;

public interface EventStream {
    void consume(EventConsumer consumer);
}
