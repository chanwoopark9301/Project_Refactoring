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
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long actorId;
    @Column
    private String peopleNm;
    @Column
    private String peopleNmEn;
    @Column(name = "casting")
    private String cast;
    @Column
    private String castEn;
    @ManyToOne   //
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;
}
