# Stressor Tool

This tool was primarily designed to test websites. However, much of the code is either incomplete or not functioning as you will see. ( MC Method and Random Packets ig )

At the moment, I have limited interest in this project, but I may release some updates in the future. Because of this, I'm providing the full source code. However, I'm not entirely sure which version this is, so if you encounter any issues, feel free to report them! Thanks.

( Full Version in Releases )


please use it only for eductional things >.<
#happytobeaskid


# HTTP
java -jar --enable-preview (Name).jar -targets ip:port -hostname ip -method http -version 1.1 -requests 1024 -type get -processors 1 -duration 1 -connections 1


# MC METHOD
java -jar (Name).jar -targets ip:port -method mc -type login -version 1.8 -hostname ip -processors 1 -connections 1 -duration 10

# RANDOM PACKET
java -jar --enable-preview (Name).jar -targets ip:port -hostname ip -method random-packet -size <1 to 65535> -processors 1 -duration 1 -connections 1

# INSTALLATION
1. Download Java 21 ( https://www.oracle.com/de/java/technologies/downloads/#java21 )
2. Set java 21 as Standard Java Version ( setx JAVA_HOME "C:\Program Files\Java\jdk-21", setx PATH "%PATH%;C:\Program Files\Java\jdk-21\bin" )
3. Open CMD and run one of the Methods that i have pasted above.


If you need any Help you can add me on Discord: ack_cat
