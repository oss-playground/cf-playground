package com.aruna.samples.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.spring.autoconfigure.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class MicrometerConfiguration {

    MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer(MeterRegistry meterRegistry) {
        return mReg -> {
            meterRegistry.config().commonTags("application", "statelessBookService").commonTags("instance", "instance_index_value");
        };
    }

}
