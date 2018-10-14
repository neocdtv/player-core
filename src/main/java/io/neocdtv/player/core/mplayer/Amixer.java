package io.neocdtv.player.core.mplayer;

import io.neocdtv.player.core.LoggerUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Amixer {

  private final static Logger LOGGER = Logger.getLogger(Amixer.class.getName());
  private String channelName;
  private final List<String> cmd;

  public Amixer(String channelName) {
    this.channelName = channelName;
    this.cmd = Arrays.asList("amixer", "sset", channelName, "--");
  }

  public void setVolume(int millibels) {
    ArrayList<String> cmdCopy = new ArrayList<String>(cmd);
    cmdCopy.add(String.valueOf(millibels));

    LoggerUtil.printCommand(LOGGER, cmdCopy);

    ProcessBuilder processBuilder = new ProcessBuilder(cmdCopy);

    try {
      Process process = processBuilder.start();
    } catch (IOException exception) {
      LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
    }
  }

}
