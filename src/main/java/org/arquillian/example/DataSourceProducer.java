package org.arquillian.example;

import org.apache.camel.cdi.ContextName;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@ApplicationScoped
@ContextName("dummy-context")
public class DataSourceProducer {

    @Produces
    @Named("OracleDS")
    public javax.sql.DataSource getDataSource() throws NamingException {
        InitialContext it = new InitialContext();
        DataSource ds = (DataSource) it.lookup("java:jboss/OracleDS");
        return ds;
    }
}