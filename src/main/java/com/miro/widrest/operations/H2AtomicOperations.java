package com.miro.widrest.operations;

import com.miro.widrest.db.WidgetStorage;
import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Widget;
import com.miro.widrest.domain.impl.EmptyZIndexWidget;
import com.miro.widrest.domain.impl.PredefinedZIndexWidget;
import com.miro.widrest.domain.impl.UpdatedZIndexWidget;
import lombok.AllArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

@AllArgsConstructor
public final class H2AtomicOperations implements AtomicWidgetOperations {

    private final TransactionTemplate transactionTemplate;

    private final WidgetStorage storage;

    @Override
    public DbWidget create(final Widget widget) {
        return this.transactionTemplate.execute(
                transactionStatus -> {
                    final DbWidget saved;
                    if (widget.getZ() == null) {
                        saved = new EmptyZIndexWidget(this.storage, widget);
                    } else {
                        saved = new PredefinedZIndexWidget(this.storage, widget);
                    }
                    transactionStatus.flush();
                    return saved;
                }
        );
    }

    @Override
    public DbWidget get(final Identifiable id) {
        return this.transactionTemplate.execute(
                transactionStatus -> this.storage.get(new WidgetStorage.SearchById(id))
        );
    }

    @Override
    public DbWidget update(final Identifiable id, final Widget widget) {
        return this.transactionTemplate.execute(transactionStatus -> {
            final DbWidget updated = new UpdatedZIndexWidget(this.storage, widget, id);
            transactionStatus.flush();
            return updated;
        });
    }

    @Override
    public Iterable<? extends DbWidget> getAll() {
        return this.transactionTemplate.execute(transactionStatus -> this.storage.getAll());
    }

    @Override
    public boolean delete(final Identifiable id) {
        return Objects.requireNonNull(
                this.transactionTemplate.execute(transactionStatus -> {
                            final boolean deleted = this.storage.remove(id);
                            transactionStatus.flush();
                            return deleted;
                        }
                )
        );
    }
}
