package com.miro.widrest.domain.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class LowerIndexWidgetTestCase {

    @Test
    @DisplayName("Test that LowerIndexWidget has lower zIndex than origin")
    public void testLowerIndex() {
        final int zIndex = 4;
        final LowerIndexWidget lowerIndexWidget = new LowerIndexWidget(
                new ImmutableWidget(
                        1,
                        1,
                        zIndex,
                        10,
                        10
                )
        );
        Assertions.assertEquals(lowerIndexWidget.getZ(), zIndex - 1);
    }
}
