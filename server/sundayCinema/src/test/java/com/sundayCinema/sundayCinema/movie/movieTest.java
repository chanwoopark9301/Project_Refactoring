package com.sundayCinema.sundayCinema.movie;

import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.DetailMovie;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.Movie;
import com.sundayCinema.sundayCinema.movie.repository.movieInfoRepo.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xmlunit.diff.Comparison;

@SpringBootTest
public class movieTest {
    @Autowired
    private MovieRepository movieRepository;

    @Test
    void findMovieTest(){
        DetailMovie findMovie = movieRepository.findByMovieId(1L);
    }
    @Test
    void MovieTest(){
        Movie findMovie = movieRepository.findMovieByMovieId(1L);
    }
}
