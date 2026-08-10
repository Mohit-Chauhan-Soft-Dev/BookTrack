package com.booktrack.fine.entity;

import com.booktrack.circulation.entity.Circulation;
import com.booktrack.common.entity.BaseEntity;
import com.booktrack.fine.enums.FineStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "fines",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_fine_circulation",
                        columnNames = "circulation_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fine extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "circulation_id",
            nullable = false
    )
    private Circulation circulation;

    @Column(
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal amount;

    @Column(
            nullable = false,
            length = 500
    )
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    @Builder.Default
    private FineStatus status = FineStatus.UNPAID;

    private LocalDateTime paidAt;

    @Column(length = 1000)
    private String notes;
}