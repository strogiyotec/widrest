package com.miro.widrest.domain.impl;

import com.miro.widrest.db.WidgetStorage;
import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Widget;

/**
 * Update db widget whose new z-index is already present in storage.
 */
public final class UpdatedZIndexWidget extends DbWidgetEnvelope {

    /**
     * Ctor.
     * Create updated widget
     * If widget with given id doesn't exist then just return empty
     * If widget has new z-index then move all widgets with bigger indexes one level
     * up and then update target widget
     *
     * @param updateId Id of widget to be updated
     * @param storage  Storage
     * @param toUpdate New data for widget
     */
    public UpdatedZIndexWidget(
            final WidgetStorage storage,
            final Widget toUpdate,
            final Identifiable updateId
    ) {
        super(() -> {
            final DbWidget dbWidget = storage.get(new WidgetStorage.SearchById(updateId));
            //non existing id
            if (dbWidget.equals(DbWidget.empty)) {
                return DbWidget.empty;
            } else {
                final DbWidget sameIndexWidget = storage.get(new WidgetStorage.SearchByZIndex(toUpdate.getZ()));
                if (!sameIndexWidget.equals(DbWidget.empty) && !sameIndexWidget.equals(dbWidget)) {
                    //if widget with this z index exists and it's not a target widget then move it and all whose z index is bigger to one level up
                    storage.incrementIndexes(sameIndexWidget.getZ());
                }
                return storage.update(toUpdate, updateId);
            }
        });
    }
}
