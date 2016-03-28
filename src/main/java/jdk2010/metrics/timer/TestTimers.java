package jdk2010.metrics.timer;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer.Context;

public class TestTimers {
    
    private static final MetricRegistry metricRegistry = new MetricRegistry();
    
    public static void main(String[] args) throws InterruptedException {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(1,TimeUnit.SECONDS);
       Context context= metricRegistry.timer("connection").time();
       context.stop();
       Thread.sleep(5 * 1000);
    }
}
