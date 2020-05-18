package com.miro.widrest.domain.impl;

import com.miro.widrest.db.WidgetStorage;
import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Widget;
import lombok.experimental.Delegate;

/**
 * Db widget whose z index is predefined.
 */

public final class PredefinedZIndexWidget implements DbWidget {

    /**
     * Origin.
     */
    @Delegate
    private final DbWidget origin;

    /**
     * Ctor.
     * if storage is empty then save this widget
     * If storage with the same z index exists
     * then move it and all widgets with bigger z-indexes
     * one level up
     *
     * @param storage Storage
     * @param toSave  Widget to save
     */
    public PredefinedZIndexWidget(final WidgetStorage storage, final Widget toSave) {
        final DbWidget sameZIndexWidget = storage.get(new WidgetStorage.SearchByZIndex(toSave.getZ()));
        // we already have widget with given index
        if (sameZIndexWidget != null) {
            storage.moveIndexes(toSave);
            this.origin = storage.add(toSave);
        } else {
            this.origin = storage.add(toSave);
        }
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
