package com.sundayCinema.sundayCinema.movie.entity.movieDetailInfo;

import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Nation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long nationId;
    @Column
    private String nationNm;

    @ManyToOne(fetch = FetchType.LAZY)   //
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;
}
