package com.sundayCinema.sundayCinema.movie.repository.movieMediaRepo;

import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.Movie;
import com.sundayCinema.sundayCinema.movie.entity.movieMediaInfo.StillCut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StillCutRepository extends JpaRepository<StillCut, Long> {
    @Query("SELECT MAX(e.stillCutId) FROM StillCut e")
    Long findMaxStillCutId();
    boolean existsByMovie(Movie movie);
}
