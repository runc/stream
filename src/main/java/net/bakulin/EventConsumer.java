package net.bakulin;

@FunctionalInterface
public interface EventConsumer {
    Event consume(Event event);
}
