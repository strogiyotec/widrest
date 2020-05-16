package com.miro.widrest.service;

import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Widget;

public interface WidgetService {

    DbWidget create(Widget widget);

    DbWidget get(Identifiable id);

    DbWidget update(Identifiable id, Widget widget);

    Iterable<? extends DbWidget> getAll();
}
