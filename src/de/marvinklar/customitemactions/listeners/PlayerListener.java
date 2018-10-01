package de.marvinklar.customitemactions.listeners;

import de.marvinklar.customitemactions.CustomItemAction;
import de.marvinklar.customitemactions.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(e.getItem() != null) {
                if(p.hasPermission(Main.getMessageManager().getMessage("Use_Permission"))) {
                    for(CustomItemAction customItemAction : Main.customItemActions.values()) {
                        if(customItemAction.item.equals(e.getItem())) {
                            e.setCancelled(true);
                            removeItem(p, customItemAction.item);
                            p.updateInventory();

                            for(String command : customItemAction.commands) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.substring(1).replace("%player%", p.getName()));
                            }
                            for(String broadcast : customItemAction.broadcasts) {
                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcast.replace("%player%", p.getName())));
                            }
                            for(String message : customItemAction.messages) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%player%", p.getName())));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void removeItem(final Player p, final ItemStack is)
    {
        for (int i = 0; i < p.getInventory().getSize(); i++)
        {
            if (p.getInventory().getItem(i) != null)
            {
                if (p.getInventory().getItem(i).isSimilar(is))
                {
                    if (p.getInventory().getItem(i).getAmount() == 1)
                    {
                        p.getInventory().setItem(i, new ItemStack(Material.AIR));
                        p.updateInventory();
                        return;
                    }
                    p.getInventory().getItem(i).setAmount(p.getInventory().getItem(i).getAmount() - 1);
                    p.updateInventory();
                    return;
                }
            }
        }
    }
}
