package org.frank.persistence.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.frank.persistence.PersistenceProvider;
import org.frank.utils.CollectionsUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DatabaseStorage<T, ID> extends PersistenceProvider.AbstractStorage<T, ID> {

    private Dao<T, ID> dao;

    public DatabaseStorage(Class<T> clazz) throws SQLException {
        ConnectionSource connectionSource = new JdbcConnectionSource(JDBCUrlResolver.jdbcUrl());
        this.dao = DaoManager.createDao( connectionSource, clazz );
    }

    @Override
    public T save(T entity) throws SQLException {
        dao.create(entity);
        return entity;
    }

    @Override
    public T get(ID id) throws SQLException {
        return dao.queryForId(id);
    }

    @Override
    public List<T> list(Map<String, Object> fields) throws SQLException {
        return (CollectionsUtils.isEmpty(fields)) ? dao.queryForAll() :
                                  dao.queryForFieldValues(fields);
    }

    @Override
    public void close() throws IOException {
        dao.getConnectionSource().close();
    }
}
