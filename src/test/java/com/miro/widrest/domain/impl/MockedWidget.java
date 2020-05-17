package com.miro.widrest.domain.impl;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Mocked widget.
 * It is used if we are not interested in widget attributes
 */
public final class MockedWidget extends ImmutableWidget {

    public MockedWidget() {
        this(
                ThreadLocalRandom.current().nextInt(0, 50)
        );
    }

    public MockedWidget(final int zIndex) {
        super(1, 1, zIndex, 1, 1);
    }
}
