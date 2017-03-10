package db;

import exceptions.GenericDAOException;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import static db.ManagerDB.DB_URL;


public class ConnectionAwareExecutor {

    //protected static final Logger LOG = LoggerFactory.getLogger(ConnectionAwareExecutor.class);

    private Connection connect() throws GenericDAOException {
        try {
            Properties properties = ManagerDB.getEnvironmentProperties();
            String host = properties.getProperty("host");
            int port = Integer.parseInt(properties.getProperty("port"));
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String dbName = properties.getProperty("db_name");
            String useSSL = properties.getProperty("useSSL");
            String sqlUrl = DB_URL + host + ":" + port + "/" + dbName + "?useSSL=" + useSSL;

            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            Connection connection = DriverManager.getConnection(sqlUrl, user, password);

            DatabaseMetaData metaData = connection.getMetaData();
            //LOG.info("dao Connected to " + metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion());
            return connection;
        } catch (SQLException | IOException e) {
            throw new GenericDAOException(e);
        }
    }


    public <T> T submit(ConnectionTask<T> task) throws GenericDAOException {
        try(Connection connection = connect();
            Statement statement = connection.createStatement()){
            return task.execute(statement);
        } catch (SQLException e) {
            throw new GenericDAOException(e);
        } finally {
            //LOG.info("dao Disconnected");
        }
    }
}
