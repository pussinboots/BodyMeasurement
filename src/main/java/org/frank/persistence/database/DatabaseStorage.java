package org.frank.persistence.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
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
    public void healthCheck() throws SQLException {
        dao.executeRawNoArgs("Select 1;");
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
    public List<T> list(Map<String, Object> fields, PersistenceProvider.Page page) throws SQLException {
        QueryBuilder<T, ID> queryBuilder = dao.queryBuilder().offset(page.offset()).limit(page.limit());
        return queryForFieldValues(queryBuilder, fields);
    }

    @Override
    public void close() throws IOException {
        dao.getConnectionSource().close();
    }



    protected static <T, ID> List<T> queryForFieldValues(QueryBuilder<T, ID> queryBuilder, Map<String, Object> fieldValues) throws SQLException {
        if(CollectionsUtils.isEmpty(fieldValues)) {
            return queryBuilder.query();
        }

        Where<T, ID> where = queryBuilder.where();
        for(Map.Entry<String, Object> entry : fieldValues.entrySet()) {
            where.eq(entry.getKey(), entry.getValue());
        }
        where.and(fieldValues.size());
        return queryBuilder.query();
    }
}
