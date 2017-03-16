package db;

import exceptions.GenericDAOException;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collector;

public class ManagerDB {

    protected static final Logger LOG = Logger.getLogger(ConnectionAwareExecutor.class);
    public static final String DB_URL = "jdbc:mysql://";

    public static Properties getEnvironmentProperties() throws IOException {
        Properties properties = new Properties();
        properties.loadFromXML(ManagerDB.class.getClassLoader().getResourceAsStream("environment.xml"));
        return properties;
    }

    public static void createDB() throws GenericDAOException {
        try {
            Properties properties = getEnvironmentProperties();
            String host = properties.getProperty("host");
            int port = Integer.parseInt(properties.getProperty("port"));
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String useSSL = properties.getProperty("useSSL");
            String sqlUrl = DB_URL + host + ":" + port + "/?useSSL=" + useSSL;
            String dbName = properties.getProperty("db_name");

            DriverManager.registerDriver(new com.mysql.jdbc.Driver());

            LinkedList<String> listScript = new LinkedList<>();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ManagerDB.class.getClassLoader().getResourceAsStream("db/db-create-script.sql"), Charset.forName("UTF-8")))) {
                while (reader.ready())
                    listScript.add(reader.readLine());
            }

            StringBuilder builder = new StringBuilder();
            List<String> scriptList = listScript.stream()
                    .collect(Collector.of(LinkedList<String>::new,
                            (l, s) -> {
                                s = s.replaceAll("%DATABASENAME%", dbName);
                                if (s.contains(";")) {
                                    Arrays.stream(s.split(";")).forEachOrdered(line -> {
                                        builder.append(line).append(";");
                                        l.add(builder.toString().trim());
                                        builder.delete(0, builder.length());
                                    });
                                } else builder.append(s).append(' ');
                            },
                            (l1, l2) -> {
                                l1.addAll(l2);
                                return l1;
                            }));

            try (Connection connection = DriverManager.getConnection(sqlUrl, user, password);
                 Statement statement = connection.createStatement()) {
                for (String query : scriptList) {
                    statement.executeUpdate(query);
                }
                LOG.info("DataBase '" + dbName + "' created");
            }
        } catch (SQLException | IOException e) {
            throw new GenericDAOException(e);
        }

    }

    public static void dropDB() throws GenericDAOException {
        try {
            Properties properties = getEnvironmentProperties();
            String host = properties.getProperty("host");
            int port = Integer.parseInt(properties.getProperty("port"));
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String useSSL = properties.getProperty("useSSL");
            String sqlUrl = DB_URL + host + ":" + port + "/?useSSL=" + useSSL;
            String dbName = properties.getProperty("db_name");

            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            try (Connection connection = DriverManager.getConnection(sqlUrl, user, password);
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("DROP DATABASE " + dbName);
                LOG.info("DataBase '" + dbName + "' destroyed");
            }
        } catch (SQLException | IOException e) {
            throw new GenericDAOException(e);
        }
    }

}
