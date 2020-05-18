package com.miro.widrest.domain.impl;

import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Widget;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
public final class DbSavedWidget implements DbWidget {
    @Delegate
    private final Widget widget;

    @Delegate
    private final Identifiable id;

    private final long lastModified;

    public DbSavedWidget(final Widget widget, final Identifiable id) {
        this.widget = widget;
        this.id = id;
        this.lastModified = new Date().getTime();
    }

    @Override
    public long getLastModified() {
        return this.lastModified;
    }

    @Override
    public boolean equals(final Object another) {
        if (this == another) return true;
        final Identifiable that = (Identifiable) another;
        return Objects.equals(this.id, that);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
