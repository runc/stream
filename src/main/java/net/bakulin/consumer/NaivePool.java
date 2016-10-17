package net.bakulin.consumer;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import net.bakulin.Event;
import net.bakulin.EventConsumer;
import net.bakulin.ProjectionMetrics;
import org.omg.PortableServer.THREAD_POLICY_ID;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.*;

public class NaivePool implements EventConsumer, Closeable {

    private final EventConsumer downstream;
    private final ExecutorService executorService;

    public NaivePool(int poolSize, EventConsumer downstream, MetricRegistry metricRegistry) {
        LinkedBlockingDeque<Runnable> queue = new LinkedBlockingDeque<>();
        String name = MetricRegistry.name(ProjectionMetrics.class, "queue");
        Gauge<Integer> gauge = queue::size;
        metricRegistry.register(name, gauge);
        this.executorService = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, queue);
        this.downstream = downstream;
    }

    @Override
    public Event consume(Event event) {
        executorService.submit(() -> downstream.consume(event));
        return event;
    }

    @Override
    public void close() throws IOException {
        executorService.shutdown();
    }
}
