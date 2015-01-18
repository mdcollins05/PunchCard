/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blockmovers.plugins.punchcard;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MattC
 */
public class Configuration
{

    PunchCard plugin = null;
    public String seperator = ".";
    public List<String> validGroups = new ArrayList();

    public Configuration(PunchCard plugin)
    {
        this.plugin = plugin;
    }

    public void reloadConfiguration()
    {
        this.plugin.reloadConfig();
        this.loadConfiguration();
    }

    public void loadConfiguration()
    {
        plugin.getConfig().addDefault("valid_groups_comment", "This is a list of groups that someone with the right permissions can change to.");
        List<String> groups = new ArrayList();
        groups.add("mod");
        groups.add("admin");

        plugin.getConfig().addDefault("valid_groups", groups);

        plugin.getConfig().options().copyDefaults(true);
        //Save the config whenever you manipulate it
        plugin.saveConfig();

        this.setVars();
    }

    public void setVars()
    {
        this.validGroups = plugin.getConfig().getStringList("valid_groups");
    }
}
