package org.frank.persistence;

import org.frank.persistence.database.DatabaseStorage;
import org.frank.resources.BodyMeasurementResource;
import org.frank.utils.TransformationBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PersistenceProvider {

    public interface Storage<T, ID> {
        T save(T entity) throws SQLException;

        T get(ID id) throws SQLException;

        <S> S getAs(ID id, TransformationBuilder.SimpleTransformer<T, S> transformer) throws SQLException;

        List<T> list(Map<String, Object> filterFields, Page page) throws SQLException;

        List<T> list() throws SQLException;

        <S> List<S> listAs(StorageFilter<?> filter, Page page, TransformationBuilder.SimpleTransformer<T, S> entityTransformer) throws SQLException;

        void close() throws IOException;

        void healthCheck() throws SQLException;
    }

    public interface StorageFilter<Filter> extends TransformationBuilder.SimpleTransformer<Filter,Map<String,Object>> {
        Map<String, Object> transform();
    }

    public interface Page {
        long offset();
        long limit();
    }

    private static class DefaultPage implements Page {
        @Override public long offset() { return 0; }
        @Override public long limit() { return BodyMeasurementResource.DEFAULT_ITEM_LIMIT; }
    }

    public static abstract class AbstractStorage<T, ID> implements Storage<T, ID> {

        @Override
        public <S> S getAs(ID id, TransformationBuilder.SimpleTransformer<T, S> transformer) throws SQLException {
            return transformer.transform(get(id));
        }

        @Override
        public List<T> list() throws SQLException {
            return list(Collections.emptyMap(), new DefaultPage());
        }

        @Override
        public <S> List<S> listAs(StorageFilter<?> filter, Page page, TransformationBuilder.SimpleTransformer<T, S> transformer) throws SQLException {
            return TransformationBuilder.<T, S>list(list(filter.transform(), page)).transformer(transformer).apply();
        }
    }

    public static <T, ID> Storage<T, ID> storage(Class<T> clazz) throws SQLException {
        return new DatabaseStorage<>(clazz);
    }
}