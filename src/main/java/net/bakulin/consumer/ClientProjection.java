package net.bakulin.consumer;

import net.bakulin.Event;
import net.bakulin.EventConsumer;
import net.bakulin.ProjectionMetrics;
import net.bakulin.Sleeper;

import java.time.Duration;
import java.time.Instant;

public class ClientProjection implements EventConsumer {

    private final ProjectionMetrics metrics;

    public ClientProjection(ProjectionMetrics metrics) {
        this.metrics = metrics;
    }

    @Override
    public Event consume(Event event) {
        metrics.latency(Duration.between(event.getCreated(), Instant.now()));
        Sleeper.randSleep(10, 1);
        return event;
    }
}
