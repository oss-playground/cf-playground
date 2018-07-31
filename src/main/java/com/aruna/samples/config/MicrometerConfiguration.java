package com.aruna.samples.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.spring.autoconfigure.MeterRegistryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrometerConfiguration {

    MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer(MeterRegistry meterRegistry) {
        return mReg -> {
            meterRegistry.config().commonTags("application", "statelessBookService");
        };
    }

}
