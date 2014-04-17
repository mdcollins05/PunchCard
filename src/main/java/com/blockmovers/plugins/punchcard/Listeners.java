/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blockmovers.plugins.punchcard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author MattC
 */
public class Listeners implements Listener {

    PunchCard plugin = null;

    public Listeners(PunchCard plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.PM.isPunchedin(player.getUniqueId())) {
            player.sendMessage(this.plugin.msgPrefix + ChatColor.GREEN + "You are still punched in!");
        }
    }
}
