package com.miro.widrest.validation;

import com.miro.widrest.domain.Widget;

public final class CreateWidgetValidation implements WidgetValidation {
    @Override
    public Widget validate(final Widget widget) throws IllegalArgumentException {
        if (widget.getHeight() < 0) {
            throw new IllegalArgumentException("Height must be non negative");
        }
        if (widget.getWidth() < 0) {
            throw new IllegalArgumentException("Width must by non negative");
        }
        return widget;
    }

    @Override
    public String failReason() {
        return "Height and Width must be non negative";
    }
}
