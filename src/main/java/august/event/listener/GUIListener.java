package august.event.listener;

import august.event.Main;
import august.event.utils.KemerdekaanPoints;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GUIListener implements Listener {
    private final Main plugin;
    private static Economy econ;

    public GUIListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void buyRelic(InventoryClickEvent e) throws SQLException {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("17 Agustus RPG")) {
            e.setCancelled(true);
            try {
                KemerdekaanPoints kemerdekaanPoints = this.plugin.getDatabase().getPlayerStatsByName(p.getName());
                if (kemerdekaanPoints == null) {
                    kemerdekaanPoints = new KemerdekaanPoints(p.getName(), 0);
                    this.plugin.getDatabase().createPlayerStats(kemerdekaanPoints);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (e.getSlot() == 10) {
                KemerdekaanPoints kemerdekaanPoints = plugin.getDatabase().getPlayerStatsByName(p.getName());
                if (kemerdekaanPoints.getKemerdekaanPoints() >= 8500){
                    if (getAmount(p, Material.PLAYER_HEAD, "&f&lSteel Ingot") >= 2) {
                        removeItem(p, Material.PLAYER_HEAD, "&f&lSteel Ingot");
                        kemerdekaanPoints.setKemerdekaanPoints(kemerdekaanPoints.getKemerdekaanPoints() - 8500);
                        plugin.getDatabase().updatePlayerStats(kemerdekaanPoints);
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        String command = "MI GIVE DAGGER GOLOK " + p.getName();
                        Bukkit.dispatchCommand(console, command);

                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&e&lEvent! &a" + p.getName() + " &fbaru saja membeli &f&lGolok&f!"));
                        for (Player online : Bukkit.getOnlinePlayers()){
                            online.playSound(online.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100F, 0F);
                        }
                    }else{
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cKamu tidak memiliki Steel Ingot yang cukup!"));
                    }
                }
                else{
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cKamu tidak memiliki Kemerdekaan Points yang cukup!"));
                }
            }
            if (e.getSlot() == 11) {
                KemerdekaanPoints kemerdekaanPoints = plugin.getDatabase().getPlayerStatsByName(p.getName());
                if (kemerdekaanPoints.getKemerdekaanPoints() >= 9500){
                    if (getAmount(p, Material.PLAYER_HEAD, "&2&lPatriot Badge") >= 2) {
                        removeItem(p, Material.PLAYER_HEAD, "&2&lPatriot Badge");
                        kemerdekaanPoints.setKemerdekaanPoints(kemerdekaanPoints.getKemerdekaanPoints() - 9500);
                        plugin.getDatabase().updatePlayerStats(kemerdekaanPoints);
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        String command = "MI GIVE ARMOR PATRIOT_HELMET " + p.getName();
                        Bukkit.dispatchCommand(console, command);
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&e&lEvent! &a" + p.getName() + " &fbaru saja membeli &2&lPatriot Helmet&f!"));
                        for (Player online : Bukkit.getOnlinePlayers()){
                            online.playSound(online.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100F, 0F);
                        }
                    }else{
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cKamu tidak memiliki Patriot Badge yang cukup!"));
                    }
                }
                else{
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cKamu tidak memiliki Kemerdekaan Points yang cukup!"));
                }
            }
            if (e.getSlot() == 12) {
                KemerdekaanPoints kemerdekaanPoints = plugin.getDatabase().getPlayerStatsByName(p.getName());
                if (kemerdekaanPoints.getKemerdekaanPoints() >= 7500){
                    if (getAmount(p, Material.PLAYER_HEAD, "&e&lM1 Garand Part") >= 2) {
                        removeItem(p, Material.PLAYER_HEAD, "&e&lM1 Garand Part");
                        kemerdekaanPoints.setKemerdekaanPoints(kemerdekaanPoints.getKemerdekaanPoints() - 7500);
                        plugin.getDatabase().updatePlayerStats(kemerdekaanPoints);
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        String command = "MI GIVE MUSKET M1_GARAND " + p.getName();
                        Bukkit.dispatchCommand(console, command);

                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&e&lEvent! &a" + p.getName() + " &fbaru saja membeli &e&lM1 Garand&f!"));
                        for (Player online : Bukkit.getOnlinePlayers()){
                            online.playSound(online.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100F, 0F);
                        }
                    }else{
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cKamu tidak memiliki M1 Garand Part yang cukup!"));
                    }
                }
                else{
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cKamu tidak memiliki Kemerdekaan Points yang cukup!"));
                }
            }
            if (e.getSlot() == 13) {
                KemerdekaanPoints kemerdekaanPoints = plugin.getDatabase().getPlayerStatsByName(p.getName());
                if (kemerdekaanPoints.getKemerdekaanPoints() >= 8000){
                    if (getAmount(p, Material.PLAYER_HEAD, "&e&lProclamation Paper") >= 2) {
                        removeItem(p, Material.PLAYER_HEAD, "&e&lProclamation Paper");
                        kemerdekaanPoints.setKemerdekaanPoints(kemerdekaanPoints.getKemerdekaanPoints() - 8000);
                        plugin.getDatabase().updatePlayerStats(kemerdekaanPoints);
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        String command = "MI GIVE TOOL BUKU_PROKLAMASI " + p.getName();
                        Bukkit.dispatchCommand(console, command);

                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&e&lEvent! &a" + p.getName() + " &fbaru saja membeli &e&lBuku Proklamasi&f!"));
                        for (Player online : Bukkit.getOnlinePlayers()){
                            online.playSound(online.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100F, 0F);
                        }
                    }else{
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cKamu tidak memiliki Proclamation Paper yang cukup!"));
                    }
                }
                else{
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cKamu tidak memiliki Kemerdekaan Points yang cukup!"));
                }
            }
            if (e.getSlot() == 14) {
                KemerdekaanPoints kemerdekaanPoints = plugin.getDatabase().getPlayerStatsByName(p.getName());
                if (kemerdekaanPoints.getKemerdekaanPoints() >= 10000){
                    if (getAmount(p, Material.PLAYER_HEAD, "&f&lSickle Handle") >= 2) {
                        removeItem(p, Material.PLAYER_HEAD, "&f&lSickle Handle");
                        kemerdekaanPoints.setKemerdekaanPoints(kemerdekaanPoints.getKemerdekaanPoints() - 10000);
                        plugin.getDatabase().updatePlayerStats(kemerdekaanPoints);
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        String command = "MI GIVE LONG_SWORD CELURIT " + p.getName();
                        Bukkit.dispatchCommand(console, command);

                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                "&e&lEvent! &a" + p.getName() + " &fbaru saja membeli &f&lCelurit&f!"));
                        for (Player online : Bukkit.getOnlinePlayers()){
                            online.playSound(online.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 100F, 0F);
                        }
                    }else{
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cKamu tidak memiliki Sickle Handle yang cukup!"));
                    }
                }
                else{
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&cKamu tidak memiliki Kemerdekaan Points yang cukup!"));
                }
            }
        }
        if (e.getView().getTitle().equalsIgnoreCase("Buy Relic")) {
            e.setCancelled(true);
            double balance = Main.econ.getBalance(p.getName());
            if (e.getSlot() == 21) {
                if (balance >= 20000) {
                    ItemStack wooden = new ItemStack(Material.WOODEN_HOE, 1);
                    ItemMeta meta = wooden.getItemMeta();
                    assert meta != null;
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cWooden Relic"));

                    List<String> lore = new ArrayList<>();
                    lore.add("&cTool");
                    lore.add("");
                    lore.add("&7&oThis item doesn't have any abilities..");
                    lore.add("");
                    lore.add("&9&lRARE RELIC");
                    lore = lore.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());

                    meta.setLore(lore);
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    wooden.setItemMeta(meta);

                    p.getInventory().addItem(wooden);

                    EconomyResponse res = Main.econ.withdrawPlayer(p.getName(), 20000);
                    if (res.transactionSuccess()) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aItems added to your inventory!"));
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTransaction Failed!"));
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have enough money to do this!"));
                }
            }
            if (e.getSlot() == 23) {
                if (balance >= 50000) {
                    ItemStack iron = new ItemStack(Material.IRON_HOE, 1);
                    ItemMeta meta = iron.getItemMeta();
                    assert meta != null;
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cIron Relic"));

                    List<String> lore = new ArrayList<>();
                    lore.add("&cTool");
                    lore.add("");
                    lore.add("&aMarker (&eRIGHT-CLICK&a, &e180s Cooldown&a):");
                    lore.add("&aPut mark on your Treasure for 10 seconds.");
                    lore.add("");
                    lore.add("&5&lEPIC RELIC");
                    lore = lore.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());

                    meta.setLore(lore);
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    iron.setItemMeta(meta);

                    p.getInventory().addItem(iron);

                    EconomyResponse res = Main.econ.withdrawPlayer(p.getName(), 50000);
                    if (res.transactionSuccess()) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aItems added to your inventory!"));
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTransaction Failed!"));
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have enough money to do this!"));
                }
            }
        }
    }

    public static int getAmount(Player p, Material mat, String name) {
        if (mat == null) {
            return 0;
        }
        int amount = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item != null) {
                if (item.getType() == mat) {
                    if (item.hasItemMeta() || Objects.requireNonNull(item.getItemMeta()).hasDisplayName()) {
                        if (Objects.requireNonNull(item.getItemMeta()).getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', name))) {
                            amount += item.getAmount();

                        }
                    }
                }
            }
        }
        return amount;
    }
    public void removeItem(Player p, Material mat, String name){
        int loop = 0;
        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null && item.getType().equals(mat) && Objects.requireNonNull(item.getItemMeta()).getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', name))) {
                if (p.getInventory().contains(mat) && item.getAmount() == 1) {
                    if (loop < 2) {
                        item.setAmount(item.getAmount() - 1);
                        loop++;
                    }
                }else{
                    if (p.getInventory().contains(mat) && item.getAmount() > 1) {
                        if (loop < 2) {
                            item.setAmount(item.getAmount() - 2);
                            break;
                        }
                    }
                }
            }
        }
    }
}