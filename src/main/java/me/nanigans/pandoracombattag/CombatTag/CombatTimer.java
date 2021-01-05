package me.nanigans.pandoracombattag.CombatTag;


import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;

public class CombatTimer {

    private final double section = 1D/30D;
    private final double sectionAdd = section/20D;
    private float xp = 1;
    private int level = 30;
    private final Combat combat;
    public CombatTimer(Combat combat) {
        this.combat = combat;
    }

    public void run() {
        final Player player = combat.getPlayer();
        sendActionText(player, "You are in combat");

        new BukkitRunnable() {
            int t = 1;
            @Override
            public void run() {
                player.setExp((float) (xp-sectionAdd));
                xp = player.getExp();
                if(t % 20 == 0){
                    player.setLevel(--level);
                    sendActionText(player, "You are in combat");
                }
                if(xp <= 0 || level <= 0) {
                    this.cancel();
                    combat.endCombat();
                    HandlerList.unregisterAll(combat);
                }
                t++;
            }
        }.runTaskTimerAsynchronously(Combat.getPlugin(), 0, 0);
    }

    public void reset(){
        this.xp = 1;
        this.level = 30;
    }

    public float getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public void sendActionText(Player player, String message){
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}