package org.frank.persistence;

import org.frank.persistence.database.DatabaseStorage;
import org.frank.utils.TransformationBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PersistenceProvider {

    public interface StorageFilter<Filter> extends TransformationBuilder.SimpleTransformer<Filter,Map<String,Object>> {
        Map<String, Object> transform();
    }

    public static class DummyStorageFilter implements StorageFilter {
        @Override public Map<String, Object> transform() { return null; }
        @Override public Object transform(Object entity) { return null; }
    }

    public interface Storage<T, ID> {
        T save(T entity) throws SQLException;

        T get(ID id) throws SQLException;

        <S> S getAs(ID id, TransformationBuilder.SimpleTransformer<T, S> transformer) throws SQLException;

        List<T> list(Map<String, Object> filterFields) throws SQLException;

        List<T> list() throws SQLException;

        <S> List<S> listAs(StorageFilter<?> filter, TransformationBuilder.SimpleTransformer<T, S> entityTransformer) throws SQLException;

        <S> List<S> listAs(TransformationBuilder.SimpleTransformer<T, S> transformer) throws SQLException;

        void close() throws IOException;
    }

    public static abstract class AbstractStorage<T, ID> implements Storage<T, ID> {

        @Override
        public <S> S getAs(ID id, TransformationBuilder.SimpleTransformer<T, S> transformer) throws SQLException {
            return transformer.transform(get(id));
        }

        @Override
        public List<T> list() throws SQLException {
            return list(Collections.emptyMap());
        }

        @Override
        public <S> List<S> listAs(TransformationBuilder.SimpleTransformer<T, S> transformer) throws SQLException {
            return listAs(new DummyStorageFilter(), transformer);
        }

        @Override
        public <S> List<S> listAs(StorageFilter<?> filter, TransformationBuilder.SimpleTransformer<T, S> transformer) throws SQLException {
            return TransformationBuilder.<T, S>list(list(filter.transform())).transformer(transformer).apply();
        }
    }

    public static <T, ID> Storage<T, ID> storage(Class<T> clazz) throws SQLException {
        return new DatabaseStorage<>(clazz);
    }
}