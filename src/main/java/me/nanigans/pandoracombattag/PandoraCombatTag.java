package me.nanigans.pandoracombattag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.nanigans.pandoracombattag.CombatTag.Combat;
import me.nanigans.pandoracombattag.Commands.Tag;
import me.nanigans.pandoracombattag.Events.LogOutTimer;
import me.nanigans.pandoracombattag.Events.PlayerEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class PandoraCombatTag extends JavaPlugin {
    public static GsonBuilder gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<Map<String, Object>>(){}.getType(),  new CustomizedObjectTypeAdapter());
    public HashMap map = new HashMap<>();
    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("tag").setExecutor(new Tag());
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

        File configFile = JsonUtil.jsonPath;
        if(!configFile.exists()) {

            saveResource(configFile.getName(), false);
            try {
                Gson gson = gsonBuilder.create();

                map = gson.fromJson(new FileReader(configFile), HashMap.class);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onDisable() {
        LogOutTimer.timers.forEach((i, j) -> j.updateOfflineLoc());
        Combat.tagged.forEach((i, j) -> {
            j.endCombat();
        });
    }
}
