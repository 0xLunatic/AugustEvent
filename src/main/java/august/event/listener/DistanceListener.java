package august.event.listener;

import august.event.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.Random;

public class DistanceListener extends BukkitRunnable implements Listener {
    private final Main plugin;

    public DistanceListener(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()){
            if (p.getItemInHand().hasItemMeta()){
                if (Objects.requireNonNull(p.getItemInHand().getItemMeta()).hasDisplayName()){
                    if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§cIron Relic") ||
                            p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§cWooden Relic")){
                        if (p.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world"))) {
                            if (plugin.data.getConfig("storage.yml").getString(p.getName() + ".data") != null) {
                                Location loc = deserialize(Objects.requireNonNull(plugin.data.getConfig("storage.yml").getString(p.getName() + ".data")));
                                double distance = p.getLocation().distance(loc);
                                String msg = String.format("%.1f", distance);
                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§b" + msg + " Blocks"));
                                if (distance > 60) {
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 100, (float) 2);
                                }
                                else if (distance > 50) {
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 100, (float) 1.8);
                                }
                                else if (distance > 20) {
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 100, (float) 1.5);
                                }
                                else if (distance > 5) {
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 100, (float) 1);
                                }
                                else if (distance < 5) {
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 100, (float) 0.5);
                                    if (distance < 3) {
                                        if (loc.getBlock().getType() == Material.AIR) {
                                            if (loc.getBlock().hasMetadata(p.getName())) {
                                                p.playSound(loc, Sound.ENTITY_CHICKEN_EGG, 100, 0);
                                                loc.getBlock().setType(Material.CHEST);
                                                loc.getBlock().setMetadata(p.getName(), new FixedMetadataValue(plugin, true));
                                            }
                                        }
                                    }
                                }
                            }else{
                                randomLoc(p);
                            }
                        }
                    }
                }
            }
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
            loc.getBlock().setType(Material.CHEST);
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
