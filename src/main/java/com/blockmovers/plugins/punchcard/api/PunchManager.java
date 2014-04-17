/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blockmovers.plugins.punchcard.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
        this.loadPlayers();
    }

    public PunchedinPlayer addPunchedinPlayer(UUID uuid, Location originalLocation, String originalGroup) {
        if (this.punchedinplayers.containsKey(uuid.toString())) {
            return null;
        }
        PunchedinPlayer pp = new PunchedinPlayer(uuid);
        pp.setOriginalLocation(originalLocation);
        pp.setOriginalGroup(originalGroup);
        this.punchedinplayers.put(uuid.toString(), pp);
        this.savePlayers();
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
            this.config.set(name, null);
            this.savePlayers();
            return true;
        }
        return false;
    }

    public Boolean isPunchedin(UUID uuid) {
        return this.punchedinplayers.containsKey(uuid.toString());
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
            this.config.set(pp.getPlayerUUID().toString() + sep + "originalLocation", this.locationToString(pp.getOriginalLocation()));
            this.config.set(pp.getPlayerUUID().toString() + sep + "originalGroup", pp.getOriginalGroup());
        }
        this.saveConfig();
    }

    public void loadPlayers() {
        Set<String> players = config.getConfigurationSection("").getKeys(false);
        if (!players.isEmpty()) {
            for (String uuid : players) {
                PunchedinPlayer pp = new PunchedinPlayer(UUID.fromString(uuid));
                pp.setOriginalLocation(this.stringToLocation(config.getString(uuid + sep + "originalLocation")));
                pp.setOriginalGroup(config.getString(uuid + sep + "originalGroup"));
                this.punchedinplayers.put(uuid, pp);
            }
        }
    }
}
