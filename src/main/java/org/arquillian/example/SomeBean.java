package org.arquillian.example;

import org.arquillian.example.inception.InceptedClass;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SomeBean {

    @Inject
    InceptedClass inceptedClass;

    public String stupid(String input) {
        return "STUPID " + input + inceptedClass.dumb();
    }
}
