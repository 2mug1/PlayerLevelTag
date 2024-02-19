package com.github.iamtakagi.playerleveltag;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.nametagedit.plugin.NametagEdit;

public class PlayerLevelTag extends JavaPlugin {

  @Override
  public void onEnable() {
    this.getServer().getPluginManager().registerEvents(new Listener() {
      @EventHandler
      public void onJoin(PlayerJoinEvent event) {
        NametagEdit.getApi().setPrefix(event.getPlayer(), "&7[&a" + event.getPlayer().getLevel() + "&7] &r");
      }
  
      @EventHandler
      public void onLevelChange(PlayerLevelChangeEvent event) {
        NametagEdit.getApi().setPrefix(event.getPlayer(), "&7[&a" + event.getPlayer().getLevel() + "&7] &r");
      }
    }, this);
  }
}
