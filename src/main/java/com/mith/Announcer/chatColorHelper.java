package com.mith.Announcer;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.md_5.bungee.api.ChatColor.COLOR_CHAR;


public class chatColorHelper
{
  public static String colorize(String message){

    message = ChatColor.translateAlternateColorCodes('&', message);

    final Pattern hexPattern = Pattern.compile("&#" + "([A-Fa-f0-9]{6})");
    Matcher matcher = hexPattern.matcher(message);
    StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
    while (matcher.find())
    {
      String group = matcher.group(1);
      matcher.appendReplacement(buffer, COLOR_CHAR + "x"
              + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
              + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
              + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
      );
    }
    return matcher.appendTail(buffer).toString();
  }
}