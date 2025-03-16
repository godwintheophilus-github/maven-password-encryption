package com.example;

import org.apache.maven.settings.Settings;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.building.DefaultSettingsBuilder;
import org.apache.maven.settings.building.DefaultSettingsBuilderFactory;
import org.apache.maven.settings.building.SettingsBuildingException;
import org.apache.maven.settings.building.SettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuildingResult;
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;
import org.sonatype.plexus.components.cipher.DefaultPlexusCipher;

import java.io.File;

public class App2 {

    public static void main(String[] args) throws Exception {
        // Load settings.xml
        Settings settings = loadMavenSettings();

        // Lookup encrypted password based on server id
        String serverId = "myserverid"; // Change this to your actual server id
        Server server = settings.getServer(serverId);
        if (server == null) {
            System.out.println("No server found with id: " + serverId);
            return;
        }

        String encryptedPassword = server.getPassword();
        if (encryptedPassword == null) {
            System.out.println("No password found for server id: " + serverId);
            return;
        }

        // Decrypt password
        String decryptedPassword = decryptPassword(encryptedPassword);

        System.out.println("Decrypted password: " + decryptedPassword);
    }

    private static Settings loadMavenSettings() throws SettingsBuildingException {
        DefaultSettingsBuilder builder = new DefaultSettingsBuilderFactory().newInstance();
        SettingsBuildingRequest request = new DefaultSettingsBuildingRequest();
        String settingsPath = System.getProperty("user.home") + "/.m2/settings.xml";
        request.setUserSettingsFile(new File(settingsPath));
        SettingsBuildingResult result = builder.build(request);
        return result.getEffectiveSettings();
    }

    private static String decryptPassword(String encryptedPassword) throws Exception {
        DefaultPlexusCipher cipher = new DefaultPlexusCipher();
        System.setProperty("settings.security", System.getProperty("user.home") + "/.m2/settings-security.xml");

        SecDispatcher secDispatcher = new DefaultSecDispatcher(cipher);
        return secDispatcher.decrypt(encryptedPassword);
    }
}
