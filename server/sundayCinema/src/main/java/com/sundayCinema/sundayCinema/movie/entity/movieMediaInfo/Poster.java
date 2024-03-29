package com.sundayCinema.sundayCinema.movie.entity.movieMediaInfo;

import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Poster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long posterId;
    @Column
    private String poster_image_url;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;
}
