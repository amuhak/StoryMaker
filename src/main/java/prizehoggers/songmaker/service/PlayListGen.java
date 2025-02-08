package prizehoggers.songmaker.service;

import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Configuration
@EnableScheduling
public class PlayListGen {
    public static final Logger logger = org.slf4j.LoggerFactory.getLogger(PlayListGen.class);
    private static final String clientId = "18dfc59b36ed422f8dec248b989f7954";
    private static final String clientSecret = "e00c33df613a4543b9c03b81b21c047d";
    private static final SpotifyApi spotifyApi =
            new SpotifyApi.Builder().setClientId(clientId).setClientSecret(clientSecret).build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();


    public static List<PlaylistSimplified> getPlayList(String genre) {
        List<PlaylistSimplified> ans = new ArrayList<>();
        try {
            ans = Arrays.stream(spotifyApi.searchPlaylists(genre).build().execute().getItems())
                    .filter(Objects::nonNull)
                    .toList();
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
        }
        return ans;
        /*
        for (var playlist : ans) {
            System.out.println(Arrays.toString(playlist.getImages()));
            System.out.println("Playlist Name: " + playlist.getName());
            System.out.println("Playlist ID: " + playlist.getId());
            System.out.println("Playlist URL: " + playlist.getExternalUrls().get("spotify"));
            System.out.println("---");
        }
*/
    }

    @Scheduled(fixedRate = 1000 * 60 * 50) // 50 minutes
    public static void clientCredentials_Async() {
        logger.info("Refreshing client credentials");
        try {
            final CompletableFuture<ClientCredentials> clientCredentialsFuture =
                    clientCredentialsRequest.executeAsync();
            final ClientCredentials clientCredentials = clientCredentialsFuture.join();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            logger.info("Expires in: {}", clientCredentials.getExpiresIn());
        } catch (CompletionException e) {
            logger.info("Error: {}", e.getCause().getMessage());
        } catch (CancellationException e) {
            logger.info("Async operation cancelled.");
        }
    }
}
