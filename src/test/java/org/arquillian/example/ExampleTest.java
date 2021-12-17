package org.arquillian.example;

import javax.inject.Inject;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.component.direct.DirectEndpoint;
import org.apache.camel.component.mock.MockEndpoint;
import org.arquillian.example.inception.InceptedClass;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.apache.camel.test.junit4.CamelTestSupport;

@RunWith(Arquillian.class)
public class ExampleTest extends CamelTestSupport {

    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
                .addClass(DummyRouteBuilder.class)
                .addClass(SomeBean.class)
                .addClass(InceptedClass.class)
                .addClass(CustomCamelContext.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(jar.toString(true));
        return jar;
    }

    @Inject
    @ContextName("dummy-context")
    DummyRouteBuilder dummyRouteBuilder;

    @EndpointInject(uri = "direct:trigger")
    DirectEndpoint directTrigger;

    @EndpointInject(uri = "mock:stock")
    MockEndpoint mockStock;

    @Test
    public void shouldCountOneMessage() throws Exception {
        dummyRouteBuilder.addRoutesToCamelContext(context);
        context.getRouteDefinition("dummy").adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith(directTrigger);
                weaveById("dummyEnd").replace().to(mockStock);
            }
        });
        template.sendBody(directTrigger, "Daz");
        mockStock.expectedMessageCount(1);
        mockStock.assertIsSatisfied();
    }

}