package org.sugarcubes.sql;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.sql.DataSource;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Maxim Butov
 */
public class DataSourceDriverTest {

    @Test
    public void test() throws Exception {

        Connection connection = Mockito.mock(Connection.class);
        DataSource dataSource = Mockito.mock(DataSource.class);
        Mockito.doReturn(connection).when(dataSource).getConnection();
        String jdbcUrl = DataSourceDriver.register(dataSource);
        Assert.assertThat(DriverManager.getConnection(jdbcUrl), Matchers.sameInstance(connection));
        DataSourceDriver.deregister(jdbcUrl);

    }

}
