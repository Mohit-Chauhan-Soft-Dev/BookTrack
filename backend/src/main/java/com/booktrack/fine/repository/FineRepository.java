package com.booktrack.fine.repository;

import com.booktrack.fine.entity.Fine;
import com.booktrack.fine.enums.FineStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface FineRepository extends
        JpaRepository<Fine, Long>,
        JpaSpecificationExecutor<Fine> {

    Optional<Fine> findByCirculationId(Long circulationId);

    boolean existsByCirculationId(Long circulationId);

    Page<Fine> findByStatus(
            FineStatus status,
            Pageable pageable
    );

    Page<Fine> findByCirculationUserId(
            Long userId,
            Pageable pageable
    );
}