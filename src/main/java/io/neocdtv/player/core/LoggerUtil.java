package io.neocdtv.player.core;

import java.util.ArrayList;
import java.util.logging.Logger;

public class LoggerUtil {

  public static void printCommand(Logger logger, final ArrayList<String> cmdCopy) {
    final StringBuffer stringBuffer = new StringBuffer();
    cmdCopy.stream().forEach(o -> stringBuffer.append(o).append(" "));
    logger.info("Executing command: " + stringBuffer.toString());
  }
}
