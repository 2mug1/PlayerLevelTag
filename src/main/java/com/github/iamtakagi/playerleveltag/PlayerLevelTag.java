package com.github.iamtakagi.playerleveltag;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.nametagedit.plugin.NametagEdit;

public class PlayerLevelTag extends JavaPlugin {

  private Config config;

  @Override
  public void onEnable() {
    if (!getServer().getPluginManager().isPluginEnabled("NametagEdit")) {
      getLogger().severe("NametagEdit is not enabled or installed, Disabling...");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }
    this.saveDefaultConfig();
    this.config = new Config(this.getConfig());
    this.getServer().getPluginManager().registerEvents(new ListenerImpl(), this);
  }

  enum Position {
    PREFIX, SUFFIX
  }

  class Config {
    private Position position;
    private String format;
    private boolean displayNameEnabled;

    public Config(FileConfiguration fileConfiguration) {
      this.position = Position.valueOf(fileConfiguration.getString("tag.position", "PREFIX").toUpperCase());
      this.format = fileConfiguration.getString("tag.format", "&7[&a%d&7] &r");
      this.displayNameEnabled = fileConfiguration.getBoolean("tag.display_name_enabled", true);
    }

    public Position getPosition() {
      return position;
    }

    public String getFormat() {
      return format;
    }

    public boolean isDisplayNameEnabled() {
      return displayNameEnabled;
    }
  }

  class ListenerImpl implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
      setLevelTag(event);
    }

    @EventHandler
    public void onLevelChange(PlayerLevelChangeEvent event) {
      setLevelTag(event);
    }

    private void setLevelTag(PlayerEvent event) {
      Player player = event.getPlayer();
      if (player == null) {
        return;
      }
      if (PlayerLevelTag.this.config.position == null || PlayerLevelTag.this.config.format == null) {
        return;
      }
      switch (PlayerLevelTag.this.config.position) {
        case PREFIX:
          new BukkitRunnable() {
            @Override
            public void run() {
              NametagEdit.getApi().setPrefix(player, String.format(PlayerLevelTag.this.config.format, player.getLevel()));
              if (PlayerLevelTag.this.config.displayNameEnabled) {
                player.setDisplayName(NametagEdit.getApi().getNametag(player).getPrefix() + player.getName() + NametagEdit.getApi().getNametag(player).getSuffix());
              }
            }
          }.runTaskLater(PlayerLevelTag.this, 1);
          break;
        case SUFFIX:
          new BukkitRunnable() {
            @Override
            public void run() {
              NametagEdit.getApi().setSuffix(player, String.format(PlayerLevelTag.this.config.format, player.getLevel()));
              if (PlayerLevelTag.this.config.displayNameEnabled) {
                player.setDisplayName(NametagEdit.getApi().getNametag(player).getPrefix() + player.getName() + NametagEdit.getApi().getNametag(player).getSuffix());
              }
            }
          }.runTaskLater(PlayerLevelTag.this, 1);
          break;
        default:
          break;
      }
    }
  }
}
