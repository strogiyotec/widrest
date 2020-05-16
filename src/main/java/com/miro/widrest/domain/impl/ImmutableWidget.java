package com.miro.widrest.domain.impl;

import com.miro.widrest.domain.Widget;
import lombok.AllArgsConstructor;

/**
 * Widget which is immutable.
 */
@AllArgsConstructor
public class ImmutableWidget implements Widget {

    private final Integer x;

    private final Integer y;

    private final Integer z;

    private final Integer width;

    private final Integer height;

    @Override
    public final Integer getX() {
        return this.x;
    }

    @Override
    public final Integer getY() {
        return this.y;
    }

    @Override
    public final Integer getZ() {
        return this.z;
    }

    @Override
    public final Integer getWidth() {
        return this.width;
    }

    @Override
    public final Integer getHeight() {
        return this.height;
    }
}
