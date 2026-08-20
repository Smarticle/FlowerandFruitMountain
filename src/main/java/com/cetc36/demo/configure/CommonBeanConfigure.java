package com.cetc36.demo.configure;


import com.cetc36.codeeye.monitor.WebMonitorAspect;
import com.cetc36.codeeye.monitor.biz.BizMetric;
import com.cetc36.codeeye.monitor.biz.BizMetricStd;
import com.cetc36.codeeye.output.MetricOutput;
import com.cetc36.codeeye.output.MetricOutputStd;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonBeanConfigure {

    @Autowired
    private MeterRegistry meterRegistry;

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public WebMonitorAspect webMonitorAspect() {
        final String[] successTagArr = new String[]{"200"};
        return new WebMonitorAspect("com.cetc36.", null, "code", successTagArr);
    }

    @Bean
    public BizMetric bizMetric() {
        return new BizMetricStd(meterRegistry, appName);
    }

    @Bean(name = MetricOutput.UNIFIED_BEAN_NAME)
    public MetricOutput unifiedMetricOutput() {
        return new MetricOutputStd(meterRegistry, appName);
    }
}
