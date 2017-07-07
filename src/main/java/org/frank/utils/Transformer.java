package org.frank.utils;

public interface Transformer<S, T> {
    abstract class SelfTransformer<S, T> implements Transformer<S, T> {
            public T transform() {
                return transform((S)this);
            }
    }
    T transform(S entity);
}

