/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.trickypr.removeitems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.ArrayList;

public class Plugin extends JavaPlugin implements Listener, CommandExecutor {
    FileConfiguration config = getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Reloading config");

        reloadConfig();
        config = getConfig();

        sender.sendMessage("Reloaded config");

        return true;
    }

    @Override
    public void onEnable() {
        config.addDefault("banned_items", new ArrayList<String>());

        // Configure enchantment levels
        config.addDefault("items.max_level", 5);
        config.addDefault("items.max_effect", 0);

        config.options().copyDefaults(true);
        this.saveConfig();

        getServer().getPluginManager().registerEvents(this, this);
        getCommand("removeitems").setExecutor(this);
        this.reloadConfig();
    }

    protected boolean shouldRemove(ItemStack item) {
        ArrayList<String> bannedItems = (ArrayList<String>) config.get("banned_items");

        for (String bannedItem : bannedItems) {
            if (item == null) continue;

            if (item.getType().getKey().asString().equals(bannedItem)) {
                return true;
            }
        }

        return false;
    }

    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent event) {
        ItemStack[] contents = event.getInventory().getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (shouldRemove(item)) {
                contents[i] = new ItemStack(Material.AIR);
            }
        }

        event.getInventory().setContents(contents);
    }
}
