package org.jazzcommunity.GitConnectorService.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ibm.team.repository.service.TeamRawService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GitlabConnection {
    private static Gson gson = new GsonBuilder().create();

    public static JsonObject getArtifact(URL url, TeamRawService parentService) throws IOException {
        URLConnection connection = url.openConnection();
        String token = TokenHelper.getToken(url, parentService);
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Private-Token", token);

        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            return gson.fromJson(reader, JsonObject.class);
        }
    }
}
