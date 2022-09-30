package dev.nullwzo.examples.cqrs.direct;

import java.time.LocalDate;

public record PersonData(String firstName, String familyName, LocalDate birthday) {
}
