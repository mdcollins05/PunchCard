package com.blockmovers.plugins.punchcard;

import java.util.logging.Logger;

import com.blockmovers.plugins.punchcard.api.PunchManager;
import com.blockmovers.plugins.punchcard.api.PunchedinPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PunchCard extends JavaPlugin implements Listener {

    Logger log;
    public static Permission perms;
    public Configuration config;
    public PunchManager PM;
    //private Random randomGenerator = new Random();

    public void onEnable() {
        this.log = getLogger();

        PluginDescriptionFile pdffile = this.getDescription();
        PluginManager pm = this.getServer().getPluginManager(); //the plugin object which allows us to add listeners later on

        pm.registerEvents(new Listeners(this), this);

        this.setupPermissions();

        this.PM = new PunchManager(this);

        this.config = new Configuration(this);

        for (String group : this.config.validGroups) {
            org.bukkit.permissions.Permission perm = new org.bukkit.permissions.Permission(group);
            perm.setDefault(PermissionDefault.FALSE);
            this.getServer().getPluginManager().addPermission(perm);
        }

        log.info(pdffile.getName() + " version " + pdffile.getVersion() + " is enabled.");
    }

    public void onDisable() {
        PluginDescriptionFile pdffile = this.getDescription();

        log.info(pdffile.getName() + " version " + pdffile.getVersion() + " is disabled.");
    }

    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("punch")) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("version")) {
                    PluginDescriptionFile pdf = this.getDescription();
                    cs.sendMessage(pdf.getName() + " " + pdf.getVersion() + " by MDCollins05");
                    return true;
                } else if (args[0].equalsIgnoreCase("in")) {
                    if (cs instanceof Player) {
                        Player player = (Player) cs;
                        if (!this.PM.isPunchedin(player.getName())) {
                            for (String group : this.config.validGroups) {
                                if (cs.hasPermission("punchcard." + group)) {
                                    String originalGroup = perms.getPrimaryGroup(player);
                                    this.PM.addPunchedinPlayer(player.getName(), player.getLocation(), originalGroup);
                                    perms.playerRemoveGroup(player, originalGroup);
                                    perms.playerAddGroup(player, group);
                                    player.sendMessage(ChatColor.GREEN + "Time to do work! (Your location has been saved)");
                                    return false;
                                }
                            }
                            player.sendMessage(ChatColor.RED + "You don't have permission to run this command!");
                            return false;
                        } else {
                            player.sendMessage(ChatColor.RED + "You are already punched in!");
                            return false;
                        }
                    } else {
                        cs.sendMessage(ChatColor.RED + "You must be a player!");
                        return false;
                    }
                } else if (args[0].equalsIgnoreCase("out")) {
                    if (cs instanceof Player) {
                        Player player = (Player) cs;
                        if (this.PM.isPunchedin(cs.getName())) {
                            PunchedinPlayer pp = this.PM.getPunchedinPlayer(player.getName());
                            perms.playerRemoveGroup(player, perms.getPrimaryGroup(player));
                            perms.playerAddGroup(player, pp.getOriginalGroup());
                            player.teleport(pp.getOriginalLocation());
                            this.PM.removePunchedinPlayer(player.getName());
                            player.sendMessage(ChatColor.RED + "You are already no longer punched in and have been returned to your original location!");
                            return false;
                        } else {
                            player.sendMessage(ChatColor.RED + "You are already punched in!");
                            return false;
                        }
                    } else {
                        cs.sendMessage(ChatColor.RED + "You must be a player!");
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                if (cs instanceof Player) {
                    Player player = (Player) cs;
                    if (this.PM.isPunchedin(alias)) {
                        player.sendMessage("You are punched in!");
                    } else {
                        player.sendMessage("You are not punched in!");
                    }
                }
            }
            return true;
        }

        return false;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms
                != null;
    }
}
