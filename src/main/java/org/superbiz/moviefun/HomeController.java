package org.superbiz.moviefun;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.superbiz.moviefun.albums.Album;
import org.superbiz.moviefun.albums.AlbumFixtures;
import org.superbiz.moviefun.albums.AlbumsBean;
import org.superbiz.moviefun.movies.Movie;
import org.superbiz.moviefun.movies.MovieFixtures;
import org.superbiz.moviefun.movies.MoviesBean;

import java.util.Map;

@Controller
public class HomeController {

    private final MoviesBean moviesBean;
    private final AlbumsBean albumsBean;
    private final MovieFixtures movieFixtures;
    private final AlbumFixtures albumFixtures;
    private final PlatformTransactionManager moviesPlatformTransactionManager;
    private final PlatformTransactionManager albumsPlatformTransactionManager;

    public HomeController(MoviesBean moviesBean, AlbumsBean albumsBean, MovieFixtures movieFixtures,
                          AlbumFixtures albumFixtures, PlatformTransactionManager moviesPlatformTransactionManager,
                          PlatformTransactionManager albumsPlatformTransactionManager) {
        this.moviesBean = moviesBean;
        this.albumsBean = albumsBean;
        this.movieFixtures = movieFixtures;
        this.albumFixtures = albumFixtures;
        this.moviesPlatformTransactionManager = moviesPlatformTransactionManager;
        this.albumsPlatformTransactionManager = albumsPlatformTransactionManager;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {
        for (Movie movie : movieFixtures.load()) {
            createMovie(movie);
        }

        for (Album album : albumFixtures.load()) {
            createAlbums(album);
        }

        model.put("movies", moviesBean.getMovies());
        model.put("albums", albumsBean.getAlbums());

        return "setup";
    }

    private void createAlbums(Album album) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(albumsPlatformTransactionManager);
        transactionTemplate.execute(transactionStatus -> {
            albumsBean.addAlbum(album);
            return null;
       });

    }

    private void createMovie(Movie movie) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(moviesPlatformTransactionManager);
        transactionTemplate.execute(transactionStatus -> {
            moviesBean.addMovie(movie);
            return null;
        });
    }
}
