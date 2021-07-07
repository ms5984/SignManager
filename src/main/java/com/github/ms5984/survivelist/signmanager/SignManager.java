/*
 * MIT License
 *
 * Copyright (c) 2021 Matt (ms5984) <https://github.com/ms5984>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.ms5984.survivelist.signmanager;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class SignManager extends JavaPlugin implements AccessManager {

    private static SignManager instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getServer().getPluginManager().registerEvents(new SignListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean canColorSign(Player player) {
        return player.hasPermission("signmanager.color.withdyes");
    }

    public static AccessManager getAccessManager() {
        return instance;
    }

    class SignListener implements Listener {
        @EventHandler
        public void onPlayerDyeSign(PlayerInteractEvent event) {
            if (!event.hasBlock()) return;
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
            //noinspection ConstantConditions
            final Material type = event.getClickedBlock().getType();
            if (Tag.SIGNS.isTagged(type) || Tag.WALL_SIGNS.isTagged(type)) {
                // We have a sign, check for dye in hand
                final EquipmentSlot hand = event.getHand();
                if (hand == null) return;
                final ItemStack item = event.getPlayer().getInventory().getItem(hand);
                switch (item.getType()) {
                    case WHITE_DYE:
                    case ORANGE_DYE:
                    case MAGENTA_DYE:
                    case LIGHT_BLUE_DYE:
                    case YELLOW_DYE:
                    case LIME_DYE:
                    case PINK_DYE:
                    case GRAY_DYE:
                    case LIGHT_GRAY_DYE:
                    case CYAN_DYE:
                    case PURPLE_DYE:
                    case BLUE_DYE:
                    case BROWN_DYE:
                    case GREEN_DYE:
                    case RED_DYE:
                    case BLACK_DYE:
                        if (!canColorSign(event.getPlayer())) {
                            event.setCancelled(true);
                        }
                        return;
                    default:
                        break;
                }
            }
        }
    }
}
