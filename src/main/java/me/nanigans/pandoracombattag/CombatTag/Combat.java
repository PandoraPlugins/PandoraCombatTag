package me.nanigans.pandoracombattag.CombatTag;

import me.nanigans.pandoracombattag.JsonUtil;
import me.nanigans.pandoracombattag.PandoraCombatTag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.json.simple.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Combat implements Listener {

    public final static Map<UUID, Combat> tagged = new HashMap<>();
    private final Player player;
    private static final PandoraCombatTag plugin = PandoraCombatTag.getPlugin(PandoraCombatTag.class);
    private final int level;
    private final CombatTimer timer;
    private final float pXp;
    private float xp = 1;
    private int pLevel = (int) CombatTimer.getDuration();
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
        combatTimer.runTaskTimerAsynchronously(plugin, 0, 0);
    }

    public void endCombat(){
        player.setExp(this.pXp);
        player.setLevel(this.level);
        tagged.remove(player.getUniqueId());
        combatTimer.cancel();
        HandlerList.unregisterAll(this);
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
    public void onDeath(PlayerDeathEvent event){
        if(event.getEntity().getUniqueId().equals(this.player.getUniqueId())){
            combatTimer.cancel();
            HandlerList.unregisterAll(this);
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

        if(event.getPlayer().getUniqueId().equals(this.player.getUniqueId())) {

            JSONArray commandsDisabled = (JSONArray) JsonUtil.getData("CombatTag.disabledCommands");

            if (commandsDisabled != null) {
                String commandName = event.getMessage().split(" ")[0].substring(1);
                if(commandName.contains(":")) commandName = commandName.split(":")[1];

                final Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
                for (Plugin plugin : plugins) {
                    final PluginDescriptionFile description = plugin.getDescription();
                    final Map<String, Map<String, Object>> commands = description.getCommands();
                    if(commands != null) {
                        for (Map.Entry<String, Map<String, Object>> cmdEntry : commands.entrySet()) {

                            final String s = ChatColor.translateAlternateColorCodes('&',
                                    JsonUtil.getData("CombatTag.cannotPerformAction").toString());
                            if (commandsDisabled.contains(cmdEntry.getKey()) && cmdEntry.getKey().equalsIgnoreCase(commandName)) {//check actual cmd name
                                event.setCancelled(true);
                                player.sendMessage(s);
                                return;
                            }
                            final List<String> aliases = (List<String>) cmdEntry.getValue().get("aliases");
                            if(aliases != null) {//check aliase names
                                String finalCommandName = commandName.toLowerCase();
                                if (aliases.stream().map(String::toLowerCase).anyMatch(i -> i.equalsIgnoreCase(finalCommandName)) &&
                                        commandsDisabled.contains(cmdEntry.getKey())) {
                                    event.setCancelled(true);
                                    player.sendMessage(s);
                                    return;
                                }
                            }

                        }
                    }
                }

            }

        }

    }


    public void setXp(float xp) {
        this.xp = xp;
    }

    public void setpLevel(int pLevel) {
        this.pLevel = pLevel;
    }

    public float getXp() {
        return xp;
    }

    public int getpLevel() {
        return pLevel;
    }

    public CombatTimer getTimer() {
        return timer;
    }

    public Player getPlayer() {
        return player;
    }


}
