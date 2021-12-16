package org.arquillian.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@ContextName("dummy-context")
public class DummyRouteBuilder extends RouteBuilder {

    @Inject
    SomeBean stupidClass;

    @Override
    public void configure() throws Exception {
        from("file:///home/dario/tmp?fileName=dummy.csv")
                .id("dummy")
                .unmarshal("some-bean")
                .log("Ciao " + stupidClass.stupid("BOH") + " ${body}")
                .to("file:///home/dario/tmp/result?fileName=saluti.txt&fileExist=append").id("dummyEnd");
    }
}
