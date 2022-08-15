package august.event.listener;

import august.event.Main;
import august.event.utils.KemerdekaanPoints;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;

public class InteractListener implements Listener {
    private final Main plugin;


    ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    public HashMap<String, Long> cooldowns = new HashMap<>();
    public HashMap<Player, Integer> mark = new HashMap<>();

    public InteractListener(Main plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onRightClickChest(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getItemInHand().hasItemMeta()){
            if (Objects.requireNonNull(p.getItemInHand().getItemMeta()).hasDisplayName()){
                if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§cWooden Relic")) {
                    e.setCancelled(true);
                    if (e.getClickedBlock() != null) {
                        if (p.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world"))) {
                            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                if (Objects.requireNonNull(e.getClickedBlock()).hasMetadata(p.getName())) {
                                    e.setCancelled(true);
                                    Location loc = deserialize(Objects.requireNonNull(plugin.data.getConfig("storage.yml").getString(p.getName() + ".data")));
                                    Location cLoc = e.getClickedBlock().getLocation();
                                    Block block = e.getClickedBlock();
                                    block.removeMetadata(p.getName(), plugin);
                                    chestOpenAnimation(p, cLoc, block);
                                    if (mark.get(p) != null){
                                        PacketPlayOutEntityDestroy remove = new PacketPlayOutEntityDestroy(mark.get(p));
                                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(remove);
                                        mark.remove(p);
                                    }
                                }
                            }
                        }
                    }
                }
                if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§cIron Relic")) {
                    e.setCancelled(true);
                    if (e.getClickedBlock() != null) {
                        if (p.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("world"))) {
                            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                if (Objects.requireNonNull(e.getClickedBlock()).hasMetadata(p.getName())) {
                                    e.setCancelled(true);
                                    Location loc = deserialize(Objects.requireNonNull(plugin.data.getConfig("storage.yml").getString(p.getName() + ".data")));
                                    Location cLoc = e.getClickedBlock().getLocation();
                                    Block block = e.getClickedBlock();
                                    block.removeMetadata(p.getName(), plugin);
                                    chestOpenAnimation(p, cLoc, block);
                                    if (mark.get(p) != null){
                                        PacketPlayOutEntityDestroy remove = new PacketPlayOutEntityDestroy(mark.get(p));
                                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(remove);
                                        mark.remove(p);
                                    }
                                }
                            }
                        }
                    } else {
                        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                            int cooldownTime = 180;
                            if (cooldowns.containsKey(p.getName())) {
                                long secondsLeft = ((cooldowns.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
                                if (secondsLeft > 0) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can use that in " + secondsLeft + "s again!"));
                                } else {
                                    cooldowns.remove(p.getName());
                                }
                            } else {
                                cooldowns.put(p.getName(), System.currentTimeMillis());

                                Location loc = deserialize(Objects.requireNonNull(plugin.data.getConfig("storage.yml").getString(p.getName() + ".data")));
                                createMarker(loc, p);
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
        }
    }
    public void createMarker(Location loc, Player p){
        WorldServer s = ((CraftWorld) Objects.requireNonNull(loc.getWorld())).getHandle();
        EntityShulker marker = new EntityShulker(EntityTypes.SHULKER, s);
        marker.setLocation(Double.parseDouble(String.format("%.0f", loc.getX())) + 0.5, loc.getY(), Double.parseDouble(String.format("%.0f", loc.getZ())) - 0.5, 0f, 0f);
        marker.setInvisible(true);
        marker.setFlag(5, true);
        marker.setFlag(6, true);
        p.playSound(p.getLocation(), Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 20, 0F);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aChest found at "
                + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " "));

        PacketPlayOutSpawnEntityLiving spawn1 = new PacketPlayOutSpawnEntityLiving(marker);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawn1);

        mark.putIfAbsent(p, marker.getId());

        PacketPlayOutEntityMetadata metadata1 = new PacketPlayOutEntityMetadata(marker.getId(), marker.getDataWatcher(), true);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(metadata1);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            PacketPlayOutEntityDestroy remove = new PacketPlayOutEntityDestroy(marker.getId());
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(remove);
            mark.remove(p);
        }, 200);
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
    public void chestOpenAnimation(Player p, Location loc, Block block){

        WorldServer s = ((CraftWorld) Objects.requireNonNull(loc.getWorld())).getHandle();
        EntityArmorStand animation = new EntityArmorStand(s, loc.getX() + 0.5, loc.getY() - 1, loc.getZ() + 0.5);
        animation.setNoGravity(true);
        animation.setInvisible(true);
        animation.setMarker(true);
        animation.getBukkitEntity().setCustomName(p.getName());

        BlockPosition position = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
        TileEntityChest tileChest = (TileEntityChest) s.getTileEntity(position);
        s.playBlockAction(position, s.getType(position).getBlock(), 1, 1);

        PacketPlayOutSpawnEntityLiving spawn1 = new PacketPlayOutSpawnEntityLiving(animation);

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawn1);

        PacketPlayOutEntityMetadata metadata1 = new PacketPlayOutEntityMetadata(animation.getId(), animation.getDataWatcher(), true);

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(metadata1);

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packet.getIntegers().write(0, animation.getId());
        List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>();
        list.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, Main.getHead("open")));
        packet.getSlotStackPairLists().write(0, list);

        new BukkitRunnable() {
            double pitch = 0;
            private boolean goingUp = true;
            private final double maximumHeight = animation.getBukkitEntity().getLocation().getY() + 1.5;
            private final double minimumHeight = animation.getBukkitEntity().getLocation().getY() - 0.4000;

            public void run() {
                pitch += 0.1;
                if (!animation.getBukkitEntity().isDead()) {
                    if (animation.getBukkitEntity().getLocation().getY() < maximumHeight) {
                        animation.setLocation(animation.getBukkitEntity().getLocation().getX(), animation.getBukkitEntity().getLocation().getY() + 0.05, animation.getBukkitEntity().getLocation().getZ(), animation.getBukkitEntity().getLocation().getYaw() + 15, animation.getBukkitEntity().getLocation().getPitch() + 15);
                        PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport(animation);
                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(tp);
                        loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_COW_BELL, 30, (float) pitch);
                    }
                    if (animation.getBukkitEntity().getLocation().getY() > maximumHeight) {
                        PacketPlayOutEntityDestroy remove = new PacketPlayOutEntityDestroy(animation.getId());
                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(remove);
                        animation.getBukkitEntity().remove();
                        block.setType(Material.AIR);
                        p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 30F, 0F);
                        randomLoc(p);
                        alwaysReward(p, loc, "kemerdekaan_points");
                        Firework f = loc.getWorld().spawn(loc.add(0.5, 1, 0.5), Firework.class);
                        FireworkMeta fm = f.getFireworkMeta();
                        fm.addEffect(FireworkEffect.builder()
                                .flicker(false)
                                .trail(false)
                                .withColor(Color.fromRGB(255, 0, 0))
                                .withColor(Color.fromRGB(255, 255, 255))
                                .withFade(Color.fromRGB(255, 255, 255))
                                .build());
                        fm.setPower(0);
                        f.setFireworkMeta(fm);
                        f.detonate();
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 1, 1);


        try {
            protocolManager.sendServerPacket(p, packet);
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }
    public void alwaysReward(Player p, Location loc, String type){
        if (type.equalsIgnoreCase("kemerdekaan_points")){
            Random rand = new Random();
            double maxNumber = 10;

            double random = rand.nextInt((int) maxNumber) + 1;
            rewardDisplay(p, loc, "kemerdekaan_points", "&cx" + String.format("%.0f", random) + " &fKemerdekaan Points", random, "kemerdekaan_points");
            rewardDisplay(p, loc, "rotten_box", "&cRotten Box", 1, "rotten_box");
            if (percentChance(0.50)){
                double maxPeso = 50;
                double peso = rand.nextInt((int) maxPeso) + 1;
                rewardDisplay(p, loc, "random_peso", "&cx" + String.format("%.0f", peso) + " &fPeso", peso, "random_peso");
            }
            else if (percentChance(0.001)){
                rewardDisplay(p, loc, "steel_ingot", "&fSteel Ingot", 1, "steel_ingot");
            }
            else if (percentChance(0.001)){
                rewardDisplay(p, loc, "patriot_badge", "&2Patriot Badge", 1, "patriot_badge");
            }
            else if (percentChance(0.004)){
                rewardDisplay(p, loc, "m1_garand_part", "&eM1 Garand Part", 1, "m1_garand_part");
            }
            else if (percentChance(0.002)){
                rewardDisplay(p, loc, "proclamation_paper", "&eProclamation Paper", 1, "proclamation_paper");
            }
            else if (percentChance(0.001)){
                rewardDisplay(p, loc, "sickle_handle", "&fSickle Handle", 1, "sickle_handle");
            }
        }

    }
    public void rewardDisplay(Player p, Location loc, String name, String displayName, double amount, String skullName){
        double min = -1.5;  // Set To Your Desired Min Value
        double max = 1.5; // Set To Your Desired Max Value
        double x = (Math.random() * ((max - min) + 1)) + min;
        double z = (Math.random() * ((max - min) + 1)) + min;


        WorldServer s = ((CraftWorld) Objects.requireNonNull(loc.getWorld())).getHandle();
        EntityArmorStand reward = new EntityArmorStand(s, loc.getX() + x, loc.getY() - 0.5, loc.getZ() + z);
        EntityArmorStand rewardHologram = new EntityArmorStand(s, reward.getBukkitEntity().getLocation().getX(), reward.getBukkitEntity().getLocation().getY() + 0.3, reward.getBukkitEntity().getLocation().getZ());
        reward.setNoGravity(true);
        reward.setInvisible(true);
        reward.getBukkitEntity().setCustomName(p.getName() + " " + name);
        rewardHologram.getBukkitEntity().setCustomName(ChatColor.translateAlternateColorCodes('&', displayName));
        rewardHologram.getBukkitEntity().setCustomNameVisible(true);
        rewardHologram.setInvisible(true);
        rewardHologram.setNoGravity(true);

        PacketPlayOutSpawnEntityLiving spawn1 = new PacketPlayOutSpawnEntityLiving(reward);
        PacketPlayOutSpawnEntityLiving spawn2 = new PacketPlayOutSpawnEntityLiving(rewardHologram);

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawn1);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawn2);

        PacketPlayOutEntityMetadata metadata1 = new PacketPlayOutEntityMetadata(reward.getId(), reward.getDataWatcher(), true);
        PacketPlayOutEntityMetadata metadata2 = new PacketPlayOutEntityMetadata(rewardHologram.getId(), rewardHologram.getDataWatcher(), true);

        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(metadata1);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(metadata2);

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packet.getIntegers().write(0, reward.getId());
        List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>();
        list.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, Main.getHead(skullName)));
        packet.getSlotStackPairLists().write(0, list);

        new BukkitRunnable() {

            private boolean goingUp = true;
            private final double maximumHeight = reward.getBukkitEntity().getLocation().getY() + 0.1000;
            private final double minimumHeight = reward.getBukkitEntity().getLocation().getY() - 0.1000;

            public void run() {
                if (!reward.getBukkitEntity().isDead()) {

                    if (goingUp) {
                        if (reward.getBukkitEntity().getLocation().getY() > maximumHeight) {
                            goingUp = false;
                        } else {
                            reward.setLocation(reward.getBukkitEntity().getLocation().getX(), reward.getBukkitEntity().getLocation().getY() + 0.004, reward.getBukkitEntity().getLocation().getZ(), reward.getBukkitEntity().getLocation().getYaw() + 3, reward.getBukkitEntity().getLocation().getPitch() + 3);
                            PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport(reward);
                            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(tp);
                        }
                    } else {
                        if (reward.getBukkitEntity().getLocation().getY() < minimumHeight) {
                            goingUp = true;
                        } else {
                            reward.setLocation(reward.getBukkitEntity().getLocation().getX(), reward.getBukkitEntity().getLocation().getY() - 0.004, reward.getBukkitEntity().getLocation().getZ(), reward.getBukkitEntity().getLocation().getYaw() + 3, reward.getBukkitEntity().getLocation().getPitch() + 3);
                            PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport(reward);
                            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(tp);
                        }
                    }

                    for (Entity ent : reward.getBukkitEntity().getNearbyEntities(0.8, 0.8, 0.8)) {
                        if (reward.getBukkitEntity().getName().contains(ent.getName())) {
                            PacketPlayOutEntityDestroy stand1 = new PacketPlayOutEntityDestroy(reward.getId());
                            PacketPlayOutEntityDestroy stand2 = new PacketPlayOutEntityDestroy(rewardHologram.getId());
                            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(stand1);
                            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(stand2);
                            p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 20F, 0.4F);

                            reward.getBukkitEntity().remove();
                            rewardHologram.getBukkitEntity().remove();


                            if (name.contains("kemerdekaan_points")) {
                                try {
                                    KemerdekaanPoints kemerdekaanPoints = plugin.getDatabase().getPlayerStatsByName(p.getName());
                                    plugin.getDatabase().addKemerdekaanPoints(kemerdekaanPoints, amount);
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d+" + String.format("%.0f", amount) + " &fKemerdekaan Points"));
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            if (name.contains("random_peso")) {
                                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                                String command = "eco give " + p.getName() + " " + amount;
                                Bukkit.dispatchCommand(console, command);
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d+" + String.format("%.0f", amount) + " &fPeso"));
                            }
                            if (name.contains("rotten_box")) {
                                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                                String command = "MI GIVE MATERIAL ROTTEN_BOX " + p.getName();
                                Bukkit.dispatchCommand(console, command);
                            }
                            if (name.contains("steel_ingot")) {
                                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                                String command = "MI GIVE MATERIAL STEEL_INGOT " + p.getName();
                                Bukkit.dispatchCommand(console, command);
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&lSelamat! &a"+p.getName()+ " &fbaru saja mendapatkan &f&lSteel Ingot&f!"));
                                for (Player online : Bukkit.getOnlinePlayers()){
                                    online.playSound(online.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100F, 1F);
                                }
                            }
                            if (name.contains("patriot_badge")) {
                                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                                String command = "MI GIVE MATERIAL PATRIOT_BADGE " + p.getName();
                                Bukkit.dispatchCommand(console, command);
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&lSelamat! &a"+p.getName()+ " &fbaru saja mendapatkan &2&lPatriot Badge&f!"));
                                for (Player online : Bukkit.getOnlinePlayers()){
                                    online.playSound(online.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100F, 1F);
                                }
                            }
                            if (name.contains("m1_garand_part")) {
                                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                                String command = "MI GIVE MATERIAL M1_GARAND_PART " + p.getName();
                                Bukkit.dispatchCommand(console, command);
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&lSelamat! &a"+p.getName()+ " &fbaru saja mendapatkan &2&lM1 Garand Part&f!"));
                                for (Player online : Bukkit.getOnlinePlayers()){
                                    online.playSound(online.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100F, 1F);
                                }
                            }
                            if (name.contains("proclamation_paper")) {
                                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                                String command = "MI GIVE MATERIAL PROCLAMATION_PAPER " + p.getName();
                                Bukkit.dispatchCommand(console, command);
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&lSelamat! &a"+p.getName()+ " &fbaru saja mendapatkan &e&lProclamation Paper&f!"));
                                for (Player online : Bukkit.getOnlinePlayers()){
                                    online.playSound(online.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100F, 1F);
                                }
                            }
                            if (name.contains("sickle_handle")) {
                                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                                String command = "MI GIVE MATERIAL SICKLE_HANDLE " + p.getName();
                                Bukkit.dispatchCommand(console, command);
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&e&lSelamat! &a"+p.getName()+ " &fbaru saja mendapatkan &f&lSickle Handle&f!"));
                                for (Player online : Bukkit.getOnlinePlayers()){
                                    online.playSound(online.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100F, 1F);
                                }
                            }
                            cancel();
                        }
                    }


                }
            }
        }.runTaskTimer(plugin, 1, 1);


        try {
            protocolManager.sendServerPacket(p, packet);
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }
    public static Boolean percentChance(double chance) {
        return Math.random() <= chance;
    }
}
