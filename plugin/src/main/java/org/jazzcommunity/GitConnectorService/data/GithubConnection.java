package org.jazzcommunity.GitConnectorService.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ibm.team.repository.service.TeamRawService;
import org.jazzcommunity.GitConnectorService.net.ArtifactInformation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;


public class GithubConnection {
    private static Gson gson = new GsonBuilder().create();

    // TODO: change gitlab version so same parameters
    public static JsonObject getArtifact(ArtifactInformation info, TeamRawService parentService) throws IOException {
        URLConnection connection = info.getApi().openConnection();
        String token = TokenHelper.getToken(info.getWeb(), parentService);
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Authorization", "token " + token);

        try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
            return gson.fromJson(reader, JsonObject.class);
        }
    }
}
