package dev.nullwzo.examples.cqrs;

public record CustomerView(String fullName, boolean isAllowedToBuyRestrictedItems) {
}
