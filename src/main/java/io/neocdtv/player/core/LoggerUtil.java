package io.neocdtv.player.core;

import java.util.List;
import java.util.logging.Logger;

public class LoggerUtil {

  public static void printCommand(Logger logger, final List<String> cmd) {
    final StringBuffer stringBuffer = new StringBuffer();
    cmd.stream().forEach(o -> stringBuffer.append(o).append(" "));
    logger.info("Executing command: " + stringBuffer.toString());
  }
}
