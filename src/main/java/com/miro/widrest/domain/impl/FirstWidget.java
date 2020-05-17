package com.miro.widrest.domain.impl;

import com.miro.widrest.domain.Widget;

/**
 * If db is empty then this is the first widget.
 */
public final class FirstWidget extends ImmutableWidget {

    /**
     * Z index of the first widget.
     */
    static final int FIRST_Z_INDEX = 0;

    public FirstWidget(final Widget origin) {
        super(
                origin.getX(),
                origin.getY(),
                FIRST_Z_INDEX,
                origin.getWidth(),
                origin.getHeight()
        );
    }
}
