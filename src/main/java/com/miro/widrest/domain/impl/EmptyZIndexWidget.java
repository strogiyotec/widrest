package com.miro.widrest.domain.impl;

import com.miro.widrest.db.WidgetStorage;
import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Widget;
import lombok.experimental.Delegate;

import java.util.Optional;

public final class EmptyZIndexWidget implements DbWidget {

    @Delegate
    private final DbWidget origin;

    public EmptyZIndexWidget(final WidgetStorage storage, final Widget toSave) {
        this.origin =
                Optional.ofNullable(storage.getLastByZIndex())
                        //create new one with one index lower
                        .map(lowestIndexWidget -> storage.add(new LowerIndexWidget(lowestIndexWidget)))
                        //otherwise if db is empty and just create new widget
                        .orElse(storage.add(new FirstWidget(toSave)));
    }


    @Override
    public boolean equals(final Object o) {
        return this.origin.equals(o);
    }

    @Override
    public int hashCode() {
        return this.origin.hashCode();
    }
}
