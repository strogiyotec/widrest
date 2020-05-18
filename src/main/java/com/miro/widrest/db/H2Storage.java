package com.miro.widrest.db;

import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.Identifiable;
import com.miro.widrest.domain.Widget;
import com.miro.widrest.domain.impl.DbSavedWidget;
import com.miro.widrest.domain.impl.ImmutableIdentifier;
import com.miro.widrest.domain.impl.ImmutableWidget;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.function.BiPredicate;

@AllArgsConstructor
public final class H2Storage implements WidgetStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean remove(final Identifiable id) {
        return this.jdbcTemplate.update(
                "DELETE from widgets where id = ?",
                id.getId()
        ) != 0;
    }

    @Override
    public DbWidget add(final Widget widget) {
        final KeyHolder holder = new GeneratedKeyHolder();
        final int update = this.jdbcTemplate.update(con -> {
            final PreparedStatement statement = con.prepareStatement("INSERT INTO widgets (x,y,z,width,height,last_updated) VALUES (?,?,?,?,?,CURRENT_TIMESTAMP)");
            statement.setInt(1, widget.getX());
            statement.setInt(2, widget.getY());
            statement.setInt(3, widget.getZ());
            statement.setInt(4, widget.getWidth());
            statement.setInt(5, widget.getHeight());
            return statement;
        }, holder);
        if (update != 0) {
            return new DbSavedWidget(widget, new ImmutableIdentifier(holder.getKey().longValue()));
        } else {
            return null;
        }
    }

    @Override
    public DbWidget update(final Widget widget, final Identifiable id) {
        return null;
    }

    @Override
    public DbWidget get(final BiPredicate<Identifiable, DbWidget> predicate) {
        return this.jdbcTemplate.query("SELECT * from widgets where id = ?", (RowMapper<DbWidget>) (rs, rowNum) -> new DbSavedWidget(
                new ImmutableWidget(
                        rs.getInt("x"),
                        rs.getInt("y"),
                        rs.getInt("z"),
                        rs.getInt("width"),
                        rs.getInt("height")
                ),
                new ImmutableIdentifier(rs.getDate("last_updated").getTime())
        )).stream()
                .filter(widget -> predicate.test(widget, widget))
                .findFirst()
                .orElse(null);
    }

    @Override
    public DbWidget getLastByZIndex(final boolean lowestZIndex) {
        return null;
    }

    @Override
    public Iterable<? extends DbWidget> getAll() {
        return null;
    }

    @Override
    public Iterable<? extends DbWidget> moveIndexes(final Widget lowestIndexWidget) {
        return null;
    }

    @Override
    public boolean exists(final BiPredicate<Identifiable, DbWidget> predicate) {
        return false;
    }

}
