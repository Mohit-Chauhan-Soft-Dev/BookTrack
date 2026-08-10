package com.booktrack.circulation.service.impl;

import com.booktrack.circulation.entity.Circulation;
import com.booktrack.circulation.enums.CirculationStatus;
import com.booktrack.circulation.repository.CirculationRepository;
import com.booktrack.circulation.service.CirculationOverdueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CirculationOverdueServiceImpl
        implements CirculationOverdueService {

    private final CirculationRepository circulationRepository;

    @Override
    @Transactional
    public void markOverdueCirculations() {

        LocalDateTime now = LocalDateTime.now();

        List<Circulation> overdueCirculations =
                circulationRepository.findByStatusAndDueAtBefore(
                        CirculationStatus.ACTIVE,
                        now
                );

        for (Circulation circulation : overdueCirculations) {

            circulation.setStatus(
                    CirculationStatus.OVERDUE
            );
        }

        if (!overdueCirculations.isEmpty()) {

            circulationRepository.saveAll(
                    overdueCirculations
            );
        }
    }
}