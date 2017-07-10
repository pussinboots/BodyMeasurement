package org.frank.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransformationBuilder {

    public interface Transformer<S, T> {
        abstract class SelfTransformer<S, T> implements Transformer<S, T> {
            public T from() {
                return from((S)this);
            }

            public S to() {
                return to((T)this);
            }
        }

        T from(S entity);

        S to(T entity);
    }

    public static abstract class ListTransformer<S, T> implements Transformer<S, T> {
        public abstract T transform(S entity);

        public T from(S entity) {
            return transform(entity);
        }

        @Override
        public S to(T entity) {
            throw new IllegalArgumentException("Only one side (to) transformation supported.");
        }
    }

    public interface SetupTransformation<S, T> {
        ListTransformation<S, T> transformer(ListTransformer<S, T> transformer);
    }

    public interface ListTransformation<S, T> {
        List<T> apply();

        SetupTransformation<S, T> list(Collection<S> list);
    }

    public static <S, T> SetupTransformation<S, T> list(Collection<S> list) {
        return new Transformation<S, T>().list(list);
    }

    @Getter
    @Setter
    @Accessors(fluent = true)
    public static class Transformation<S, T> implements ListTransformation<S, T>, SetupTransformation<S, T> {
        private ListTransformer<S, T> transformer;
        private Collection<S> list;

        protected <C extends Collection<T>> C apply(C collection) {
            if (list == null){
                return collection;
            }
            for (S value : list) {
                T entity = transform(value);
                if (entity != null) {
                    collection.add(entity);
                }
            }
            return collection;
        }

        protected int size() {
            return (list == null)? 0 : list.size();
        }

        @Override
        public List<T> apply() {
            return apply(new ArrayList<T>(size()));
        }

        protected T transform(S value) {
            if (value == null) {
                return null;
            }
            return transformer.transform(value);
        }
    }
}

