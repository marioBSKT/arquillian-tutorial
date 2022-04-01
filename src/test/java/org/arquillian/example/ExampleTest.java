package org.arquillian.example;

import javax.inject.Inject;
import javax.naming.InitialContext;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.component.direct.DirectEndpoint;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.arquillian.example.inception.InceptedClass;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.apache.camel.test.junit4.CamelTestSupport;

@RunWith(Arquillian.class)
public class ExampleTest extends CamelTestSupport {

    @Deployment
    public static JavaArchive createDeployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
                .addClass(Greeter.class)
                .addClass(DummyRouteBuilder.class)
                .addClass(SomeBean.class)
                .addClass(InceptedClass.class)
                .addClass(CustomCamelContext.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(jar.toString(true));
        return jar;
    }

    @Inject
    Greeter greeter;

    @Inject
    @ContextName("dummy-context")
    DummyRouteBuilder dummyRouteBuilder;

    @EndpointInject(uri = "direct:trigger")
    DirectEndpoint directTrigger;

    @EndpointInject(uri = "mock:stock")
    MockEndpoint mockStock;

    @Inject
    @ContextName("dummy-context")
    CustomCamelContext customCamelContext;

    private InitialContext initialContext;

    @Before
    public void setup() throws Exception {
        dummyRouteBuilder.addRoutesToCamelContext(context);
        context.getRouteDefinition("dummy-route").adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith(directTrigger);
                weaveById("dummyEnd").replace().to(mockStock);
            }
        });
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        this.initialContext = new InitialContext();
        JndiRegistry jndiRegistry = super.createRegistry();
        if (jndiRegistry.lookupByName("properties") == null) {
            jndiRegistry.bind("properties", customCamelContext.propertiesComponent());
        }
        return jndiRegistry;
    }

    @Test
    public void shouldCountOneMessage() throws Exception {
        template.sendBody(directTrigger, "TEST");
        mockStock.expectedMessageCount(1);
        mockStock.assertIsSatisfied();
    }

}