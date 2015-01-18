package com.blockmovers.plugins.punchcard;

import java.util.logging.Logger;

import com.blockmovers.plugins.punchcard.api.PunchManager;
import com.blockmovers.plugins.punchcard.api.PunchedinPlayer;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PunchCard extends JavaPlugin implements Listener
{

    Logger log;
    public static Permission perms;
    public Configuration config;
    public PunchManager PM;
    public String msgPrefix;

    public void onEnable()
    {
        this.log = getLogger();

        msgPrefix = ChatColor.BLACK + "[" + ChatColor.GRAY + "PunchCard" + ChatColor.BLACK + "] " + ChatColor.RESET;

        PluginDescriptionFile pdffile = this.getDescription();
        PluginManager pm = this.getServer().getPluginManager(); //the plugin object which allows us to add listeners later on

        pm.registerEvents(new Listeners(this), this);

        this.setupPermissions();

        this.PM = new PunchManager(this);

        this.config = new Configuration(this);
        this.config.loadConfiguration();

        for (String group : this.config.validGroups)
        {
            org.bukkit.permissions.Permission perm = new org.bukkit.permissions.Permission("punchcard." + group);
            perm.setDefault(PermissionDefault.FALSE);
            this.getServer().getPluginManager().addPermission(perm);
        }

        log.info(pdffile.getName() + " version " + pdffile.getVersion() + " is enabled.");
    }

    public void onDisable()
    {
        PluginDescriptionFile pdffile = this.getDescription();

        log.info(pdffile.getName() + " version " + pdffile.getVersion() + " is disabled.");
    }

    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("punch"))
        {
            if (args.length >= 1)
            {
                if (args[0].equalsIgnoreCase("version"))
                {
                    PluginDescriptionFile pdf = this.getDescription();
                    cs.sendMessage(msgPrefix + pdf.getName() + " " + pdf.getVersion() + " by MDCollins05");
                    return true;
                } else if (args[0].equalsIgnoreCase("in"))
                {
                    if (cs instanceof Player)
                    {
                        Player player = (Player) cs;
                        if (!this.PM.isPunchedin(player.getUniqueId()))
                        {
                            for (String group : this.config.validGroups)
                            {
                                if (cs.hasPermission("punchcard." + group))
                                {
                                    String originalGroup = perms.getPrimaryGroup(player);
                                    this.PM.addPunchedinPlayer(player.getUniqueId(), player.getLocation(), originalGroup);
                                    perms.playerRemoveGroup(player, originalGroup);
                                    perms.playerAddGroup(player, group);
                                    player.sendMessage(msgPrefix + ChatColor.GREEN + "Time to do work! (Your location has been saved)");
                                    return true;
                                }
                            }
                            player.sendMessage(msgPrefix + ChatColor.RED + "You don't have permission to run this command!");
                            return true;
                        } else
                        {
                            player.sendMessage(msgPrefix + ChatColor.RED + "You are already punched in!");
                            return true;
                        }
                    } else
                    {
                        cs.sendMessage(msgPrefix + ChatColor.RED + "You must be a player!");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("out"))
                {
                    if (cs instanceof Player)
                    {
                        Player player = (Player) cs;
                        if (this.PM.isPunchedin(player.getUniqueId()))
                        {
                            PunchedinPlayer pp = this.PM.getPunchedinPlayer(player.getUniqueId());
                            perms.playerRemoveGroup(player, perms.getPrimaryGroup(player));
                            perms.playerAddGroup(player, pp.getOriginalGroup());
                            player.teleport(pp.getOriginalLocation());
                            this.PM.removePunchedinPlayer(player.getUniqueId());
                            player.sendMessage(msgPrefix + ChatColor.RED + "You are now punched out and have been returned to your original location!");
                            return true;
                        } else
                        {
                            player.sendMessage(msgPrefix + ChatColor.RED + "You are already punched out!");
                            return true;
                        }
                    } else
                    {
                        cs.sendMessage(msgPrefix + ChatColor.RED + "You must be a player!");
                        return true;
                    }
                } else
                {
                    return true;
                }
            } else
            {
                if (cs instanceof Player)
                {
                    Player player = (Player) cs;
                    if (this.PM.isPunchedin(player.getUniqueId()))
                    {
                        player.sendMessage(msgPrefix + "You are punched in!");
                    } else
                    {
                        player.sendMessage(msgPrefix + "You are not punched in!");
                    }
                }
            }
            return true;
        }

        return false;
    }

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
}
