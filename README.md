What
=====
This project offers a Java API to control mplayer and omxplayer. It was tested only on linux. 

How
=====
    <dependency>
      <groupId>io.neocdtv</groupId>
      <artifactId>player-core</artifactId>
      <version>0.1</version>
    </dependency>

  
  
public static void main(String[] args) {
    final Player omxplayer = PlayerFactory.create("omxplayer");
}
