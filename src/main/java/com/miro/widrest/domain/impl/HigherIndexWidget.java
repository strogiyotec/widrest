package com.miro.widrest.domain.impl;

import com.miro.widrest.domain.Widget;

/**
 * Widget whose z index is one position higher than origin.
 */
public final class HigherIndexWidget extends ImmutableWidget {

    public HigherIndexWidget(final Widget widget) {
        super(widget.getX(), widget.getY(), widget.getZ() + 1, widget.getWidth(), widget.getHeight());
    }
}
