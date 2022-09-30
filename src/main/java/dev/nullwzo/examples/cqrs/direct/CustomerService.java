package dev.nullwzo.examples.cqrs.direct;

import dev.nullwzo.examples.cqrs.CustomerView;
import dev.nullwzo.examples.cqrs.direct.spi.PersonDataRepository;
import dev.nullwzo.examples.cqrs.spi.CustomerViewRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class CustomerService {
    private final PersonDataRepository personDataRepository;
    private final CustomerViewRepository customerViewRepository;
    private final ViewUpdater viewUpdater;

    public CustomerService(PersonDataRepository personDataRepository, CustomerViewRepository customerViewRepository,
                           ViewUpdater viewUpdater) {
        this.personDataRepository = personDataRepository;
        this.customerViewRepository = customerViewRepository;
        this.viewUpdater = viewUpdater;
    }

    public UUID newCustomerFromPerson(String firstName, String familyName, LocalDate birthDay) {
        var id = UUID.randomUUID();
        personDataRepository.upsert(id, new PersonData(firstName, familyName, birthDay));
        viewUpdater.updateView(id);
        return id;
    }

    public void changeFamilyName(UUID id, String newFamilyName) {
        // here we have a bug, because we forgot to change this check after removing the flags
//        if(personDataRepository.findById(id).isPresent()) {
        personDataRepository.upsert(id, new PersonData(null, newFamilyName, null));
        viewUpdater.updateView(id);
//        }
    }

    public UUID newCustomerFromCompany(String name) {
        throw new UnsupportedOperationException();
    }

    public Optional<CustomerView> findById(UUID id) {
        return customerViewRepository.findById(id);
    }

    public void gdprDeletionFor(UUID id) {
        personDataRepository.deleteById(id);
        customerViewRepository.deleteById(id);
    }
}
