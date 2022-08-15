package august.event.listener;

import august.event.Main;
import august.event.utils.KemerdekaanPoints;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;

public class JoinListener implements Listener {
    private Main plugin;

    public JoinListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (plugin.data.getConfig("storage.yml").getString(p.getName() + ".data") != null) {
            Location loc = deserialize(Objects.requireNonNull(plugin.data.getConfig("storage.yml").getString(p.getName() + ".data")));
            randomLoc(p);
            if (loc.getBlock().getType() == Material.CHEST){
                loc.getBlock().setType(Material.AIR);
            }
        }
        try {
            KemerdekaanPoints kemerdekaanPoints = this.plugin.getDatabase().getPlayerStatsByName(p.getName());
            if (kemerdekaanPoints == null) {
                kemerdekaanPoints = new KemerdekaanPoints(p.getName(), 0);
                this.plugin.getDatabase().createPlayerStats(kemerdekaanPoints);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void randomLoc(Player p) {
        World world = plugin.getServer().getWorld(Objects.requireNonNull(plugin.getConfig().getString("world")));
        Location center = deserialize(Objects.requireNonNull(plugin.location.getConfig("centerlocation.yml").getString("center")));
        double radius = Double.parseDouble(Objects.requireNonNull(plugin.location.getConfig("centerlocation.yml").getString("radius")));
        Random rand = new Random();
        double angle = rand.nextDouble() * 360; //Generate a random angle
        double x = center.getX() + (rand.nextDouble() * radius * Math.cos(Math.toRadians(angle))); // x
        double z = center.getZ() + (rand.nextDouble() * radius * Math.sin(Math.toRadians(angle))); // z

        Location highestY = new Location(world, x, 1, z);

        assert world != null;
        Location loc = new Location(world, x, world.getHighestBlockYAt(highestY) + 1, z);
        if (loc.getBlock().getType() == Material.AIR) {
            loc.getBlock().setMetadata(p.getName(), new FixedMetadataValue(plugin, true));
            plugin.data.getConfig("storage.yml").set(p.getName() + ".data", serialize(loc));
            plugin.data.saveConfig("storage.yml");
        }
    }

    public String serialize(Location loc) {
        World world = loc.getWorld();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();
        assert world != null;
        return world.getName() + ";" + x + ";" + y + ";" + z + ";" + yaw + ";" + pitch;
    }

    public Location deserialize(String string) {
        String[] vals = string.split(";");
        World world = Bukkit.getWorld(vals[0]);
        double x = Double.parseDouble(vals[1]);
        double y = Double.parseDouble(vals[2]);
        double z = Double.parseDouble(vals[3]);
        float yaw = Float.parseFloat(vals[4]);
        float pitch = Float.parseFloat(vals[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }
}
