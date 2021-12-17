package org.arquillian.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
@ContextName("dummy-context")
public class DummyRouteBuilder extends RouteBuilder {

    @Inject
    @Named("some-bean")
    private BindyCsvDataFormat someBean;

    @Override
    public void configure() throws Exception {
        from("file:///home/dario/tmp?fileName=dummy.csv")
                .id("dummy")
                .unmarshal(someBean)
                .log("Ciao ${body}")
                .to("file:///home/dario/tmp/result?fileName=saluti.txt&fileExist=append").id("dummyEnd");
    }
}
