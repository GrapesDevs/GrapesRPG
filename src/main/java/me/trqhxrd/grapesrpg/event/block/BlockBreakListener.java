package me.trqhxrd.grapesrpg.event.block;

import me.trqhxrd.grapesrpg.api.GrapesPlayer;
import me.trqhxrd.grapesrpg.api.attribute.Register;
import me.trqhxrd.grapesrpg.api.objects.block.GrapesBlock;
import me.trqhxrd.grapesrpg.api.objects.block.GrapesBlockType;
import me.trqhxrd.grapesrpg.api.objects.item.GrapesItem;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * This BlockBreakListener will reduce the durability of item if you break a block with it.
 * If the durability is 0 the block-breaking will be cancelled.
 *
 * @author Trqhxrd
 */
@Register
public class BlockBreakListener implements Listener {

    /**
     * The Handler-Method
     *
     * @param e A BlockBreakEvent.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {
        boolean cancel = false;
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            GrapesItem i = GrapesItem.fromItemStack(e.getPlayer().getInventory().getItemInMainHand());
            if (i != null) {
                int durability = i.getCurrentDurability();
                if (!i.isUnbreakable() && durability == 0) {
                    GrapesPlayer p = GrapesPlayer.getByUniqueId(e.getPlayer().getUniqueId());
                    p.getWrappedObject().getInventory().setItemInMainHand(i.build());
                    p.sendMessage("&c&lIt seems like your tool is broken...");
                    cancel = true;
                } else {
                    i.setDurability(durability - 1, i.getMaxDurability());
                    e.getPlayer().getInventory().setItemInMainHand(i.build());
                }
            }
        }
        GrapesBlock block = GrapesBlock.getBlock(e.getBlock().getLocation());
        if (!cancel && block.getType() != GrapesBlockType.UNDEFINED) block.destroy();

        e.setCancelled(cancel);

    }
}
