package com.booktrack.publisher.repository;

import com.booktrack.publisher.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PublisherRepository extends
        JpaRepository<Publisher, Long>,
        JpaSpecificationExecutor<Publisher> {

    Optional<Publisher> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

}