package dev.nullzwo.examples.cqrs.flag;

import dev.nullzwo.examples.cqrs.CustomerView;
import dev.nullzwo.examples.cqrs.flag.spi.PersonDataRepository;
import dev.nullzwo.examples.cqrs.spi.CustomerViewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FlagCustomerServiceMockTest {

    CustomerService service;
    PersonDataRepository pdRepo;
    CustomerViewRepository cvRepo;

    @BeforeEach
    void setup() {
        pdRepo = mock(PersonDataRepository.class);
        cvRepo = mock(CustomerViewRepository.class);
        var updater = new ViewUpdater(pdRepo, cvRepo);
        service = new CustomerService(pdRepo, cvRepo, updater);
    }

    @Test
    void shouldUpdateViewOnCreate() {
        when(pdRepo.findById(any())).thenReturn(Optional.of(new PersonData(
                "Daniel", "Heinrich",
                LocalDate.of(1980, 11, 20)
        )));

        var id = service.newCustomerFromPerson("Daniel", "Heinrich",
                                               LocalDate.of(1980, 11, 20));


        verify(cvRepo).upsert(id, new CustomerView("Daniel Heinrich", true));
    }

    @Test
    void shouldUpdateViewOnDelete() {
        var id = UUID.randomUUID();

        when(pdRepo.findById(id)).thenReturn(Optional.of(new PersonData(
                "Daniel", "Heinrich",
                LocalDate.of(1980, 11, 20),
                true
        )));

        service.gdprDeletionFor(id);

        verify(pdRepo).upsert(id, new PersonData(
                null, null,
                null,
                true
        ));
        verify(cvRepo).deleteById(id);
    }

    @Test
    void shouldUpdateViewOnNameChange() {
        var id = UUID.randomUUID();

        when(pdRepo.findById(id)).thenReturn(Optional.of(new PersonData(
                "Daniel", "Müller",
                LocalDate.of(1980, 11, 20),
                false
        )));

        service.changeFamilyName(id, "Müller");

        verify(pdRepo).upsert(id, new PersonData(
                null, "Müller",
                null
        ));

        verify(cvRepo).upsert(id, new CustomerView("Daniel Müller", true));
    }
}
