/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blockmovers.plugins.punchcard;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

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
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player attacker = null;
        Arrow arrow = null;
        Entity damageSource = event.getDamager();
        if (damageSource instanceof Player) {
            attacker = (Player) damageSource;
        } else if (damageSource instanceof Arrow) {
            arrow = (Arrow) damageSource;
            if (arrow.getShooter() instanceof Player) {
                attacker = (Player) arrow.getShooter();
            }
        } else if (damageSource instanceof ThrownPotion) {
            ThrownPotion potion = (ThrownPotion) damageSource;
            if (potion.getShooter() instanceof Player) {
                attacker = (Player) potion.getShooter();
            }
        }

        if (event.getEntity() instanceof Player && attacker instanceof Player) {
            if (attacker.getName().equalsIgnoreCase(((Player) event.getEntity()).getName())) {
                return; //damaged and attacker are the same
            }
            //PVP stuff
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void OnPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType().equals(EntityType.ITEM_FRAME) && !event.getPlayer().hasPermission("ItemFrame.modify")) {
            //event.getPlayer().sendMessage(ChatColor.RED + "Ah ah ah, you didn't say the magic word!");
            event.setCancelled(true);
        }
    }
}
