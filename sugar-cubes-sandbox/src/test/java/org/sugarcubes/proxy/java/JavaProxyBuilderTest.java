package org.sugarcubes.proxy.java;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Maxim Butov
 */
public class JavaProxyBuilderTest {

    @Test
    public void testProxyBuilder() throws Throwable {
        Connection connection = new JavaProxyBuilder<Connection>()
            .interfaces(Connection.class)
            .proxyToString()
            .newProxy();
        DataSource dataSource = new JavaProxyBuilder<DataSource>()
            .interfaces(DataSource.class)
            .method("getConnection", (proxy, target) -> connection)
            .method("getConnection", String.class, String.class, (proxy, target, arg0, arg1) -> connection)
            .proxyToString()
            .newProxy();
        Assert.assertSame(connection, dataSource.getConnection());
        System.out.println(connection);
        System.out.println(dataSource);
    }

}
