package com.miro.widrest.validation;

import com.miro.widrest.domain.Widget;

public final class CreateWidgetValidation implements WidgetValidation {
    @Override
    public Widget validate(final Widget widget) throws IllegalArgumentException {
        if (widget.getX() == null) {
            throw new IllegalArgumentException("X must be specified");
        }
        if (widget.getY() == null) {
            throw new IllegalArgumentException("X must be specified");
        }
        if (widget.getHeight() == null) {
            throw new IllegalArgumentException("Height must be specified");
        }
        if (widget.getWidth() == null) {
            throw new IllegalArgumentException("Width must be specified");
        }
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
        return "All attributes must be specified.Z is optional. Width and Height are non negative";
    }
}
