package com.miro.widrest.domain.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class FirstWidgetTestCase {

    @Test
    @DisplayName("Test that First widget has specific position")
    public void testFirstWidgetIndex() {
        final FirstWidget firstWidget = new FirstWidget(
                new ImmutableWidget(
                        1,
                        1,
                        Integer.MAX_VALUE,
                        10,
                        10
                )
        );
        Assertions.assertEquals(
                firstWidget.getZ(),
                FirstWidget.FIRST_Z_INDEX
        );

    }
}
