package org.frank;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.frank.persistence.PersistenceProvider;
import org.frank.persistence.database.JDBCUrlResolver;
import org.frank.utils.TransformationBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Accessors(fluent = true)
public class TestDatabase<T> {

    @Setter
    private Class<T> databaseEntityClass;
    @Getter
    private List<T> inserted = new ArrayList<>();
    private PersistenceProvider.Storage<T, Long> storage;

    public TestDatabase<T> startDatabase() throws SQLException {
        ConnectionSource connectionSource = new JdbcConnectionSource(JDBCUrlResolver.jdbcUrl());
        DaoManager.createDao( connectionSource, databaseEntityClass ).executeRaw("DROP SCHEMA PUBLIC CASCADE");

        TableUtils.createTableIfNotExists(connectionSource, databaseEntityClass);
        connectionSource.closeQuietly();
        return this;
    }

    public TestDatabase<T> connect() throws SQLException, IOException {

        storage = PersistenceProvider.storage(databaseEntityClass);
        assertEquals("Ensure an empty test database.", 0, storage.list().size());
        return this;
    }

    public TestDatabase<T> close() throws IOException {
        storage.close();
        return this;
    }

    public TestDatabase<T> insert(T ...entities) throws SQLException {
        for(T entity : entities) {
            storage.save(entity);
            inserted.add(entity);
        }
        return this;
    }

    public <E> List<E> insertedAs(TransformationBuilder.SimpleTransformer<T,E> transformer) {
        return TransformationBuilder.<T, E>list(inserted()).transformer(transformer).apply();
    }

    public static <T> TestDatabase<T> db(Class<T> databaseEntityClass) {
        return new TestDatabase<T>().databaseEntityClass(databaseEntityClass);
    }


}
