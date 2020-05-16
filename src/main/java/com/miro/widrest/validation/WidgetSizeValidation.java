package com.miro.widrest.validation;

import com.miro.widrest.domain.Widget;

public final class WidgetSizeValidation implements WidgetValidation {
    @Override
    public Widget validate(final Widget widget) {
        if (widget.getHeight() < 0) {
            throw new IllegalStateException("Height must be non negative");
        }
        if (widget.getWidth() < 0) {
            throw new IllegalStateException("Width must by non negative");
        }
        return widget;
    }

    @Override
    public String failReason() {
        return "Height and Width must be non negative";
    }
}
