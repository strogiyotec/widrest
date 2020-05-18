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
                Optional.ofNullable(storage.getLast(true))
                        //create new one with one index lower
                        .map(lowestIndexWidget -> storage.add(new LowerIndexWidget(lowestIndexWidget)))
                        //otherwise if db is empty and just create new widget
                        .orElse(storage.add(new FirstWidget(toSave)));
    }
}
