package dev.nullzwo.examples.cqrs.flag;

import dev.nullzwo.examples.cqrs.CustomerView;
import dev.nullzwo.examples.cqrs.flag.spi.PersonDataMemRepository;
import dev.nullzwo.examples.cqrs.spi.CustomerViewMemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FlagCustomerServiceFakeTest {

    CustomerService service;

    @BeforeEach
    void setup() {
        var pdRepo = new PersonDataMemRepository();
        var cvRepo = new CustomerViewMemRepository();
        var updater = new ViewUpdater(pdRepo, cvRepo);
        service = new CustomerService(pdRepo, cvRepo, updater);
    }

    @Test
    void shouldReturnEmpty() {
        assertThat(service.findById(UUID.randomUUID())).isEmpty();
    }

    @Test
    void shouldFindCreatedCustomer() {
        var id = service.newCustomerFromPerson("Daniel", "Heinrich",
                                               LocalDate.of(1980, 11, 20));

        assertThat(service.findById(id)).isNotEmpty();
    }

    @Test
    void shouldNotFindDeleted() {
        var id = service.newCustomerFromPerson("Daniel", "Heinrich",
                                               LocalDate.of(1980, 11, 20));

        service.gdprDeletionFor(id);

        assertThat(service.findById(id)).isEmpty();
    }

    @Test
    void shouldChangeName() {
        var id = service.newCustomerFromPerson("Daniel", "Heinrich",
                                               LocalDate.of(1980, 11, 20));

        service.changeFamilyName(id, "Müller");

        assertThat(service.findById(id).map(CustomerView::fullName)).get().asString().endsWith("Müller");
    }

    @Test
    void canNotChangeNameOfDeleted() {
        var id = service.newCustomerFromPerson("Daniel", "Heinrich",
                                               LocalDate.of(1980, 11, 20));

        service.gdprDeletionFor(id);
        service.changeFamilyName(id, "Müller");

        assertThat(service.findById(id)).isEmpty();
    }
}
