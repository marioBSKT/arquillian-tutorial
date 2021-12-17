package org.arquillian.example;

import org.apache.camel.cdi.ContextName;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.sql.DataSource;

@ApplicationScoped
@ContextName("dummy-context")
public class DataSourceProvider {

    @Produces
    @Named("dummy-datasource")
    public javax.sql.DataSource getDataSource() throws Exception {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:jboss/ExampleDataSource");
        return ds;
    }

}
