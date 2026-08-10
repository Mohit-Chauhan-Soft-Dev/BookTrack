package com.booktrack.circulation.repository;

import com.booktrack.circulation.entity.Circulation;
import com.booktrack.circulation.enums.CirculationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.time.LocalDateTime;
import java.util.List;

public interface CirculationRepository extends
                JpaRepository<Circulation, Long>,
                JpaSpecificationExecutor<Circulation> {

        Page<Circulation> findByUserId(
                        Long userId,
                        Pageable pageable);

        Page<Circulation> findByBookCopyId(
                        Long bookCopyId,
                        Pageable pageable);

        Page<Circulation> findByStatus(
                        CirculationStatus status,
                        Pageable pageable);

        Page<Circulation> findByUserIdAndStatus(
                        Long userId,
                        CirculationStatus status,
                        Pageable pageable);

        boolean existsByBookCopyIdAndStatus(
                        Long bookCopyId,
                        CirculationStatus status);

        List<Circulation> findByStatusAndDueAtBefore(
                        CirculationStatus status,
                        LocalDateTime dateTime);
}