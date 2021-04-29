package com.mith.Announcer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class announcer
  extends JavaPlugin implements Listener {
  protected List<String> announcementMessages;
  protected String announcementPrefix;
  protected long announcementInterval;
  protected boolean enabled;
  protected boolean random;
  private announcerThread announcerThread;
  private Logger logger;
  public List<Player> arrayOfPlayer = new ArrayList<>();

  
  public announcer() {
    this.announcerThread = new announcerThread(this);
  }

  
  public void onEnable() {

    this.logger = getServer().getLogger();
    if (!(new File(getDataFolder(), "config.yml")).exists()) {
      saveDefaultConfig();
    }
    reloadConfiguration();
    BukkitScheduler scheduler = getServer().getScheduler();
    scheduler.scheduleSyncRepeatingTask(this, this.announcerThread, this.announcementInterval * 20L, this.announcementInterval * 20L);
    announcerCommandExecutor announcerCommandExecutor = new announcerCommandExecutor(this);
    getCommand("announce").setExecutor(announcerCommandExecutor);
    getCommand("announcer").setExecutor(announcerCommandExecutor);
    getCommand("acc").setExecutor(announcerCommandExecutor);
    
    getServer().getPluginManager().registerEvents(this, this);
    this.logger.info(String.format("%s is enabled!\n", new Object[] { getDescription().getFullName() }));
  }

  
  public void onDisable() {
    this.logger.info(String.format("%s is disabled!\n", new Object[] { getDescription().getFullName() }));
  }

  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    this.arrayOfPlayer.add(player);
  }

  
  public void announce() {
    this.announcerThread.run();
  }

  
  public void announce(int index) {
    announce(this.announcementMessages.get(index - 1));
  }


  
  public void announce(String line) {
    String[] messages = line.split("&n");
    String[] arrayOfString1;
    int j = (arrayOfString1 = messages).length;
    for (int i = 0; i < j; i++) {
      
      String message = arrayOfString1[i];
      if (message.startsWith("/")) {
        
        getServer().dispatchCommand(getServer().getConsoleSender(), message.substring(1));
      }
      else if (getServer().getOnlinePlayers().size() > 0) {
        
        String messageToSend = chatColorHelper.colorize(String.format("%s%s", this.announcementPrefix, message));
        int m = this.arrayOfPlayer.size();
        if (!this.arrayOfPlayer.isEmpty()) {
          for (int k = 0; k < m; k++) {
            
            Player player = this.arrayOfPlayer.get(k);
            if (player.hasPermission("announcer.receiver")) {
              if (!messageToSend.isEmpty()) {
                player.sendMessage(messageToSend);
              }
            } 
          } 
        }
      } 
    } 
  }

  
  public void saveConfiguration() {
    getConfig().set("announcement.messages", this.announcementMessages);
    getConfig().set("announcement.interval", Long.valueOf(this.announcementInterval));
    getConfig().set("announcement.prefix", this.announcementPrefix);
    getConfig().set("announcement.enabled", Boolean.valueOf(this.enabled));
    getConfig().set("announcement.random", Boolean.valueOf(this.random));
    saveConfig();
  }

  
  public void reloadConfiguration() {
    reloadConfig();
    this.announcementPrefix = getConfig().getString("announcement.prefix", "&c[Announcement] ");
    this.announcementMessages = getConfig().getStringList("announcement.messages");
    
    this.announcementInterval = getConfig().getInt("announcement.interval", 1000);
    this.enabled = getConfig().getBoolean("announcement.enabled", true);
    this.random = getConfig().getBoolean("announcement.random", false);
  }

  
  public String getAnnouncementPrefix() {
    return this.announcementPrefix;
  }

  
  public void setAnnouncementPrefix(String announcementPrefix) {
    this.announcementPrefix = announcementPrefix;
    saveConfig();
  }

  
  public long getAnnouncementInterval() {
    return this.announcementInterval;
  }

  
  public void setAnnouncementInterval(long announcementInterval) {
    this.announcementInterval = announcementInterval;
    saveConfiguration();
    
    BukkitScheduler scheduler = getServer().getScheduler();
    scheduler.cancelTasks(this);
    scheduler
      .scheduleSyncRepeatingTask(this, this.announcerThread, announcementInterval * 20L, announcementInterval * 20L);
  }

  
  public void addAnnouncement(String message) {
    this.announcementMessages.add(message);
    saveConfiguration();
  }

  
  public String getAnnouncement(int index) {
    return this.announcementMessages.get(index - 1);
  }

  
  public int numberOfAnnouncements() {
    return this.announcementMessages.size();
  }

  
  public void removeAnnouncements() {
    this.announcementMessages.clear();
    saveConfiguration();
  }

  
  public void removeAnnouncement(int index) {
    this.announcementMessages.remove(index - 1);
    saveConfiguration();
  }

  
  public boolean isAnnouncerEnabled() {
    return this.enabled;
  }

  
  public void setAnnouncerEnabled(boolean enabled) {
    this.enabled = enabled;
    saveConfiguration();
  }

  
  public boolean isRandom() {
    return this.random;
  }

  
  public void setRandom(boolean random) {
    this.random = random;
    saveConfiguration();
  }

  @EventHandler
  public void onLeave(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    this.arrayOfPlayer.remove(player);
  }
  
  @EventHandler
  public void onLeave(PlayerKickEvent event) {
    Player player = event.getPlayer();
    this.arrayOfPlayer.remove(player);
  }
}