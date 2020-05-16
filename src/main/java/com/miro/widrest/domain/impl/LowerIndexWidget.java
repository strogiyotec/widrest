package com.miro.widrest.domain.impl;

import com.miro.widrest.domain.Widget;

/**
 * Widget whose z index in one position lower than given origin.
 */
public final class LowerIndexWidget extends ImmutableWidget {

    public LowerIndexWidget(final Widget origin) {
        super(
                origin.getX(),
                origin.getY(),
                origin.getZ() - 1,
                origin.getWidth(),
                origin.getHeight()
            );
    }
}
