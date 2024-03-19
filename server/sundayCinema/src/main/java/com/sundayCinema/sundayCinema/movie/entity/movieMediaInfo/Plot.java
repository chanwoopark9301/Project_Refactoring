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
public class Plot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long plotId;

    @Column(columnDefinition = "text")
    private String plotText;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;
}
