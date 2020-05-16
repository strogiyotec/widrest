package com.miro.widrest.validation;

import com.miro.widrest.domain.Widget;

public interface WidgetValidation {
    Widget validate(Widget widget);

    String failReason();
}
