/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blockmovers.plugins.punchcard.api;

import org.bukkit.Location;

/**
 *
 * @author MattC
 */
public class PunchedinPlayer {
    
    private String player;
    private Location originalLocation;
    private String originalGroup;
    
    public PunchedinPlayer(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public Location getOriginalLocation() {
        return originalLocation;
    }

    public void setOriginalLocation(Location originalLocation) {
        this.originalLocation = originalLocation;
    }

    public String getOriginalGroup() {
        return originalGroup;
    }

    public void setOriginalGroup(String originalGroup) {
        this.originalGroup = originalGroup;
    }
}
