package org.arquillian.example;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.sql.DataSource;

@Alternative
public class DataSourceProviderImpl implements DataSourceProvider{

    @Produces
    @Named("dummy-datasource")
    public javax.sql.DataSource getDataSource() throws Exception {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("datasource/ds");
        return ds;
    }
}
