package me.nanigans.pandoracombattag.Events;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Relation;
import me.nanigans.pandoracombattag.CombatTag.Combat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void playerHitPlayer(EntityDamageByEntityEvent event){

        final Entity entity = event.getEntity();
        final Entity damager = event.getDamager();
        if(damager instanceof Player && entity instanceof Player){
            final  Player player = ((Player) entity);
            final Player damager1 = (Player) damager;
            final FPlayer byPlayer = FPlayers.getInstance().getByPlayer(damager1);
            final FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
            final Relation relationTo = fPlayer.getRelationTo(byPlayer);
            if(relationTo.isEnemy() || relationTo.isNeutral()) {
                if (Combat.tagged.containsKey(player.getUniqueId())) {
                    Combat.tagged.get(player.getUniqueId()).getTimer().reset();
                } else {
                    new Combat(player).startCounting();
                }
            }
        }

    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        
    }

}