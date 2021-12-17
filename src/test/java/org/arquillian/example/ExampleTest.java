package org.arquillian.example;

import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

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
import org.junit.Before;
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
                .addClasses(DataSourceProvider.class, DataSourceProviderAlternative.class)
                //.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
                .addAsManifestResource("beans.xml");
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

    private InitialContext initContext;

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        this.initContext = new InitialContext();
        JndiRegistry jndi = super.createRegistry();

        //use jndi.bind to bind your beans
        String dsString = "org.h2.Driver::::jdbc:jdbc:h2:mem:testdb::::sa";
        Context envContext = (Context) this.initContext.lookup("java:/comp/env");
        DataSource ds = (DataSource) envContext.lookup("datasource/ds");
        jndi.bind("dummy-datasource", ds);
        return jndi;
    }

    @Before
    public void setup() throws Exception {

    }

    //@Test
    public void whenMockJndiDataSource_thenReturnJndiDataSource() throws Exception {
        String dsString = "org.h2.Driver::::jdbc:jdbc:h2:mem:testdb::::sa";
        Context envContext = (Context) this.initContext.lookup("java:/comp/env");
        DataSource ds = (DataSource) envContext.lookup("datasource/ds");
        assertEquals(dsString, ds.toString());
    }



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

    //@Test
    /*public void should_bean_be_fancy() throws Exception {
        // because it is declared as the alternative in beans.xml
        assertThat(ds, instanceOf(DataSourceProviderAlternative.class));
    }*/

}