package dev.nullzwo.examples.cqrs.flag;

import dev.nullzwo.examples.cqrs.CustomerView;
import dev.nullzwo.examples.cqrs.flag.spi.PersonDataRepository;
import dev.nullzwo.examples.cqrs.spi.CustomerViewRepository;

import java.time.LocalDate;
import java.util.UUID;

public class ViewUpdater {

    private final PersonDataRepository personDataRepository;

    private final CustomerViewRepository customerViewRepository;

    public ViewUpdater(PersonDataRepository personDataRepository, CustomerViewRepository customerViewRepository) {
        this.personDataRepository = personDataRepository;
        this.customerViewRepository = customerViewRepository;
    }

    public void updateView(UUID id) {
        personDataRepository.findById(id).ifPresent(pd -> {
            if (pd.isDeleted()) {
                customerViewRepository.deleteById(id);
            } else {
                var eighteenYearsAgo = LocalDate.now().minusYears(18);
                var view = new CustomerView(pd.firstName() + " " + pd.familyName(),
                                            pd.birthday().isBefore(eighteenYearsAgo));
                customerViewRepository.upsert(id, view);
            }
        });
    }
}
