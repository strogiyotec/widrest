package com.miro.widrest.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * Default size is 10.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public final class Pageable {
    private int page;

    private int size = 10;

    @JsonCreator
    public void setSize(final int size) {
        this.size = size > 500 ? 500 : size;
    }

    @JsonCreator
    public void setPage(final int page) {
        this.page = page < 0 ? 0 : page;
    }

    public long skip() {
        return this.page * this.size;
    }
}
