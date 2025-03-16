package com.example;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;
import org.sonatype.plexus.components.cipher.PlexusCipher;
import org.sonatype.plexus.components.cipher.DefaultPlexusCipher;

public class App {

    public static void main(String[] args) throws Exception {
        PlexusCipher cipher = new DefaultPlexusCipher();

        String securitySettingsPath = System.getProperty("user.home") + "/.m2/settings-security.xml";
        System.setProperty("settings.security", securitySettingsPath);

        SecDispatcher secDispatcher = new DefaultSecDispatcher(cipher);

        String encryptedPassword = "{ZPBEXo0+xH0Hh2d15GF9WqP9RPt2AfrXcAKShVxPMsZuqTIZbyvoq1gIPelNplst}";
        String decryptedPassword = secDispatcher.decrypt(encryptedPassword);

        System.out.println("Decrypted password: " + decryptedPassword);
    }
}
