package com.vending.machine.app.config;

import com.vending.machine.app.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {
    private final TokenRepository repository;

    @Scheduled(fixedDelayString = "${cool.machine.delay.string}")
    private void coolTheMachine() {
        System.out.println(new Date() + ": Dummy cooling operation");
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void deleteExpiredTokens() {
        System.out.println("Deleting expired tokens...");
        repository.findByValidUntilBefore(new Date()).collectList().flatMap(repository::deleteAll).subscribe();
    }
}
