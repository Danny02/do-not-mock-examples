package dev.nullwzo.examples.cqrs.spi;

import dev.nullwzo.examples.cqrs.CustomerView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CustomerViewMemRepository implements CustomerViewRepository {

    private final Map<UUID, CustomerView> data = new HashMap<>();

    @Override
    public Optional<CustomerView> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    @Override
    public void upsert(UUID id, CustomerView view) {
        data.put(id, view);
    }
}
