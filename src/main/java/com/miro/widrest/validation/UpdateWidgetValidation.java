package com.miro.widrest.validation;

import com.miro.widrest.domain.Widget;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class UpdateWidgetValidation implements WidgetValidation {

    private final WidgetValidation createWidgetValidation;

    @Override
    public Widget validate(final Widget widget) throws IllegalArgumentException {
        if (widget.getZ() == null) {
            throw new IllegalArgumentException("Z must be specified");
        }
        return this.createWidgetValidation.validate(widget);
    }

    @Override
    public String failReason() {
        return "All attributes must be specified during update";
    }
}
