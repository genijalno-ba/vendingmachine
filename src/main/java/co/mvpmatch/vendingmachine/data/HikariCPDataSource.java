package co.mvpmatch.vendingmachine.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jvnet.hk2.annotations.Service;

import java.sql.Connection;
import java.sql.SQLException;

@Service
public class HikariCPDataSource implements IDataSource {

  private final HikariDataSource ds;

  public HikariCPDataSource() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:postgresql://localhost:5432/vendingmachine");
    config.setUsername("postgres");
    config.setPassword("postgres");
    config.setDriverClassName("org.postgresql.Driver");
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    ds = new HikariDataSource(config);
  }

  public Connection getConnection() throws SQLException {
    return ds.getConnection();
  }
}
