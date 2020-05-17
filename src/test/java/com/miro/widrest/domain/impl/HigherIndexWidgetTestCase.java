package com.miro.widrest.domain.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class HigherIndexWidgetTestCase {

    @Test
    @DisplayName("Test that HigherIndexWidget has z index bigger then origin")
    public void testHigherIndex() {
        final int zIndex = 4;
        final HigherIndexWidget higherIndexWidget = new HigherIndexWidget(new ImmutableWidget(
                1,
                1,
                zIndex,
                10,
                10
        ));
        Assertions.assertEquals(higherIndexWidget.getZ(), zIndex + 1);
    }
}
