package net.euphalys.euphabuild.data;

import net.euphalys.api.plugin.IEuphalysPlugin;
import net.euphalys.core.api.utils.Pair;
import net.euphalys.euphabuild.EuphaBuild;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Dinnerwolph
 */
public class PlotsManager {

    private final DataSource dataSource;
    private final IEuphalysPlugin plugin;

    public PlotsManager(IEuphalysPlugin plugin) {
        this.plugin = plugin;
        this.dataSource = plugin.getDatabaseManager().getDataSource();
    }

    public void registerPlots(Location loc1, Location loc2) {
        EuphaBuild.getInstance().plotMap.put(getNextId(), new Pair<>(loc1, loc2));
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `build_plots`(`loc1`, `loc2`) VALUES (?,?)");
            statement.setString(1, loc1.getWorld().getName() + ";" + loc1.getX() + ";0;" + loc1.getZ());
            statement.setString(2, loc2.getWorld().getName() + ";" + loc2.getX() + ";255;" + loc2.getZ());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getNextId() {
        int returnInt = -1;
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `AUTO_INCREMENT`\n" +
                    "FROM  INFORMATION_SCHEMA.TABLES\n" +
                    "WHERE TABLE_NAME   = 'build_plots';");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            returnInt = resultSet.getInt("AUTO_INCREMENT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnInt;
    }

    public void getAllPlots() {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM build_plots");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String[] loc1 = resultSet.getString("loc1").split(";");
                String[] loc2 = resultSet.getString("loc2").split(";");
                Location location1 = new Location(Bukkit.getWorld("tests2"), Double.parseDouble(loc1[1]), Double.parseDouble(loc1[2]), Double.parseDouble(loc1[3]));
                Location location2 = new Location(Bukkit.getWorld("tests2"), Double.parseDouble(loc2[1]), Double.parseDouble(loc2[2]), Double.parseDouble(loc2[3]));
                EuphaBuild.getInstance().plotMap.put(resultSet.getInt("id"), new Pair<>(location1, location2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
