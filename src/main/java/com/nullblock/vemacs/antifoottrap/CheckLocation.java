package com.nullblock.vemacs.antifoottrap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ConcurrentHashMap;

public class CheckLocation extends BukkitRunnable {

    public static int interval = 2;
    public float sec = 10;

    public CheckLocation() {

    }

    public void run() {
        for (ConcurrentHashMap.Entry<String, Location> entry : AntiFootTrap.triggeredPlayers.entrySet()) {
            Player p = Bukkit.getPlayer(entry.getKey());
            if (p != null) {
                Location l1 = entry.getValue();
                entry.setValue(p.getLocation());
                if (isSameXZ(l1, p.getLocation())) {
                    AntiFootTrap.playerTimes.put(p.getName(), AntiFootTrap.playerTimes.get(p.getName()) + 1);
                    // getPlugin().getLogger().info(p.getName() + "has had " + AntiFootTrap.playerTimes.get(p.getName()) + " passes");
                    if (AntiFootTrap.playerTimes.get(p.getName()) >= sec * (20 / interval)) {
                        String[] config = getPlugin().getConfig().getString("spawn").split(",");
                        int[] spawn = new int[3];
                        spawn[0] = Integer.parseInt(config[0]);
                        spawn[1] = Integer.parseInt(config[1]);
                        spawn[2] = Integer.parseInt(config[2]);
                        Location loc = new Location(p.getWorld(), spawn[0], spawn[1] + 2, spawn[2]);
                        p.teleport(loc);
                        AntiFootTrap.cleanup(p.getName());
                        getPlugin().getLogger().info(p.getName() + " has been TPed to spawn");
                    }
                } else {
                    AntiFootTrap.cleanup(p.getName());
                }
            }
            else {
                AntiFootTrap.cleanup(entry.getKey());
            }
        }
    }

    private boolean isSameXZ(Location loc1, Location loc2) {
        float range = 1.1F;
        if (Math.abs(loc1.getX() - loc2.getX()) < (range / 2) && Math.abs(loc1.getZ() - loc2.getZ()) < (range / 2)) {
            return true;
        }
        return false;
    }

    private Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin("AntiFootTrap");
    }

}
