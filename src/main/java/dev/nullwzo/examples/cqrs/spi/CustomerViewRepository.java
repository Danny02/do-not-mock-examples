package dev.nullwzo.examples.cqrs.spi;

import dev.nullwzo.examples.cqrs.CustomerView;

import java.util.Optional;
import java.util.UUID;

public interface CustomerViewRepository {
    Optional<CustomerView> findById(UUID id);

    void deleteById(UUID id);

    void upsert(UUID id, CustomerView view);
}
