package com.miro.widrest.domain.impl;

import com.miro.widrest.domain.DbWidget;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public final class MockedDbWidget implements DbWidget {
    private final Integer x;

    private final Integer y;

    private final Integer z;

    private final Integer width;

    private final Integer height;

    private final long id;

    private final long lastModified;

    @Override
    public boolean equals(final Object another) {
        if (this == another) return true;
        final MockedDbWidget that = (MockedDbWidget) another;
        return Objects.equals(this.id, that);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

}
