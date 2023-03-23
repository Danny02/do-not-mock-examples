package dev.nullzwo.examples.cqrs;

public record CustomerView(String fullName, boolean isAllowedToBuyRestrictedItems) {
}
