package com.nullblock.vemacs.antifoottrap;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ConcurrentHashMap;

public class AntiFootTrap extends JavaPlugin implements Listener {

    public static ConcurrentHashMap<String, Location> triggeredPlayers = new ConcurrentHashMap();
    public static ConcurrentHashMap<String, Integer> playerTimes = new ConcurrentHashMap();


    public void onDisable() {
    }

    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();
        BukkitTask task = new CheckLocation().runTaskTimer(this, 0, CheckLocation.interval);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        Entity e = event.getEntity();
        if (e instanceof Player && event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getDamager() instanceof Player) {
            Player p = (Player) e;
            if (!triggeredPlayers.containsKey(p.getName())) {
                triggeredPlayers.put(p.getName(), p.getLocation());
              }
            if (!playerTimes.containsKey(p.getName())) {
                playerTimes.put(p.getName(), 0);
              }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        cleanup(p.getName());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player p = event.getPlayer();
        cleanup(p.getName());
    }

    public static void cleanup(String player) {
        triggeredPlayers.remove(player);
        playerTimes.remove(player);
    }

}
