package jdk2010.metrics.gauges;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;

public class TestGauges {
    /**
     * ʵ����һ��registry������ĵ�һ��ģ�飬�൱��һ��Ӧ�ó����metricsϵͳ��������ά��һ��Map
     */
    private static final MetricRegistry metrics = new MetricRegistry();

    private static Queue<String> queue = new LinkedBlockingDeque<String>();

    /**
     * �ڿ���̨�ϴ�ӡ���
     */
    private static ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();

    public static void main(String[] args) throws InterruptedException {
        reporter.start(3, TimeUnit.SECONDS);

        // ʵ����һ��Gauge
        Gauge<Integer> gauge = new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return queue.size();
            }
        };

        // ע�ᵽ������
        metrics.register(MetricRegistry.name(TestGauges.class, "pending-job", "size"), gauge);

        // ����JMX
        JmxReporter jmxReporter = JmxReporter.forRegistry(metrics).build();
        jmxReporter.start();

        // ģ������
        for (int i = 0; i < 20; i++) {
            queue.add("a");
            Thread.sleep(1000);
        }

    }
}
