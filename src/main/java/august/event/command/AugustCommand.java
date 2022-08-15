package august.event.command;

import august.event.Main;
import august.event.utils.KemerdekaanPoints;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AugustCommand implements CommandExecutor {
    private final Main plugin;
    public Inventory inv;
    public HashMap<String, Long> commandcd = new HashMap<>();

    public AugustCommand(Main plugin){
        this.plugin = plugin;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            if (command.getName().equalsIgnoreCase("showkemerdekaanpoints")){
                Player p = (Player) sender;
                int cooldownTime = 180;
                if (commandcd.containsKey(p.getName())) {
                    long secondsLeft = ((commandcd.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
                    if (secondsLeft > 0) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can use that in " + secondsLeft + "s again!"));
                    } else {
                        commandcd.remove(p.getName());
                    }
                } else {
                    commandcd.put(p.getName(), System.currentTimeMillis());
                    try {
                        KemerdekaanPoints kemerdekaanPoints = this.plugin.getDatabase().getPlayerStatsByName(p.getName());
                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&a" + p.getName() + " &fmemiliki &cKemerdekaan &fPoints sebanyak &e" + kemerdekaanPoints.getKemerdekaanPoints()));
                        Bukkit.broadcastMessage("");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (command.getName().equalsIgnoreCase("17agustus")){
                if (args.length == 0) {
                    Player p = (Player) sender;
                    sender.sendMessage("- clear");
                    sender.sendMessage("- addpoint <player> <value>");
                    sender.sendMessage("- removepoint <player> <value>");
                    sender.sendMessage("- setpoint <player> <value>");
                }else{
                    if (args[0].equalsIgnoreCase("clear")){
                        try {
                            plugin.getDatabase().clearDatabase();
                            sender.sendMessage("Successfully DROP TABLE!");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (args[0].equalsIgnoreCase("setpoint")){
                        if (args.length == 3){
                            String target = args[1];
                            double value = Double.parseDouble(args[2]);
                            try {
                                KemerdekaanPoints kemerdekaanPoints = this.plugin.getDatabase().getPlayerStatsByName(target);
                                if (kemerdekaanPoints != null) {
                                    kemerdekaanPoints = new KemerdekaanPoints(target, value);
                                    this.plugin.getDatabase().updatePlayerStats(kemerdekaanPoints);
                                    sender.sendMessage("Successfully set " +value+ " to " +target);
                                }else{
                                    sender.sendMessage("Failed to set value to " +target);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else if (args[0].equalsIgnoreCase("addpoint")){
                        if (args.length == 3){
                            String target = args[1];
                            double value = Double.parseDouble(args[2]);
                            try {
                                KemerdekaanPoints kemerdekaanPoints = this.plugin.getDatabase().getPlayerStatsByName(target);
                                if (kemerdekaanPoints != null) {
                                    kemerdekaanPoints = new KemerdekaanPoints(target, kemerdekaanPoints.getKemerdekaanPoints() + value);
                                    this.plugin.getDatabase().updatePlayerStats(kemerdekaanPoints);
                                    sender.sendMessage("Successfully add " +value+ " to " +target);
                                }else{
                                    sender.sendMessage("Failed to add value to " +target);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else if (args[0].equalsIgnoreCase("removepoint")){
                        if (args.length == 3){
                            String target = args[1];
                            double value = Double.parseDouble(args[2]);
                            try {
                                KemerdekaanPoints kemerdekaanPoints = this.plugin.getDatabase().getPlayerStatsByName(target);
                                if (kemerdekaanPoints != null) {
                                    kemerdekaanPoints = new KemerdekaanPoints(target, kemerdekaanPoints.getKemerdekaanPoints() - value);
                                    this.plugin.getDatabase().updatePlayerStats(kemerdekaanPoints);
                                    sender.sendMessage("Successfully remove " +value+ " from " +target);
                                }else{
                                    sender.sendMessage("Failed to remove value from " +target);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            if (command.getName().equalsIgnoreCase("setcenterevent")){
                if (args.length == 1) {
                    Player p = (Player) sender;
                    plugin.location.getConfig("centerlocation.yml").set("center", serialize((p.getLocation())));
                    plugin.location.getConfig("centerlocation.yml").set("radius", args[0]);
                    plugin.location.saveConfig("centerlocation.yml");
                    sender.sendMessage("Successfully set location to " + ((Player) sender).getLocation());
                }else{
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSilahkan masukkan radius!"));
                }
            }
            if (command.getName().equalsIgnoreCase("buyrpglimited")){
                if (args.length == 0){
                    Player p = (Player) sender;
                    int slot = 45;
                    inv = Bukkit.getServer().createInventory(p, slot, "17 Agustus RPG");
                    for (int i = 0 ; i < slot ; i++){
                        if (i == 0 || i == 1 || i == 2 || i == 3 || i == 5 || i == 6 || i == 7 ||
                                i == 8 || i == 9 || i == 17 || i == 18 ||  i == 26 || i == 27 ||
                                i == 35 || i == 36 || i == 37 || i == 38 || i == 39 || i == 40 ||
                                i == 41 || i == 42 || i == 43 || i == 44){
                            inv.setItem(i, fillGUI(Material.GRAY_STAINED_GLASS_PANE, "&a"));
                        }
                        if (i == 4){
                            try {
                                KemerdekaanPoints kemerdekaanPoints = plugin.getDatabase().getPlayerStatsByName(p.getName());
                                List<String> lore = new ArrayList<>();
                                lore.add("");
                                lore.add("&dYour Kemerdekaan Points : &f" + kemerdekaanPoints.getKemerdekaanPoints());
                                lore.add("");
                                inv.setItem(i, createItem(Material.SUNFLOWER, "&cKemerdekaan &fProfile", lore));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }
                        if (i == 10){
                            List<String> lore = new ArrayList<>();
                            lore.add("");
                            lore.add("&aStab Them (&ePassive&a, &eBACKSTAB&a):");
                            lore.add("&aUpon backstabbing your enemies, your damage");
                            lore.add("&awill be increased by x2 from your normal");
                            lore.add("&anormal damage and make them suffer.");
                            lore.add("&eOnly works for player");
                            lore.add("&8Created by Mornov");
                            lore.add("&7&o\"Digoenakan oleh Djawara Betawi pada djamannya!\"");
                            lore.add("");
                            lore.add("&ePrice :");
                            lore.add("&7- &c8500 &fKemerdekaan Points");
                            lore.add("&7- &cx2 &fSteel Ingot");

                            inv.setItem(i, createItem(Material.STONE_SWORD, "&f&lGolok", lore));
                        }
                        if (i == 11){
                            List<String> lore = new ArrayList<>();
                            lore.add("");
                            lore.add("&aWarrior Spirit (&ePassive&a, &eHITTEN&a):");
                            lore.add("&aIf you got hitten by your enemies, you got");
                            lore.add("&a40% chance to deflect their attack");
                            lore.add("&8Created by Mornov");
                            lore.add("");
                            lore.add("&7&o\"Bangsa yang besar adalah bangsa");
                            lore.add("&7&oyang menghargai jasa pahlawannya\"");
                            lore.add("");
                            lore.add("&ePrice :");
                            lore.add("&7- &c9500 &fKemerdekaan Points");
                            lore.add("&7- &cx2 &2Patriot Badge");

                            inv.setItem(i, createItem(Material.TURTLE_HELMET, "&2&lPatriot Helmet", lore));
                        }
                        if (i == 12){
                            List<String> lore = new ArrayList<>();
                            lore.add("");
                            lore.add("&8Created by Mornov");
                            lore.add("");
                            lore.add("&7&o\"Sendjata jang dirampas oleh Indonesia dari tentara Belanda\"");
                            lore.add("");
                            lore.add("&ePrice :");
                            lore.add("&7- &c7500 &fKemerdekaan Points");
                            lore.add("&7- &cx2 &fM1 Garand Part");

                            inv.setItem(i, createItem(Material.GOLDEN_HORSE_ARMOR, "&e&lM1 Garand", lore));
                        }
                        if (i == 13){
                            List<String> lore = new ArrayList<>();
                            lore.add("&aSpirit Synchronization (&eRIGHT-CLICK&a, &e60s Cooldown&a):");
                            lore.add("&aYou got an chance between &bSpirit Charge &aand");
                            lore.add("&bSpirit Banishment &aif you using this book");
                            lore.add("&a");
                            lore.add("&aSpirit Charge (&eACTIVE&a):");
                            lore.add("&aWhile you activated this skills, you will");
                            lore.add("&againing buff by &bSpeed II &aand &bStrength III &afor");
                            lore.add("&a5 seconds after using this skills");
                            lore.add("&a");
                            lore.add("&aSpirit Banishment (&eACTIVE&a):");
                            lore.add("&aSet yourself on spirit and makes");
                            lore.add("&ayourself stronger, next hit will");
                            lore.add("&abe &c30 &atrue damages in one hit");
                            lore.add("&8Created by Mornov");
                            lore.add("&a");
                            lore.add("&7&o\"Dengan ini menjatakan kemerdekaan Indonesia\"");
                            lore.add("");
                            lore.add("&ePrice :");
                            lore.add("&7- &c8000 &fKemerdekaan Points");
                            lore.add("&7- &cx2 &fProclamation Paper");

                            inv.setItem(i, createItem(Material.BOOK, "&e&lBuku Proklamasi", lore));
                        }
                        if (i == 14){
                            List<String> lore = new ArrayList<>();
                            lore.add("");
                            lore.add("&7&oComing Soon");
                            lore.add("");
                            lore.add("&ePrice :");
                            lore.add("&7- &c10000 &fKemerdekaan Points");
                            lore.add("&7- &cx2 &fSickle Handle");

                            inv.setItem(i, createItem(Material.IRON_HOE, "&f&lCelurit", lore));
                        }
                    }
                    p.openInventory(inv);
                }
            }
            if (command.getName().equalsIgnoreCase("buyrelic")) {
                if (args.length == 0) {
                    Player p = (Player) sender;
                    int slot = 45;
                    inv = Bukkit.getServer().createInventory(p, slot, "Buy Relic");
                    for (int loop = 0;loop < slot; loop++) {
                        inv.setItem(loop, fillGUI(Material.BLACK_STAINED_GLASS_PANE, "&a"));
                        if (loop == 21) {
                            List<String> lore = new ArrayList<>();
                            lore.add("");
                            lore.add("&6Harga : 20000 Peso");
                            lore.add("");
                            inv.setItem(loop, createItem(Material.WOODEN_HOE, "&cWooden Relic", lore));
                        }
                        if (loop == 23) {
                            List<String> lore = new ArrayList<>();
                            lore.add("");
                            lore.add("&aMarker (&eRIGHT-CLICK&a, &e180s Cooldown&a):");
                            lore.add("&aPut mark on your Treasure for 10 seconds.");
                            lore.add("");
                            lore.add("&6Harga : 50000 Peso");
                            lore.add("");
                            inv.setItem(loop, createItem(Material.IRON_HOE, "&cIron Relic", lore));
                        }
                    }
                    p.openInventory(inv);
                }
            }
        }
        return false;
    }
    public ItemStack fillGUI(Material material, String name) {
        name = ChatColor.translateAlternateColorCodes('&', name);
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);

        return item;
    }
    public ItemStack createItem(Material material, String name, List<String> lore) {
        name = ChatColor.translateAlternateColorCodes('&', name);
        lore = lore.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setLore(lore);

        item.setItemMeta(meta);

        return item;
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
