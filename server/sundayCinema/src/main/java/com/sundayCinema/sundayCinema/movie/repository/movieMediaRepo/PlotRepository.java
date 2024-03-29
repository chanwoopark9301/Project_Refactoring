package com.sundayCinema.sundayCinema.movie.repository.movieMediaRepo;

import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.Movie;
import com.sundayCinema.sundayCinema.movie.entity.movieMediaInfo.Plot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlotRepository extends JpaRepository<Plot, Long> {
    @Query("SELECT MAX(e.plotId) FROM Plot e")
    Long findMaxPlotId();
    boolean existsByMovie(Movie movie);
}
