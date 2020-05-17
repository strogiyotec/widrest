package com.miro.widrest;

import com.miro.widrest.domain.DbWidget;
import lombok.experimental.UtilityClass;
import org.assertj.core.util.Lists;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class Utils {

    /**
     * Gets all z indexes as single list.
     *
     * @param widgets Widgets
     * @return List of z indexes
     */
    public static List<Integer> indexes(final Iterable<? extends DbWidget> widgets) {
        return Lists.newArrayList(widgets)
                .stream()
                .map(DbWidget::getZ)
                .collect(Collectors.toList());
    }
}
