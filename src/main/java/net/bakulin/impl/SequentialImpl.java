package net.bakulin.impl;

import com.codahale.metrics.MetricRegistry;
import net.bakulin.Event;
import net.bakulin.EventConsumer;
import net.bakulin.EventStream;
import net.bakulin.ProjectionMetrics;
import net.bakulin.consumer.ClientProjection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SequentialImpl implements EventStream {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {

        ProjectionMetrics projectionMetrics = new ProjectionMetrics(new MetricRegistry());

        ClientProjection clientProjection = new ClientProjection(projectionMetrics);

        SequentialImpl es = new SequentialImpl();

        es.consume(clientProjection);
    }

    @Override
    public void consume(EventConsumer consumer) {
        List<Event> source;

        source = RANDOM.ints()
                .boxed()
                .parallel()
                .map(i -> new Event(i, UUID.randomUUID()))
                .limit(10_000)
                .collect(Collectors.toList());

        source.stream().sequential().forEach(event -> consumer.consume(event));
    }
}
