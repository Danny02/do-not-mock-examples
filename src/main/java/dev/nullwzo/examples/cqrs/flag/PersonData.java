package dev.nullwzo.examples.cqrs.flag;

import java.time.LocalDate;

public record PersonData(String firstName, String familyName, LocalDate birthday, boolean isDeleted) {
    public PersonData(String firstName, String familyName, LocalDate birthday) {
        this(firstName, familyName, birthday, false);
    }
}
