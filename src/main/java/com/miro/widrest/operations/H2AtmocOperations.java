package com.miro.widrest.operations;

import com.miro.widrest.db.WidgetStorage;
import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Widget;
import lombok.AllArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;

@AllArgsConstructor
public final class H2AtmocOperations implements AtomicWidgetOperations {

    private final TransactionTemplate transactionTemplate;

    private final WidgetStorage storage;

    @Override
    public DbWidget create(final Widget widget) {
        return null;
    }

    @Override
    public DbWidget get(final Identifiable id) {
        return null;
    }

    @Override
    public DbWidget update(final Identifiable id, final Widget widget) {
        return null;
    }

    @Override
    public Iterable<? extends DbWidget> getAll() {
        return null;
    }

    @Override
    public boolean delete(final Identifiable id) {
        return false;
    }
}
