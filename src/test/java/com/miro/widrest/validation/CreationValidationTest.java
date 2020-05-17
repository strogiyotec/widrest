package com.miro.widrest.validation;

import com.miro.widrest.domain.impl.ImmutableWidget;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class CreationValidationTest {

    @Test
    @DisplayName("Test invalid values")
    public void testNonValidValues() {
        final CreateWidgetValidation validation = new CreateWidgetValidation();
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> validation.validate(
                        new ImmutableWidget(null, 1, 1, 1, 1)
                )
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> validation.validate(
                        new ImmutableWidget(1, null, 1, 1, 1)
                )
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> validation.validate(
                        new ImmutableWidget(1, 1, 1, null, 1)
                )
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> validation.validate(
                        new ImmutableWidget(1, 1, 1, 1, null)
                )
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> validation.validate(
                        new ImmutableWidget(1, 1, 1, 1, -100)
                )
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> validation.validate(
                        new ImmutableWidget(1, 1, 1, -100, 1)
                )
        );
    }

    @Test
    @DisplayName("Test valid widgets")
    public void testValidValies() {
        final CreateWidgetValidation validation = new CreateWidgetValidation();
        Assertions.assertDoesNotThrow(() -> {
            validation.validate(new ImmutableWidget(1, 1, 1, 1, 1));
        });
        Assertions.assertDoesNotThrow(() -> {
            validation.validate(new ImmutableWidget(1, 1, null, 1, 1));
        });
    }
}
