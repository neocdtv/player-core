package io.neocdtv.player.core;

import io.neocdtv.player.core.mplayer.MPlayer;
import io.neocdtv.player.core.omxplayer.OmxPlayer;

/**
 * @author xix
 */
public class PlayerFactory {

  public static Player getPlayer(String player) {

    if ("omxplayer".equals(player)) {
      return new OmxPlayer();
    } else {
      return new MPlayer();
    }
  }
}
