package org.arquillian.example;

import org.apache.camel.cdi.ContextName;
import org.arquillian.example.inception.InceptedClass;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@ContextName("dummy-context")
public class SomeBean {

    @Inject
    @ContextName("dummy-context")
    InceptedClass inceptedClass;

    public String greetingMethod(String input) {
        return inceptedClass.greet() + input;
    }
}
