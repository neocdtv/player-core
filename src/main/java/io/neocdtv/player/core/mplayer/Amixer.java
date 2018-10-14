package io.neocdtv.player.core.mplayer;

import io.neocdtv.player.core.LoggerUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Amixer {

  private final static Logger LOGGER = Logger.getLogger(Amixer.class.getName());
  private String channelName;
  private final List<String> cmd;

  public Amixer(String channelName) {
    this.channelName = channelName;
    this.cmd = Arrays.asList("amixer", "sset", channelName, "--");
  }

  public void setVolume(int millibels) throws IOException {
    ArrayList<String> cmdCopy = new ArrayList<String>(cmd);
    cmdCopy.add(String.valueOf(millibels));

    LoggerUtil.printCommand(LOGGER, cmdCopy);

    ProcessBuilder processBuilder = new ProcessBuilder(cmdCopy);
    Process process = processBuilder.start();
  }

}
