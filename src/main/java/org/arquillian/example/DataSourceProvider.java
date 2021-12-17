package org.arquillian.example;

import javax.sql.DataSource;

public interface DataSourceProvider {

    public DataSource getDataSource() throws Exception;

}
