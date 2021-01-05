package me.nanigans.pandoracombattag.CombatTag;

import me.nanigans.pandoracombattag.PandoraCombatTag;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Combat implements Listener {

    public final static Map<UUID, Combat> tagged = new HashMap<>();
    private final Player player;
    private static final PandoraCombatTag plugin = PandoraCombatTag.getPlugin(PandoraCombatTag.class);
    private final int level;
    private CombatTimer timer;
    private final float pXp;
    private final CombatTimer combatTimer;

    public Combat(Player player){
        this.player = player;
        timer = new CombatTimer(this);
        this.pXp = player.getExp();
        this.level = player.getLevel();
        combatTimer = new CombatTimer(this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        tagged.put(player.getUniqueId(), this);
    }

    public void startCounting(){
        player.setLevel(30);
        player.setExp(1F);
        combatTimer.run();
    }

    public void endCombat(){
        player.setExp(this.pXp);
        player.setLevel(this.level);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLeave(PlayerQuitEvent event){

        final Player player = event.getPlayer();
        if(player.getUniqueId().equals(this.player.getUniqueId())){
            player.setHealth(0);
            final PlayerInventory inventory = player.getInventory();
            for (ItemStack itemStack : inventory) {
                if(itemStack != null && itemStack.getType() != Material.AIR) {
                    player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                    inventory.removeItem(itemStack);
                }
            }
            for (ItemStack armorContent : inventory.getArmorContents()) {
                if(armorContent != null && armorContent.getType() != Material.AIR){
                    player.getWorld().dropItemNaturally(player.getLocation(), armorContent);
                }
            }
            inventory.setHelmet(null);
            inventory.setChestplate(null);
            inventory.setLeggings(null);
            inventory.setBoots(null);
        }

    }

    @EventHandler
    public void onBowBoost(EntityDamageByEntityEvent event){

        if (event.getDamager() instanceof Arrow) {
            if(((Player) ((Projectile) event.getDamager()).getShooter()).getUniqueId().equals(this.player.getUniqueId())){
                timer.reset();
            }
        }

    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){

        if(event.getPlayer().getUniqueId().equals(this.player.getUniqueId())){

            if (event.getMessage().contains("echest")) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED+"You cannot use this command in combat for another: "
                        +timer.getLevel()+" seconds");
            }

        }

    }

    public CombatTimer getTimer() {
        return timer;
    }

    public static PandoraCombatTag getPlugin() {
        return plugin;
    }

    public Player getPlayer() {
        return player;
    }


}
