package com.sundayCinema.sundayCinema.movie.entity.movieMainInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sundayCinema.sundayCinema.advice.audit.Auditable;
import com.sundayCinema.sundayCinema.comment.Comment;
import com.sundayCinema.sundayCinema.movie.entity.movieDetailInfo.*;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.BoxOfficeMovie;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.ForeignBoxOffice;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.GenreBoxOffice;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.KoreaBoxOffice;
import com.sundayCinema.sundayCinema.movie.entity.movieMediaInfo.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId; // 기본
    @Column(nullable = false)
    private String movieCd;
    @Column(nullable = false)
    private String movieNm;// "영화명(국문)" 기본
    @Column(nullable = false)
    private String movieNmEn;// "영화명(영문)" 디테일
    @Column(nullable = false)
    private String movieNmOg;// ""
    @Column(nullable = false)
    private String showTm;// "상영시간" 디테일
    @Column(nullable = false)
    private String openDt; // "개봉일" 디테일


    @OneToOne(mappedBy = "movie", fetch = FetchType.LAZY)
    private BoxOfficeMovie boxOfficeMovie;
    @OneToOne(mappedBy = "movie", fetch = FetchType.LAZY)
    private ForeignBoxOffice foreignBoxOffice;
    @OneToOne(mappedBy = "movie", fetch = FetchType.LAZY)
    private KoreaBoxOffice koreaBoxOfficeMovie;
    @OneToOne(mappedBy = "movie", fetch = FetchType.LAZY)
    private GenreBoxOffice genreBoxOfficeMovie;
    @OneToOne(mappedBy = "movie", fetch = FetchType.LAZY)
    private Plot plot;
    @OneToOne(mappedBy = "movie", fetch = FetchType.LAZY)
    private Poster poster;
    @OneToOne(mappedBy = "movie", fetch = FetchType.LAZY)
    private BackDrop backDrop;


    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<MovieAudit> audits; // "관람 등급 명칭"
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Nation> nations = new ArrayList<>(); //: "국가"
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Actor> actors = new ArrayList<>();
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Genre> genres = new ArrayList<>();
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Director> directors = new ArrayList<>();
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<YoutubeReview> youtubeReviews = new ArrayList<>();
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<StillCut> stillCuts = new ArrayList<>();
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Trailer> trailers = new ArrayList<>();
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
}
