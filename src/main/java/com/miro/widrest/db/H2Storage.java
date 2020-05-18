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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.util.List;
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
            final PreparedStatement statement = con.prepareStatement("INSERT INTO widgets (x,y,z,width,height,last_updated) VALUES (?,?,?,?,?,CURRENT_TIMESTAMP)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, widget.getX());
            statement.setInt(2, widget.getY());
            statement.setInt(3, widget.getZ());
            statement.setInt(4, widget.getWidth());
            statement.setInt(5, widget.getHeight());
            return statement;
        }, holder);
        if (update != 0) {
            return new DbSavedWidget(widget, new ImmutableIdentifier((Long) holder.getKeyList().get(0).get("ID")));
        } else {
            return DbWidget.empty;
        }
    }

    @Override
    public DbWidget update(final Widget widget, final Identifiable id) {
        final int update = this.jdbcTemplate.update(
                "UPDATE widgets set x=?,y=?,width=?,height=?,last_updated = CURRENT_TIMESTAMP where id = ?",
                widget.getX(),
                widget.getY(),
                widget.getWidth(),
                widget.getHeight(),
                id.getId()
        );
        if (update != 0) {
            return new DbSavedWidget(widget, id);
        }
        return DbWidget.empty;
    }

    @Override
    public DbWidget get(final BiPredicate<Identifiable, DbWidget> predicate) {
        return this.jdbcTemplate.query("SELECT * from widgets ;", new WidgetMapper())
                .stream()
                .filter(widget -> predicate.test(widget, widget))
                .findFirst()
                .orElse(DbWidget.empty);
    }

    @Override
    public DbWidget getLastByZIndex() {
        final List<DbWidget> widgets = this.jdbcTemplate.query("SELECT * from widgets order by z desc limit 1;", new WidgetMapper());
        if (widgets.isEmpty()) {
            return DbWidget.empty;
        } else {
            return widgets.get(0);
        }
    }

    @Override
    public Iterable<? extends DbWidget> getAll() {
        return this.jdbcTemplate.query(
                "SELECT * from widgets order by z desc;",
                new WidgetMapper()
        );
    }

    @Override
    public Iterable<? extends DbWidget> moveIndexes(final Widget lowestIndexWidget) {
        final List<DbWidget> widgets = this.jdbcTemplate.query("SELECT * from widgets where z >=?", new WidgetMapper(), lowestIndexWidget.getZ());
        this.jdbcTemplate.update("UPDATE widgets set z = z+1 where z>=?", lowestIndexWidget.getZ());
        return widgets;
    }

    @Override
    public boolean exists(final BiPredicate<Identifiable, DbWidget> predicate) {
        return !this.get(predicate).equals(DbWidget.empty);
    }

    static final class WidgetMapper implements RowMapper<DbWidget> {

        @Override
        public DbWidget mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new DbSavedWidget(
                    new ImmutableWidget(
                            rs.getInt("x"),
                            rs.getInt("y"),
                            rs.getInt("z"),
                            rs.getInt("width"),
                            rs.getInt("height")
                    ),
                    new ImmutableIdentifier(rs.getLong("id")),
                    ((OffsetTime) rs.getObject("last_updated")).toEpochSecond(LocalDate.now())
            );
        }
    }

}
