package com.miro.widrest.domain;

public interface DbWidget extends Identifiable, Widget {

    Empty empty = new Empty();

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
    }
}
