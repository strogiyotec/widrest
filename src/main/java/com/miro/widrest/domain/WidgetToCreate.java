package com.miro.widrest.domain;

import com.miro.widrest.domain.impl.ImmutableWidget;

import javax.validation.constraints.NotNull;

public final class WidgetToCreate extends ImmutableWidget {

    public WidgetToCreate(
            @NotNull final Integer x,
            @NotNull final Integer y,
            final Integer z,
            @NotNull final Integer width,
            @NotNull final Integer height
    ) {
        super(x, y, z, width, height);
    }
}
