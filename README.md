What
=====
This project offers a Java API to control mplayer and omxplayer. It was tested only on linux. 

How
=====
Maven
======
    <dependency>
      <groupId>io.neocdtv</groupId>
      <artifactId>player-core</artifactId>
      <version>0.1</version>
    </dependency>
    
Java
======
    public static void main(String[] args) {
        final Player player = PlayerFactory.getPlayer("omxplayer");
        // final Player player = PlayerFactory.getPlayer("mplayer");
        final String url = "http://somedomain.eu/video.mp4";
        player.play(url);
    }
