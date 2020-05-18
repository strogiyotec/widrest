package com.miro.widrest.operations;

import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Pageable;
import com.miro.widrest.domain.Widget;

public interface AtomicWidgetOperations {

    /**
     * Creates new widget.
     * If widget doesn't have z-index then
     * assign z-index one level lower than lowest one
     * If widget has z-index and this z-index is already
     * presented in Db then move all widgets with bigger z-index
     * one level up and save new one
     *
     * @param widget Widget to save
     * @return Saved widget
     */
    DbWidget create(Widget widget);

    DbWidget get(Identifiable id);

    /**
     * Update widget.
     * <p>
     * If widget has z-index which already presented
     * is storage than move all widgets with bigger
     * z-index one level up
     *
     * @param id     Id
     * @param widget New data
     * @return Updated widget
     */
    DbWidget update(Identifiable id, Widget widget);

    Iterable<? extends DbWidget> getAll(Pageable pageable);

    /**
     * Delete widget.
     *
     * @param id Id
     * @return True if was deleted
     */
    boolean delete(Identifiable id);
}
