/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blockmovers.plugins.punchcard.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.blockmovers.plugins.punchcard.PunchCard;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 *
 * @author MattC
 */
public class PunchManager extends ConfigHelper {
    
    private PunchCard plugin;
    private Map<String, PunchedinPlayer> punchedinplayers = new HashMap();
    
    public PunchManager(PunchCard plugin) {
        this.setPlugin(plugin);
        this.setConfigFileName("punchedin.yml");
        this.loadConfig();
        this.plugin = plugin;
    }

    public PunchedinPlayer addPunchedinPlayer(String name, Location originalLocation, String originalGroup) {
        if (this.punchedinplayers.containsKey(name)) {
            return null;
        }
        PunchedinPlayer pp = new PunchedinPlayer(name);
        pp.setOriginalLocation(originalLocation);
        pp.setOriginalGroup(originalGroup);
        this.punchedinplayers.put(name, pp);
        return pp;
    }
    
    
    public PunchedinPlayer getPunchedinPlayer(String name) {
        if (this.punchedinplayers.containsKey(name)) {
            return this.punchedinplayers.get(name);
        }
        return null;
    }
    
    public Boolean removePunchedinPlayer(String name) {
        if (this.punchedinplayers.containsKey(name)) {
            this.punchedinplayers.remove(name);
            return true;
        }
        return false;
    }
    
    public Boolean isPunchedin(String name) {
        if (this.punchedinplayers.containsKey(name)) {
            return true;
        }
        return false;
    }
    
    public String locationToString(Location l) {
        StringBuilder sb = new StringBuilder();
        sb.append(l.getWorld().getName()).append(":")
                .append(l.getX()).append(":")
                .append(l.getY()).append(":")
                .append(l.getZ()).append(":")
                .append(l.getYaw()).append(":")
                .append(l.getPitch());
        return sb.toString();
    }
    
    public Location stringToLocation(String l) {
        String[] lSplit = l.split(":");
        try {
            return new Location(Bukkit.getWorld(lSplit[0]), Double.valueOf(lSplit[1]), Double.valueOf(lSplit[2]), Double.valueOf(lSplit[3]), Float.valueOf(lSplit[4]), Float.valueOf(lSplit[5]));
        } catch (Exception e) {
            return null;
        }
    }
    
    public void savePlayers() {
        for (PunchedinPlayer pp : this.punchedinplayers.values()) {
            this.config.set(pp.getPlayer() + sep + "originalLocation", this.locationToString(pp.getOriginalLocation()));
            this.config.set(pp.getPlayer() + sep + "originalGroup", pp.getOriginalGroup());
        }
    }
    
    public void loadPlayers() {
        Set<String> players = config.getConfigurationSection("").getKeys(false);
        if (!players.isEmpty()) {
            for (String player : players) {
                PunchedinPlayer pp = new PunchedinPlayer(player);
                pp.setOriginalLocation(this.stringToLocation(config.getString(player + sep + "originalLocation")));
                pp.setOriginalGroup(config.getString(player + sep + "originalGroup"));
                this.punchedinplayers.put(player, pp);
            }
        }
    }
}
