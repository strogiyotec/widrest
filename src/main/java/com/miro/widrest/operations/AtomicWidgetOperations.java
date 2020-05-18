package com.miro.widrest.operations;

import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Widget;

public interface AtomicWidgetOperations {

    DbWidget create(Widget widget);

    DbWidget get(Identifiable id);

    DbWidget update(Identifiable id, Widget widget);

    Iterable<? extends DbWidget> getAll();

    boolean delete(Identifiable id);
}
