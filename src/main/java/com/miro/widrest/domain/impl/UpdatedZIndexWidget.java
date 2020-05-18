package com.miro.widrest.domain.impl;

import com.miro.widrest.db.WidgetStorage;
import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Widget;
import lombok.experimental.Delegate;

/**
 * Update db widget whose new z-index is already present in storage.
 */

public final class UpdatedZIndexWidget implements DbWidget {

    @Delegate
    private final DbWidget origin;

    /**
     * Ctor.
     * It moves all widgets whose zindexes are bigger or equal to
     * given withSameZIndex and then update widget with given
     * updateId
     *
     * @param updateId       Id of widget to be updated
     * @param storage        Storage
     * @param toUpdate       New data for widget
     * @param withSameZIndex Widget whose z-index is the same as in
     *                       given toUpdate
     */
    public UpdatedZIndexWidget(
            final WidgetStorage storage,
            final Widget toUpdate,
            final DbWidget withSameZIndex,
            final Identifiable updateId
    ) {
        storage.moveIndexes(withSameZIndex);
        this.origin = storage.update(toUpdate, updateId);
        System.out.println(this.origin);
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
