package org.arquillian.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@ContextName("dummy-context")
public class DummyRouteBuilder extends RouteBuilder {

    @Inject
    @ContextName("dummy-context")
    SomeBean someBean;

    @Override
    public void configure() throws Exception {
        from("file:///home/dario/tmp?fileName=dummy.csv")
                .id("dummy-route")
                .toD("{{jdbc.endpoint.uri}}")
                .to("file:///{{home.path}}/tmp/result?fileName=test.txt&fileExist=append").id("dummyEnd");
    }
}
