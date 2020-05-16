package com.miro.widrest.validation;

import com.miro.widrest.domain.Widget;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class UpdateWidgetValidation implements WidgetValidation {

    private final WidgetValidation createWidgetValidation;

    @Override
    public Widget validate(final Widget widget) throws IllegalArgumentException {
        if (widget.getX() == null) {
            throw new IllegalArgumentException("X must be specified");
        }
        if (widget.getY() == null) {
            throw new IllegalArgumentException("Y must be specified");
        }
        if (widget.getZ() == null) {
            throw new IllegalArgumentException("Z must by specified");
        }
        if (widget.getWidth() == null) {
            throw new IllegalArgumentException("Width has to be specified");
        }
        if (widget.getHeight() == null) {
            throw new IllegalArgumentException("Height has to be specified");
        }
        return this.createWidgetValidation.validate(widget);
    }

    @Override
    public String failReason() {
        return "You can't delete attributes so all attributes have to be present.\n" + this.createWidgetValidation.failReason();
    }
}
