package com.miro.widrest.domain.impl;

import com.miro.widrest.domain.Identifiable;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public final class ImmutableIdentifier implements Identifiable {

    private final long id;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ImmutableIdentifier that = (ImmutableIdentifier) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
