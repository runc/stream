package net.bakulin.impl;

import com.codahale.metrics.MetricRegistry;
import net.bakulin.Event;
import net.bakulin.EventConsumer;
import net.bakulin.EventStream;
import net.bakulin.ProjectionMetrics;
import net.bakulin.consumer.ClientProjection;

import java.util.ArrayList;
import java.util.List;

public class SequentialImpl implements EventStream {
    public static void main(String[] args) {

        ProjectionMetrics projectionMetrics = new ProjectionMetrics(new MetricRegistry());

        ClientProjection clientProjection = new ClientProjection(projectionMetrics);

        SequentialImpl es = new SequentialImpl();
        es.consume(clientProjection);
    }

    @Override
    public void consume(EventConsumer consumer) {
        List<Event> source = new ArrayList<>();

        source.stream().forEach(event -> consumer.consume(event));
    }
}
