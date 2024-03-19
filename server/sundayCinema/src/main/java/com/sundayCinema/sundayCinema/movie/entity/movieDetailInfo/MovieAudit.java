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
@Table(name = "MOVIE_AUDIT")
public class MovieAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long auditId;
    @Column
    private String auditNo;
    @Column
    private String watchGradeNm;
    @ManyToOne(fetch = FetchType.LAZY)   //
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;
}
