package com.playground.demo.springsleuth.config;

import brave.sampler.Sampler;
import org.springframework.cloud.sleuth.sampler.ProbabilityBasedSampler;
import org.springframework.cloud.sleuth.sampler.SamplerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZipkinConfig {

//    @Bean
//    public Sampler sampler() {
//        SamplerProperties samplerProperties = new SamplerProperties();
//        samplerProperties.setProbability(1.0f);
//        return new ProbabilityBasedSampler(samplerProperties);
//    }

}
