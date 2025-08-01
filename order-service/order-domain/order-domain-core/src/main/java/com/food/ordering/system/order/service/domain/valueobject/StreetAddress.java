package com.food.ordering.system.order.service.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public class StreetAddress {

    private final UUID id;
    private final String street;
    private final String postalCode;
    private final String city;

    public StreetAddress(String city, UUID id, String street, String postalCode) {
        this.city = city;
        this.id = id;
        this.street = street;
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public UUID getId() {
        return id;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreet() {
        return street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreetAddress that = (StreetAddress) o;
        return Objects.equals(street, that.street) && Objects.equals(postalCode, that.postalCode) && Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, postalCode, city);
    }
}
