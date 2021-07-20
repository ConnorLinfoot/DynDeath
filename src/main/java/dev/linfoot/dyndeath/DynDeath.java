package dev.linfoot.dyndeath;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;

import java.util.logging.Level;

public class DynDeath extends JavaPlugin {

    @Override
    public void onEnable() {
        // Setup config
        getConfig().options().copyDefaults(true);
        saveConfig();

        DynmapAPI dynmap = getDynmap();
        if (dynmap == null) {
            getLogger().log(Level.SEVERE, "Failed to load DynDeath, Dynmap could not be found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        DeathListener listener = new DeathListener(this);
        getServer().getPluginManager().registerEvents(listener, this);
        Bukkit.getScheduler().runTaskTimer(this, listener, 5 * 20, 5 * 20);
    }

    @Override
    public void onDisable() {

    }

    int getDuration() {
        return getConfig().getInt("Duration", 15);
    }

    DynmapAPI getDynmap() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Dynmap");
        if (plugin instanceof DynmapAPI) {
            return (DynmapAPI) plugin;
        }
        return null;
    }

}
