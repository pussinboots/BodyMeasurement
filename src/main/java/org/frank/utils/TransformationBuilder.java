package org.frank.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransformationBuilder<Source, Target> {

    public interface SimpleTransformer<Source, Target> {
        Target transform(Source entity);
    }

    public interface SetupTransformation<Source, Target> {
        ListTransformation<Source, Target> transformer(SimpleTransformer<Source, Target> transformer);
    }

    public interface ListTransformation<Source, Target> {
        List<Target> apply();

        SetupTransformation<Source, Target> list(Collection<Source> list);
    }

    public static <Source, Target> SetupTransformation<Source, Target> list(Collection<Source> list) {
        return new TransformationBuilder<Source, Target>().new Transformation().list(list);
    }

    @Getter
    @Setter
    @Accessors(fluent = true)
    public class Transformation implements ListTransformation<Source, Target>, SetupTransformation<Source, Target> {
        private SimpleTransformer<Source, Target> transformer;
        private Collection<Source> list;

        protected <C extends Collection<Target>> C apply(C collection) {
            if (list == null){
                return collection;
            }
            for (Source value : list) {
                Target entity = transform(value);
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
        public List<Target> apply() {
            return apply(new ArrayList<Target>(size()));
        }

        protected Target transform(Source value) {
            if (value == null) {
                return null;
            }
            return transformer.transform(value);
        }
    }
}

