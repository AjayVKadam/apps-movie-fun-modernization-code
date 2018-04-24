package org.superbiz.moviefun;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.superbiz.moviefun.albumsapi.AlbumFixtures;
import org.superbiz.moviefun.albumsapi.AlbumInfo;
import org.superbiz.moviefun.albumsapi.AlbumsClient;
import org.superbiz.moviefun.moviesapi.MovieFixtures;
import org.superbiz.moviefun.moviesapi.MovieInfo;
import org.superbiz.moviefun.moviesapi.MoviesClient;

import java.util.Map;

@Controller
public class SetupController {

    private final MoviesClient moviesClient;
    private final AlbumsClient albumsClient;

    private final MovieFixtures movieFixtures;
    private final AlbumFixtures albumFixtures;

    public SetupController(MoviesClient moviesClient, MovieFixtures movieFixtures
                           ,AlbumsClient albumsClient, AlbumFixtures albumFixtures) {
        this.moviesClient = moviesClient;
        this.movieFixtures = movieFixtures;

        this.albumsClient = albumsClient;
        this.albumFixtures = albumFixtures;

    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {
        for (MovieInfo movie : movieFixtures.load()) {
            moviesClient.addMovie(movie);
        }

        for (AlbumInfo album : albumFixtures.load()) {
            albumsClient.addAlbum(album);
        }
        model.put("movies", moviesClient.getMovies());
        model.put("albums", albumsClient.getAlbums());

        return "setup";
    }
}