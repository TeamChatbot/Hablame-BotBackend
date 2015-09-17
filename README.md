# Hablame-BotBackend

### What this repository is about
Within this repo you can find a Java EE Application which uses the _deprecated_ <a href="https://code.google.com/p/program-ab/" title="Program AB is the reference implementation of the AIML 2.0 draft specification">Program AB</a> to achieve a conversation service with the possibility of extensions (like getting weather informations).
It uses

1. <a href="https://maven.apache.org/" title="Maven is a build automation tool used primarily for Java projects">Maven</a> as software project management and comprehension tool
2. <a href="http://spring.io/" title="The Spring Framework is an application framework and inversion of control container for the Java platform">Spring Framework</a> for dependency injection, transaction management, web applications, data access
3. <a href="http://junit.org/" title="A programmer-oriented testing framework for Java">JUnit</a> for testing


### Structure of this repository
Sourcecode:
 - Backend: [src/main/java/de/fhws/hablame/chatbotbackend](https://github.com/TeamChatbot/Hablame-BotBackend/tree/master/src/main/java/de/fhws/hablame/chatbotbackend)
 - WebApp: [WebContent/WEB-INF](https://github.com/TeamChatbot/Hablame-BotBackend/tree/master/WebContent/WEB-INF)
 - Testing: [src/test/java/de/fhws/hablame/chatbotbackend](https://github.com/TeamChatbot/Hablame-BotBackend/tree/master/src/test/java/de/fhws/hablame/chatbotbackend)
 
Documentation:
 - TODO: create javadocs

### Used libraries
All used dependencies can be found within the pom.xml

### Usage example
<table>
    <tr>
        <td>
          http://194.95.221.229:8080/Hablame-BotBackend/conversation<br>
          POST-Body: WIE IST DAS WETTER IN WUERZBURG
        </td>
        <td>
          Und die Ausgabe wäre:<br>
          "Es hat 18 Grad und maessiger Regen wird gemeldet"
        </td>
    </tr>
    <tr>
     <td>
       http://194.95.221.229:8080/Hablame-BotBackend/stopconversation
       (GET-Request)
     </td>
     <td>
       Und die Ausgabe wäre:<br>
       "chatbot stopped" (Falls die Instanz lief)
       "chatbot could not be stopped" (Falls die Instanz bereits null war)
     </td>
    </tr>
</table>
