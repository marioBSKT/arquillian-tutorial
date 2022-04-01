package org.arquillian.example;

import org.apache.camel.cdi.ContextName;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

@ApplicationScoped
@Default
@ContextName("dummy-context")
public class CustomCamelContext extends DefaultCamelContext {

    @Produces
    @Named("some-bean")
    @ApplicationScoped
    public SomeBean getSomeBean() {
        return new SomeBean();
    }

    @Produces
    @Named("properties")
    @ApplicationScoped
    PropertiesComponent propertiesComponent() {
        PropertiesComponent pc = new PropertiesComponent();
        pc.setLocation("classpath:/app.properties");
        return pc;
    }

}
