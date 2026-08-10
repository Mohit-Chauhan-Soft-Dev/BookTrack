package com.booktrack.fine.validator;

import com.booktrack.circulation.entity.Circulation;
import com.booktrack.circulation.enums.CirculationStatus;
import com.booktrack.circulation.repository.CirculationRepository;
import com.booktrack.fine.entity.Fine;
import com.booktrack.fine.repository.FineRepository;
import com.booktrack.exception.BadRequestException;
import com.booktrack.exception.DuplicateResourceException;
import com.booktrack.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FineValidator {

    private final FineRepository fineRepository;

    private final CirculationRepository circulationRepository;

    public Fine validateFineExists(Long id) {

        return fineRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Fine not found with id: " + id
                        )
                );
    }

    public Circulation validateCirculationExists(
            Long circulationId) {

        return circulationRepository.findById(circulationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Circulation not found with id: "
                                        + circulationId
                        )
                );
    }

    public void validateDuplicateFine(
            Long circulationId) {

        if (fineRepository.existsByCirculationId(
                circulationId)) {

            throw new DuplicateResourceException(
                    "Fine already exists for circulation: "
                            + circulationId
            );
        }
    }

    public void validateFineCreation(
            Circulation circulation) {

        if (circulation.getStatus()
                != CirculationStatus.RETURNED) {

            throw new BadRequestException(
                    "Fine can only be created for a returned circulation."
            );
        }

        if (circulation.getReturnedAt() == null) {

            throw new BadRequestException(
                    "Returned date is required before creating a fine."
            );
        }

        if (!circulation.getReturnedAt()
                .isAfter(circulation.getDueAt())) {

            throw new BadRequestException(
                    "Fine cannot be created because the book was returned on time."
            );
        }
    }

    public void validatePayableFine(Fine fine) {

        switch (fine.getStatus()) {

            case PAID ->
                    throw new BadRequestException(
                            "Fine has already been paid."
                    );

            case WAIVED ->
                    throw new BadRequestException(
                            "Waived fine cannot be paid."
                    );

            case UNPAID -> {
                // Valid
            }
        }
    }
}