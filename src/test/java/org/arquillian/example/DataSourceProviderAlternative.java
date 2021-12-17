package org.arquillian.example;

import javax.enterprise.inject.Alternative;
import javax.sql.DataSource;

@Alternative
public class DataSourceProviderAlternative implements DataSourceProvider{
    @Override
    public DataSource getDataSource() throws Exception {
        return null;
    }
}
