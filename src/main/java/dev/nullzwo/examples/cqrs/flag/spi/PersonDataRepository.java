package dev.nullzwo.examples.cqrs.flag.spi;

import dev.nullzwo.examples.cqrs.flag.PersonData;

import java.util.Optional;
import java.util.UUID;

public interface PersonDataRepository {

    /**
     * Updates data for a specific identifier. Updates are done by merging non null fields into any existing values.
     *
     * @param id
     * @param data
     */
    void upsert(UUID id, PersonData data);

    Optional<PersonData> findById(UUID id);
}
