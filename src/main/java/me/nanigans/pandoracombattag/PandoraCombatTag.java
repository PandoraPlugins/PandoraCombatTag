package me.nanigans.pandoracombattag;

import me.nanigans.pandoracombattag.Commands.Tag;
import me.nanigans.pandoracombattag.Events.PlayerEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class PandoraCombatTag extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("tag").setExecutor(new Tag());
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

    }

    @Override
    public void onDisable() {
//        Combat.getBossBars().forEach((i, j) -> {
//            BossbarLib.getHandler().clearBossbar(Bukkit.getPlayer(i));
//        });
        // Plugin shutdown logic
    }
}
