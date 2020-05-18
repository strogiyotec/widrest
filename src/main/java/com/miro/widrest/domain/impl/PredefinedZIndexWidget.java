package com.miro.widrest.domain.impl;

import com.miro.widrest.db.WidgetStorage;
import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Widget;

/**
 * Db widget whose z index is predefined.
 */
public final class PredefinedZIndexWidget extends DbWidgetEnvelope {

    /**
     * Ctor.
     * if storage is empty then save this widget
     * If widget with the same z index exists
     * then move it and all widgets with bigger z-indexes
     * one level up
     *
     * @param storage Storage
     * @param toSave  Widget to save
     */
    public PredefinedZIndexWidget(final WidgetStorage storage, final Widget toSave) {
        super(() ->
                {
                    final DbWidget sameZIndexWidget = storage.get(new WidgetStorage.SearchByZIndex(toSave.getZ()));
                    // we already have widget with given index
                    if (!sameZIndexWidget.equals(DbWidget.empty)) {
                        storage.moveIndexes(toSave);
                        return storage.add(toSave);
                    } else {
                        //z-index doesn't exist just create new widget
                        return storage.add(toSave);
                    }
                }
        );
    }
}
