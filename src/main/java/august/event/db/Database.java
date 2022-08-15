package august.event.db;

import august.event.Main;
import august.event.utils.KemerdekaanPoints;

import java.sql.*;

public class Database {
    private final Main plugin;

    private Connection connection;

    public Database(Main plugin) {
        this.plugin = plugin;
    }

    public Connection getConnection() throws SQLException {

        if (connection != null) {
            return connection;
        }
        String database = plugin.getConfig().getString("database.database");
        String host = plugin.getConfig().getString("database.host");
        String port = plugin.getConfig().getString("database.port");

        String username = plugin.getConfig().getString("database.user");
        String password = plugin.getConfig().getString("database.password");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        this.connection = DriverManager.getConnection(url, username, password);

        return this.connection;
    }

    public void initializeDatabase() throws SQLException {
        Statement statement = getConnection().createStatement();

        String sql1 = "CREATE TABLE IF NOT EXISTS kemerdekaan_stats(player_name varchar(36) primary key, kemerdekaan_points double)";
        statement.execute(sql1);

        statement.close();

    }
    public KemerdekaanPoints getPlayerStatsByName(String name) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM kemerdekaan_stats WHERE player_name = ?");
        statement.setString(1, name);

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            double kemerdekaan_points = results.getDouble("kemerdekaan_points");

            KemerdekaanPoints kemerdekaanPoints = new KemerdekaanPoints(name, kemerdekaan_points);

            statement.close();
            return kemerdekaanPoints;
        }
        statement.close();
        return null;
    }
    public void createPlayerStats(KemerdekaanPoints kemerdekaanPoints) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("INSERT INTO kemerdekaan_stats(player_name, kemerdekaan_points) VALUES (?, ?)");

        statement.setString(1, kemerdekaanPoints.getPlayerName());
        statement.setDouble(2, kemerdekaanPoints.getKemerdekaanPoints());

        statement.executeUpdate();
        statement.close();
    }
    public void clearDatabase() throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("DROP TABLE kemerdekaan_stats");
        statement.executeUpdate();
        statement.close();
    }
    public void updatePlayerStats(KemerdekaanPoints kemerdekaanPoints) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE kemerdekaan_stats SET kemerdekaan_points = ? WHERE player_name = ?");

        statement.setDouble(1, kemerdekaanPoints.getKemerdekaanPoints());
        statement.setString(2, kemerdekaanPoints.getPlayerName());

        statement.executeUpdate();
        statement.close();
    }
    public void addKemerdekaanPoints(KemerdekaanPoints kemerdekaanPoints, double value) throws SQLException{
        PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE kemerdekaan_stats SET kemerdekaan_points = ? WHERE player_name = ?");

        statement.setDouble(1, kemerdekaanPoints.getKemerdekaanPoints() + value);
        statement.setString(2, kemerdekaanPoints.getPlayerName());

        statement.executeUpdate();
        statement.close();
    }
}
