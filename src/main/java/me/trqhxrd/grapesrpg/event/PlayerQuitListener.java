package me.trqhxrd.grapesrpg.event;

import me.trqhxrd.grapesrpg.Grapes;
import me.trqhxrd.grapesrpg.api.common.GrapesPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Trqhxrd
 * This Class handles players, who are quitting using a {@see PlayerQuitEvent}.
 */
public class PlayerQuitListener implements Listener {

    /**
     * This Constructor registers the Listener to the Plugin.
     * WARNING: Only execute one time.
     */
    public PlayerQuitListener() {
        Bukkit.getPluginManager().registerEvents(this, Grapes.getGrapes());
    }

    /**
     * This Method will be executed, everytime, the a player quits.
     *
     * @param e The {@see PlayerQuitEvent} with all important data about the Player.
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (GrapesPlayer.exists(e.getPlayer().getUniqueId())) {
            GrapesPlayer p = GrapesPlayer.getByUniqueId(e.getPlayer().getUniqueId());
            //PLAYER DESTROY:
            GrapesPlayer.getPlayers().remove(p);
        }
    }
}