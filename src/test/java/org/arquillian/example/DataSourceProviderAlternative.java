package org.arquillian.example;

import org.apache.camel.cdi.ContextName;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

@Alternative
@ContextName("dummy-context")
public class DataSourceProviderAlternative implements DataSourceProvider{

    @Produces
    @Named("dummy-datasource")
    @Override
    public DataSource getDataSource() throws Exception {
        InitialContext initContext = new InitialContext();
        String dsString = "org.h2.Driver::::jdbc:jdbc:h2:mem:testdb::::sa";
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        DataSource ds = (DataSource) envContext.lookup("datasource/ds");
        return ds;
    }
}
