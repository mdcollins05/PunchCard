/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blockmovers.plugins.punchcard.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MattC
 */
public class ConfigHelper
{

    private Plugin plugin;
    protected FileConfiguration config = null;
    protected String sep = ".";
    private File configFile = null;
    private String configFileName;

    protected void setPlugin(Plugin plugin)
    {
        this.plugin = plugin;
    }

    protected void setConfigFileName(String configFileName)
    {
        this.configFileName = configFileName;
    }

    protected void reloadConfig()
    {
        if (configFile == null)
        {
            configFile = new File(plugin.getDataFolder(), configFileName);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource(configFileName);
        if (defConfigStream != null)
        {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            config.setDefaults(defConfig);
        }
    }

    protected FileConfiguration loadConfig()
    {
        if (config == null)
        {
            reloadConfig();
        }
        return config;
    }

    protected void saveConfig()
    {
        if (config == null || configFile == null)
        {
            return;
        }
        try
        {
            config.save(configFile);
        } catch (IOException ex)
        {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save config to " + configFileName, ex);
        }
    }
}
