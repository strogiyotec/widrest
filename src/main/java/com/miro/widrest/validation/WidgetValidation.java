package com.miro.widrest.validation;

import com.miro.widrest.domain.Widget;

public interface WidgetValidation {
    Widget validate(Widget widget) throws IllegalArgumentException;

    String failReason();

    enum Type {
        UPDATE, INSERT
    }
}
