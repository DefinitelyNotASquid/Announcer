package com.mith.Announcer;

import java.util.Random;

public class announcerThread
  extends Thread {
  private final Random randomGenerator = new Random();
  private final announcer plugin;
  private int lastAnnouncement = 0;
  
  public announcerThread(announcer plugin) {
    this.plugin = plugin;
  }


  
  public void run() {
    if (this.plugin.isAnnouncerEnabled()) {
      if (this.plugin.isRandom()) {
        this.lastAnnouncement = Math.abs(this.randomGenerator.nextInt() % this.plugin.numberOfAnnouncements());
      }
      else if (++this.lastAnnouncement >= this.plugin.numberOfAnnouncements()) {
        this.lastAnnouncement = 0;
      } 
      
      if (this.lastAnnouncement < this.plugin.numberOfAnnouncements())
        this.plugin.announce(this.lastAnnouncement + 1); 
    } 
  }
}