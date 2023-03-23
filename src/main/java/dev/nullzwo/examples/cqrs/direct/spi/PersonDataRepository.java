package dev.nullzwo.examples.cqrs.direct.spi;

import dev.nullzwo.examples.cqrs.direct.PersonData;

import java.util.Optional;
import java.util.UUID;

public interface PersonDataRepository {
    void upsert(UUID id, PersonData data);

    Optional<PersonData> findById(UUID id);

    void deleteById(UUID id);
}
