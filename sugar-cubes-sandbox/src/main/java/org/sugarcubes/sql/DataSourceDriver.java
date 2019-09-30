package org.sugarcubes.sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.sugarcubes.rex.Rex;

/**
 * A proxy JDBC {@link Driver} which takes {@link Connection} from {@link DataSource}.
 *
 * <p>Usage:</p>
 *
 * <pre>
 *     DataSource dataSource = ...
 *     DataSourceDriver driver = new DataSourceDriver(dataSource);
 *     String jdbcUrl = driver.registerDriver();
 *     try {
 *         ...
 *         try (Connection connection = DriverManager.getConnection(jdbcUrl)) {
 *             ...
 *         }
 *         ...
 *     } finally {
 *         driver.deregisterDriver();
 *     }
 * </pre>
 *
 * Useful in cases when some code accepts only JDBC URL and does not support data sources.
 *
 * @author Maxim Butov
 */
public class DataSourceDriver implements Driver {

    /**
     * Registered instances of {@link DataSourceDriver}.
     */
    private static final Map<String, DataSourceDriver> REGISTERED_DRIVERS = new HashMap<>();

    /**
     * Data source.
     */
    private final DataSource dataSource;

    /**
     * JDBC URL.
     */
    private final String url;

    /**
     * Value for {@link Driver#jdbcCompliant()}.
     */
    private final boolean jdbcCompliant;

    /**
     * Constructor.
     *
     * @param dataSource data source
     */
    public DataSourceDriver(DataSource dataSource) {
        this(dataSource, true);
    }

    /**
     * Constructor.
     *
     * @param dataSource data source
     * @param jdbcCompliant value for {@link #jdbcCompliant()}
     */
    public DataSourceDriver(DataSource dataSource, boolean jdbcCompliant) {
        this(dataSource, jdbcCompliant, null);
    }

    /**
     * Constructor.
     *
     * @param dataSource data source
     * @param url JDBC URL
     */
    public DataSourceDriver(DataSource dataSource, String url) {
        this(dataSource, true, url);
    }

    /**
     * Constructor.
     *
     * @param dataSource data source
     * @param jdbcCompliant value for {@link #jdbcCompliant()}
     * @param url JDBC URL
     */
    public DataSourceDriver(DataSource dataSource, boolean jdbcCompliant, String url) {
        this.dataSource = Objects.requireNonNull(dataSource, "dataSource must be not null");
        this.jdbcCompliant = jdbcCompliant;
        if (url != null) {
            if (REGISTERED_DRIVERS.containsKey(url)) {
                throw new IllegalArgumentException(String.format("JDBC URL '%s' already registered.", url));
            }
            this.url = url;
        }
        else {
            this.url = String.format("jdbc:datasource:%s", UUID.randomUUID());
        }
    }

    /**
     * Registers this driver in {@link DriverManager}. When registered driver will not be disposed by GC,
     * a developer should {@link #deregisterDriver()}.
     *
     * @return JDBC URL
     */
    public String registerDriver() {
        try {
            DriverManager.registerDriver(this, () -> REGISTERED_DRIVERS.remove(url));
        }
        catch (SQLException e) {
            throw Rex.rethrowAsRuntime(e);
        }
        REGISTERED_DRIVERS.put(url, this);
        return url;
    }

    /**
     * Deregisters this driver in {@link DriverManager}.
     */
    public void deregisterDriver() {
        try {
            DriverManager.deregisterDriver(this);
        }
        catch (SQLException e) {
            throw Rex.rethrowAsRuntime(e);
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return acceptsURL(url) ? dataSource.getConnection() : null;
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return this.url.equals(url);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return jdbcCompliant;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(DataSourceDriver.class.getPackage().getName());
    }

}
