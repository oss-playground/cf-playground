package com.aruna.samples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;

@SpringBootApplication
public class StatelessbookserviceApplication extends SpringBootServletInitializer {
	
	@Bean
	JvmGcMetrics gcMetrics() {
		return new JvmGcMetrics();
	}

	@Bean
	JvmThreadMetrics threadMetrics() {
		return new JvmThreadMetrics();
	}

	@Bean
	JvmMemoryMetrics memoryMetrics() {
		return new JvmMemoryMetrics();
	}
	
    public static void main(final String[] args) {
        SpringApplication.run(StatelessbookserviceApplication.class, args);
    }
}
