package com.miro.widrest.controller;

import com.miro.widrest.domain.DbWidget;
import com.miro.widrest.domain.impl.ConstantIdentifiable;
import com.miro.widrest.domain.impl.ImmutableWidget;
import com.miro.widrest.service.WidgetService;
import com.miro.widrest.validation.WidgetValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public final class WidgetController {

    private final WidgetService widgetService;

    private final WidgetValidation validation;

    @Autowired
    public WidgetController(final WidgetService widgetService, final WidgetValidation validation) {
        this.widgetService = widgetService;
        this.validation = validation;
    }

    @GetMapping("/widgets/{id}")
    public ResponseEntity<? extends DbWidget> get(@PathVariable("id") final long id) {
        final DbWidget dbWidget = this.widgetService.get(new ConstantIdentifiable(id));
        if (dbWidget == DbWidget.empty) {
            return ResponseEntity.noContent().build();
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

    @PostMapping("/widgets")
    public ResponseEntity<Object> create(@RequestBody final ImmutableWidget widget) {
        try {
            return
                    ResponseEntity.ok(
                            this.widgetService.create(
                                    this.validation.validate(widget)
                            )
                    );
        } catch (IllegalStateException exc) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("reason", this.validation.failReason()));
        }
    }

}