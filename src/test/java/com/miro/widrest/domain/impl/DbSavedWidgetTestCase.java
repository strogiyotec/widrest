package com.miro.widrest.domain.impl;

import com.miro.widrest.domain.DbWidget;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class DbSavedWidgetTestCase {

    @Test
    @DisplayName("Test that widget is equals to identifiable")
    public void testEquals() {
        final long id = 1;
        final DbWidget widget = new DbSavedWidget(new MockedWidget(), new ImmutableIdentifier(id));
        final ImmutableIdentifier identifier = new ImmutableIdentifier(id);

        Assertions.assertEquals(
                widget,
                identifier
        );
    }

    @Test
    @DisplayName("Test empty widget properties")
    public void testEmptyWidget() {
        final DbWidget.Empty empty = DbWidget.empty;
        Assertions.assertNull(
                empty.getHeight()
        );
        Assertions.assertNull(
                empty.getWidth()
        );
        Assertions.assertNull(
                empty.getX()
        );
        Assertions.assertNull(
                empty.getY()
        );
        Assertions.assertEquals(
                empty.getId(),
                Integer.MIN_VALUE
        );
        Assertions.assertEquals(
                empty.getLastModified(),
                -1
        );

    }
}
