package com.miro.widrest.domain.impl;

import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Widget;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

@AllArgsConstructor
public final class DbSavedWidget implements DbWidget {
    @Delegate
    private final Widget widget;

    @Delegate
    private final Identifiable id;

}
