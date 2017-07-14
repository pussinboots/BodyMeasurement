package org.frank.persistence;

import org.frank.persistence.database.DatabaseStorage;
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

        <S> S getAs(ID id, TransformationBuilder.Transformer<T, S> transformer) throws SQLException;

        List<T> list(Map<String, Object> fields) throws SQLException;

        List<T> list() throws SQLException;

        <S> List<S> listAs(Map<String, Object> fields, TransformationBuilder.ListTransformer<T, S> transformer) throws SQLException;

        <S> List<S> listAs(TransformationBuilder.ListTransformer<T, S> transformer) throws SQLException;

        void close() throws IOException;
    }

    public static abstract class AbstractStorage<T, ID> implements Storage<T, ID> {

        @Override
        public <S> S getAs(ID id, TransformationBuilder.Transformer<T, S> transformer) throws SQLException {
            return transformer.from(get(id));
        }

        @Override
        public List<T> list() throws SQLException {
            return list(Collections.<String, Object>emptyMap());
        }

        @Override
        public <S> List<S> listAs(TransformationBuilder.ListTransformer<T, S> transformer) throws SQLException {
            return listAs(null, transformer);
        }

        @Override
        public <S> List<S> listAs(Map<String, Object> fields, TransformationBuilder.ListTransformer<T, S> transformer) throws SQLException {
            return TransformationBuilder.<T, S>list(list(fields)).transformer(transformer).apply();
        }
    }

    public static <T, ID> Storage<T, ID> storage(Class<T> clazz) throws SQLException {
        /*return new Storage<T>() {
            @Override
            public void save(T entity) {
                System.out.println("Save entity " + entity.toString());
            }
        };*/
        return new DatabaseStorage<>(clazz);
    }
}