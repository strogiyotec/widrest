package com.miro.widrest.domain.impl;

import com.miro.widrest.domain.Identifiable;

/**
 * Mocked identifiable.
 * It is used when we are not interested in id
 */
public final class MockedIdentifier implements Identifiable {
    @Override
    public long getId() {
        return Integer.MIN_VALUE;
    }
}
