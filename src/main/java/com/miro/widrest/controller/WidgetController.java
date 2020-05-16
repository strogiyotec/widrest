package com.miro.widrest.controller;

import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.impl.ImmutableIdentifier;
import com.miro.widrest.domain.impl.ImmutableWidget;
import com.miro.widrest.service.WidgetService;
import com.miro.widrest.validation.WidgetValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public final class WidgetController {

    private final WidgetService widgetService;

    private final Map<WidgetValidation.Type, WidgetValidation> validation;

    @Autowired
    public WidgetController(
            final WidgetService widgetService,
            final Map<WidgetValidation.Type, WidgetValidation> validation
    ) {
        this.widgetService = widgetService;
        this.validation = validation;
    }

    @GetMapping("/widgets/{id}")
    public ResponseEntity<? extends DbWidget> get(@PathVariable("id") final long id) {
        final DbWidget dbWidget = this.widgetService.get(new ImmutableIdentifier(id));
        if (dbWidget == DbWidget.empty) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(dbWidget);
        }
    }

    @GetMapping("/widgets")
    public ResponseEntity<Object> getAll() {
        final List<DbWidget> widgets = new ArrayList<>(16);
        this.widgetService.getAll().forEach(widgets::add);
        if (widgets.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(widgets);
        }
    }

    @PutMapping("/widgets/{id}")
    public ResponseEntity<Object> update(
            @RequestBody final ImmutableWidget widget,
            @PathVariable("id") final long id) {
        try {
            final DbWidget updated = this.widgetService.update(new ImmutableIdentifier(id), widget);
            if (updated == DbWidget.empty) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(updated);
            }
        } catch (final IllegalArgumentException exc) {
            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "description",
                                    this.validation.get(WidgetValidation.Type.UPDATE).failReason(),
                                    "reason",
                                    exc.getMessage()
                            )
                    );
        }
    }

    @PostMapping("/widgets")
    public ResponseEntity<Object> create(@RequestBody final ImmutableWidget widget) {
        try {
            return
                    ResponseEntity.ok(
                            this.widgetService.create(
                                    this.validation.get(WidgetValidation.Type.INSERT).validate(widget)
                            )
                    );
        } catch (final IllegalArgumentException exc) {
            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "description",
                                    this.validation.get(WidgetValidation.Type.INSERT).failReason(),
                                    "reason",
                                    exc.getMessage()
                            )
                    );
        }
    }

    @DeleteMapping("/widgets/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") final long id) {
        final DbWidget deleted = this.widgetService.delete(new ImmutableIdentifier(id));
        if (deleted == DbWidget.empty) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}