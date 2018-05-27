package io.neocdtv.player.core;

import org.junit.Test;

import java.net.URL;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

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
    assertThat("1.00", equalTo(duration));
  }

  @Test
  public void getDuration_no_file() {
    assertThat(null, equalTo(MediaInfo.getDuration("/no/file.mp3")));
  }
}