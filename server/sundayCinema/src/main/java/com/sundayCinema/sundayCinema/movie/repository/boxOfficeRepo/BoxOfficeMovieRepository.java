package com.sundayCinema.sundayCinema.movie.repository.boxOfficeRepo;

import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.BoxOfficeMovie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoxOfficeMovieRepository extends JpaRepository<BoxOfficeMovie, Long>  {
    void deleteAll();
    BoxOfficeMovie findByMovieCd(String MovieCd);
}
