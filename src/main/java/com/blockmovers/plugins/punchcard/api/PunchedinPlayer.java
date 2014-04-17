/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blockmovers.plugins.punchcard.api;

import java.util.UUID;

import org.bukkit.Location;

/**
 *
 * @author MattC
 */
public class PunchedinPlayer {

    private UUID player;
    private Location originalLocation;
    private String originalGroup;

    public PunchedinPlayer(UUID player) {
        this.player = player;
    }

    public UUID getPlayerUUID() {
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
