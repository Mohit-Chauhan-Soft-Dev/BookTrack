package com.booktrack.bookcopy.repository;

import com.booktrack.bookcopy.entity.BookCopy;
import com.booktrack.bookcopy.enums.BookCopyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BookCopyRepository extends
        JpaRepository<BookCopy, Long>,
        JpaSpecificationExecutor<BookCopy> {

    Optional<BookCopy> findByBarcode(String barcode);

    boolean existsByBarcode(String barcode);

    Page<BookCopy> findByBookId(
            Long bookId,
            Pageable pageable);

    Page<BookCopy> findByStatus(
            BookCopyStatus status,
            Pageable pageable);

    Page<BookCopy> findByBookIdAndStatus(
            Long bookId,
            BookCopyStatus status,
            Pageable pageable);

}