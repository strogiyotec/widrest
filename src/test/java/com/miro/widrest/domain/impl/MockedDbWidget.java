package com.miro.widrest.domain.impl;

import com.miro.widrest.domain.DbWidget;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class MockedDbWidget implements DbWidget {
    private final Integer x;

    private final Integer y;

    private final Integer z;

    private final Integer width;

    private final Integer height;

    private final long id;

    private final long lastModified;

}
