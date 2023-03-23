package dev.nullzwo.examples.cqrs.direct.spi;

import dev.nullzwo.examples.cqrs.direct.PersonData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;

public class PersonDataMemRepository implements PersonDataRepository {

    private final Map<UUID, PersonData> store = new HashMap<>();

    @Override
    public void upsert(UUID id, PersonData data) {
        var orig = findById(id);

        var next = new PersonData(
                ofNullable(data.firstName()).or(() -> orig.map(PersonData::firstName)).orElse(null),
                ofNullable(data.familyName()).or(() -> orig.map(PersonData::familyName)).orElse(null),
                ofNullable(data.birthday()).or(() -> orig.map(PersonData::birthday)).orElse(null)
        );

        store.put(id, next);
    }

    @Override
    public Optional<PersonData> findById(UUID id) {
        return ofNullable(store.get(id));
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }
}
