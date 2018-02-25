package io.neocdtv.player.core;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

/**
 * MediaInfoTest.
 *
 * @author xix
 * @since 25.02.18
 */
public class MediaInfoTest {

  @Test
  public void getDuration() {
    // given
    URL resource = Thread.currentThread().getContextClassLoader().getResource("audio/1.mp3");
    // when
    String duration = MediaInfo.getDuration(resource.getPath());
    // then
    Assert.assertThat("7.00", CoreMatchers.equalTo(duration));
  }
}