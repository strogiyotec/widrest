package com.miro.widrest.domain;

import java.util.Objects;

public interface DbWidget extends Identifiable, Widget {

    Empty empty = new Empty();

    long getLastModified();

    @Override
    boolean equals(Object another);

    class Empty implements DbWidget {

        @Override
        public long getId() {
            return Integer.MIN_VALUE;
        }

        @Override
        public Integer getX() {
            return null;
        }

        @Override
        public Integer getY() {
            return null;
        }

        @Override
        public Integer getZ() {
            return null;
        }

        @Override
        public Integer getWidth() {
            return null;
        }

        @Override
        public Integer getHeight() {
            return null;
        }

        @Override
        public long getLastModified() {
            return -1;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.getId());
        }

        @Override
        public boolean equals(final Object another) {
            if (this == another) return true;
            final Identifiable that = (Identifiable) another;
            return Objects.equals(this.getId(), that.getId());
        }
    }
}
