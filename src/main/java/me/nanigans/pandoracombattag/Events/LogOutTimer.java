package me.nanigans.pandoracombattag.Events;

import me.nanigans.pandoracombattag.PandoraCombatTag;
import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagDouble;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.UUID;

public class LogOutTimer extends TimerTask {
    private final Player player;
    private final static PandoraCombatTag plugin = PandoraCombatTag.getPlugin(PandoraCombatTag.class);
    public final static Map<UUID, LogOutTimer> timers = new HashMap<>();
    public LogOutTimer(Player player){
        this.player = player;
    }

    @Override
    public void run() {

        File playerFile = new File(plugin.getServer().getWorldContainer().getAbsolutePath().replace(".", "")
                + player.getWorld().getName() + File.separator
                + "playerdata", player.getUniqueId().toString() + ".dat");

        NBTTagCompound nbt = null;
        try {
            nbt = NBTCompressedStreamTools.a(new FileInputStream(playerFile));
            System.out.println("nbt = " + nbt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(nbt != null) {
            NBTTagList newLoc = new NBTTagList();
            newLoc.add(new NBTTagDouble(490));
            newLoc.add(new NBTTagDouble(150));
            newLoc.add(new NBTTagDouble(145));
            nbt.set("Pos", newLoc);
            try {
                NBTCompressedStreamTools.a(nbt, new FileOutputStream(playerFile.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
