package com.miro.widrest.domain.impl;

import com.miro.widrest.db.WidgetStorage;
import com.miro.widrest.domain.Widget;

import java.util.Optional;

/**
 * Widget without z-index.
 */
public final class EmptyZIndexWidget extends DbWidgetEnvelope {

    /**
     * Ctor.
     * If widget doesn't have z-index
     * then assign the one which is one level lower
     * than min z-index in storage
     *
     * @param storage Storage
     * @param toSave  Widget To save
     */
    public EmptyZIndexWidget(final WidgetStorage storage, final Widget toSave) {
        super(
                Optional.ofNullable(storage.getLastByZIndex())
                        //create new one with one index lower
                        .map(lowestIndexWidget -> storage.add(new LowerIndexWidget(lowestIndexWidget)))
                        //otherwise if db is empty then just create new widget
                        .orElse(storage.add(new FirstWidget(toSave)))
        );

    }
}
