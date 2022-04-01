package org.arquillian.example.inception;

import org.apache.camel.cdi.ContextName;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ContextName("dummy-context")
public class InceptedClass {
    public String greet() {
        return "Hello ";
    }
}
