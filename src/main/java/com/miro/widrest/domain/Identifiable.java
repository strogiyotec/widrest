package com.miro.widrest.domain;

public interface Identifiable {
    long getId();

    @Override
    int hashCode();

    @Override
    boolean equals(Object another);
}
