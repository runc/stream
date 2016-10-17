package net.bakulin.impl;

import com.codahale.metrics.MetricRegistry;
import lombok.extern.slf4j.Slf4j;
import net.bakulin.Event;
import net.bakulin.EventConsumer;
import net.bakulin.EventStream;
import net.bakulin.ProjectionMetrics;
import net.bakulin.consumer.ClientProjection;
import net.bakulin.consumer.NaivePool;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class NaivePoolImpl implements EventStream {
    private static final Random RANDOM = new Random();

    /**
     * According requirements system should handle 1000 events per second.
     * For a single event system should waste about 10ms.
     * So single thread could process no more than 100 events per send.
     * For handle whole load in a second we need at least 10 threads in success way.
     */
    private static final int NUM_THREADS = 10;

    public static void main(String[] args) {

        MetricRegistry metricRegistry = new MetricRegistry();

        ProjectionMetrics projectionMetrics = new ProjectionMetrics(metricRegistry);

        ClientProjection clientProjection = new ClientProjection(projectionMetrics);

        NaivePool concurrentProcessor = new NaivePool(NUM_THREADS, clientProjection, metricRegistry);

        NaivePoolImpl es = new NaivePoolImpl();
        es.consume(concurrentProcessor);
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

        for (int i = 0; i < source.size()/1000; i++) {
            log.info("Process " + (i + 1) + "th thousand events");
            source.subList(i * 1000, (i + 1) * 1000)
                    .stream()
                    .sequential()
                    .forEach(event -> consumer.consume(event));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {

            }
        }
    }
}
