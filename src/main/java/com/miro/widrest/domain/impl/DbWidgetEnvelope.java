package com.miro.widrest.domain.impl;

import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;

import java.util.Objects;
import java.util.function.Supplier;

public class DbWidgetEnvelope implements DbWidget {

    private final DbWidget origin;

    public DbWidgetEnvelope(final DbWidget origin) {
        this.origin = origin;
    }


    public DbWidgetEnvelope(final Supplier<DbWidget> origin) {
        this.origin = origin.get();
    }

    @Override
    public final long getLastModified() {
        return this.origin.getLastModified();
    }


    @Override
    public final long getId() {
        return this.origin.getId();
    }

    @Override
    public final Integer getX() {
        return this.origin.getX();
    }

    @Override
    public final Integer getY() {
        return this.origin.getY();
    }

    @Override
    public final Integer getZ() {
        return this.origin.getZ();
    }

    @Override
    public final Integer getWidth() {
        return this.origin.getWidth();
    }

    @Override
    public final Integer getHeight() {
        return this.origin.getHeight();
    }

    @Override
    public final boolean equals(final Object another) {
        if (this == another) return true;
        final Identifiable that = (Identifiable) another;
        return Objects.equals(this.origin, that);
    }

    @Override
    public final int hashCode() {
        return this.origin.hashCode();
    }

}
