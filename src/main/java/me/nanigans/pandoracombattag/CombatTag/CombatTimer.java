package me.nanigans.pandoracombattag.CombatTag;


import me.nanigans.pandoracombattag.JsonUtil;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;

public class CombatTimer extends BukkitRunnable {

    private static final double duration = Double.parseDouble(JsonUtil.getData("CombatTag.duration").toString());
    private static final double section = 1D / duration;
    private static final double sectionAdd = section / 20D;
    private final Combat combat;
    private final String msg = JsonUtil.getData("CombatTag.message").toString();
    private final String message = ChatColor.translateAlternateColorCodes('&', msg);
    private int t = 1;

    public CombatTimer(Combat combat) {
        this.combat = combat;
        sendActionText(combat.getPlayer(), message);

    }

    @Override
    public void run() {
        final Player player = combat.getPlayer();
        combat.setXp((float) (this.combat.getXp() - sectionAdd));
        player.setExp(combat.getXp());
        if (t % 20 == 0) {
            combat.setpLevel(combat.getpLevel() - 1);
            player.setLevel(combat.getpLevel());
            sendActionText(combat.getPlayer(), message);
        }
        if (combat.getXp() <= 0 || combat.getpLevel() <= 0) {
            this.cancel();
            combat.endCombat();
            HandlerList.unregisterAll(combat);
        }
        t++;
    }

    public synchronized void reset() {
        combat.setXp(1F);
        combat.getPlayer().setExp(combat.getXp());
        combat.setpLevel((int) duration);
        combat.getPlayer().setLevel(combat.getpLevel());
    }

    public void sendActionText(Player player, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static double getDuration() {
        return duration;
    }
}
