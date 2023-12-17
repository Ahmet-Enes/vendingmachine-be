package com.vending.machine.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
@EnableScheduling
public class TempatureConfig {

    @Scheduled(fixedDelayString = "${cool.machine.delay.string}")
    private void coolTheMachine() {
        System.out.println(new Date() + ": Dummy cooling operation");
    }
}
