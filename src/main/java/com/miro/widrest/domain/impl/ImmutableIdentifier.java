package com.miro.widrest.domain.impl;

import com.miro.widrest.domain.Identifiable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public final class ImmutableIdentifier implements Identifiable {

    private final long id;

    @Override
    public long getId() {
        return this.id;
    }
}
