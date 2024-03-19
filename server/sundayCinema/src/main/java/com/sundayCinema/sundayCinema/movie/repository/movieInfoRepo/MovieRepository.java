package com.sundayCinema.sundayCinema.movie.repository.movieInfoRepo;

import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.DetailMovie;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    DetailMovie findByMovieId(Long movieId);
    @Query("select m from Movie m where m.movieId = :movieId")
    Movie findMovieByMovieId(Long movieId);
    Movie findByMovieCd(String movieCd);
    Movie findByMovieNm(String movieNm);

    List<Movie> findByNationsNationNm(String nationName);
    List<Movie> findByNationsNationNmIsNot(String nationName);
    default boolean existsByMovieCd(String movieCd) {
        return findByMovieCd(movieCd) != null;
    }



}
