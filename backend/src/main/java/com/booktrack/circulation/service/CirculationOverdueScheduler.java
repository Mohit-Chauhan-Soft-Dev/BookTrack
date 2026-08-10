package com.booktrack.circulation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CirculationOverdueScheduler {

    private final CirculationOverdueService circulationOverdueService;

    @Scheduled(fixedDelay = 60000)
    public void markOverdueCirculations() {

        circulationOverdueService.markOverdueCirculations();
    }
}