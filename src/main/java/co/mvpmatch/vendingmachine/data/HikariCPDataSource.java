package co.mvpmatch.vendingmachine.data;

import co.mvpmatch.vendingmachine.cdi.LiquibaseFeature;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jvnet.hk2.annotations.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@Service
public class HikariCPDataSource implements IDataSource {

  private final HikariDataSource ds;

  public HikariCPDataSource() throws IOException {
    Properties prop = LiquibaseFeature.loadProperties();
    String jdbcUrl = prop.getProperty("url");
    String username = prop.getProperty("username");
    String password = prop.getProperty("password");
    String driverClassName = prop.getProperty("driver");
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(jdbcUrl);
    config.setUsername(username);
    config.setPassword(password);
    config.setDriverClassName(driverClassName);
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    ds = new HikariDataSource(config);
  }

  public Connection getConnection() throws SQLException {
    return ds.getConnection();
  }
}
