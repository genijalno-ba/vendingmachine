package co.mvpmatch.vendingmachine.cdi;

import co.mvpmatch.vendingmachine.data.HikariCPDataSource;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@SuppressWarnings("unused")
public class LiquibaseFeature implements Feature {

  @Inject
  private HikariCPDataSource dataSource;

  @Override
  public boolean configure(FeatureContext featureContext) {
    String pathToChangeLogFile = getChangeLogFile();
    try (Connection connection = dataSource.getConnection()) {
      Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
      Liquibase liquibase = new Liquibase(pathToChangeLogFile, new FileSystemResourceAccessor(), database);
      liquibase.update(new Contexts());
    } catch (SQLException e) {
      throw new RuntimeException(new DatabaseException(e));
    } catch (LiquibaseException e) {
      throw new RuntimeException(e);
    }
    return true;
  }

  private String getChangeLogFile() {
    try {
      Properties prop = loadProperties();
      String changeLogFile = prop.getProperty("changeLogFile");
      File file = new File(changeLogFile);
      if (!file.exists() || !file.isFile()) {
        throw new FileNotFoundException("Could not load changeLogFile: " + changeLogFile);
      }
      return file.getAbsolutePath();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Properties loadProperties() throws IOException {
    try (InputStream input = new FileInputStream("src/main/resources/liquibase.properties")) {
      Properties prop = new Properties();
      prop.load(input);
      return prop;
    }
  }
}
