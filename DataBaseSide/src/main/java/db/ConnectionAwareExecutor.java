package db;

import exceptions.GenericDAOException;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class ConnectionAwareExecutor {
    protected static final Logger LOG = LoggerFactory.getLogger(ConnectionAwareExecutor.class);
    private static DataSource dataSource = null;
    private static final String DB_URL = "jdbc:mysql://";

    private static Properties getEnvironmentProperties() throws IOException {
        Properties properties = new Properties();
        properties.loadFromXML(ConnectionAwareExecutor.class.getClassLoader().getResourceAsStream("environment.xml"));
        return properties;
    }
    public Connection connect() throws GenericDAOException {
        try {
            if (dataSource != null)
                return dataSource.getConnection();
            else {
                try {
                    dataSource = new DataSource();
                    Properties properties = ConnectionAwareExecutor.getEnvironmentProperties();
                    String host = properties.getProperty("host");
                    int port = Integer.parseInt(properties.getProperty("port"));
                    String user = properties.getProperty("user");
                    String password = properties.getProperty("password");
                    String dbName = properties.getProperty("db_name");
                    String sqlUrl = DB_URL + host + ":" + port + "/" + dbName;
                    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
                    dataSource.setUrl(sqlUrl);
                    dataSource.setUsername(user);
                    dataSource.setPassword(password);
                    dataSource.setInitialSize(5);
                    dataSource.setMaxActive(10);
                    dataSource.setMaxWait(10000);
                    dataSource.setValidationInterval(30000);
                    dataSource.setTimeBetweenEvictionRunsMillis(30000);
                    dataSource.setRemoveAbandonedTimeout(60);
                    dataSource.setMinEvictableIdleTimeMillis(30000);
                    dataSource.setMaxIdle(5);
                    dataSource.setMinIdle(2);
                } catch (IOException e) {
                    LOG.error("error while creating pool connections to DB");
                }
                return dataSource.getConnection();
            }
        } catch (SQLException e) {
            LOG.error("error while connecting to DB");
            throw new GenericDAOException(e);
        }
    }


    public void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public void rollbackConnection(Connection connection) {
        try {
            if (connection != null)
                connection.rollback();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }
}
