package me.nanigans.pandoracombattag;

import me.nanigans.pandoracombattag.Commands.Tag;
import org.bukkit.plugin.java.JavaPlugin;

public final class PandoraCombatTag extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("tag").setExecutor(new Tag());

    }

    @Override
    public void onDisable() {
//        Combat.getBossBars().forEach((i, j) -> {
//            BossbarLib.getHandler().clearBossbar(Bukkit.getPlayer(i));
//        });
        // Plugin shutdown logic
    }
}
