package com.sundayCinema.sundayCinema.movie.mapper;

import com.sundayCinema.sundayCinema.movie.dto.mainPageDto.BoxOfficeMovieDto;
import com.sundayCinema.sundayCinema.movie.dto.mainPageDto.GenreVo;
import com.sundayCinema.sundayCinema.movie.dto.detaiPagelDto.StillCutVo;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.BoxOfficeMovie;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.ForeignBoxOffice;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.KoreaBoxOffice;
import com.sundayCinema.sundayCinema.movie.entity.movieDetailInfo.Genre;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.Movie;
import com.sundayCinema.sundayCinema.movie.entity.movieMediaInfo.StillCut;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BoxOfficeMovieMapper {
    public BoxOfficeMovieDto boxOfficeResponseDto(BoxOfficeMovie boxOfficeMovie) {
        if (boxOfficeMovie == null) {
            return null;
        }
        return mapToDto(boxOfficeMovie.getMovie(), boxOfficeMovie.getRank(), boxOfficeMovie.getMovieNm());
    }

    public BoxOfficeMovieDto boxOfficeResponseDto(KoreaBoxOffice boxOfficeMovie) {
        if (boxOfficeMovie == null) {
            return null;
        }
        return mapToDto(boxOfficeMovie.getMovie(), boxOfficeMovie.getRank(), boxOfficeMovie.getMovieNm());
    }

    public BoxOfficeMovieDto boxOfficeResponseDto(ForeignBoxOffice boxOfficeMovie) {
        if (boxOfficeMovie == null) {
            return null;
        }
        return mapToDto(boxOfficeMovie.getMovie(), boxOfficeMovie.getRank(), boxOfficeMovie.getMovieNm());
    }

    private BoxOfficeMovieDto mapToDto(Movie movie, String rank, String movieNm) {
        // 공통 매핑 로직을 수행
        ArrayList<GenreVo> genreVos = mapGenres(movie.getGenres());
        ArrayList<StillCutVo> stillCutVos = mapStillCuts(movie.getStillCuts());

        BoxOfficeMovieDto boxOfficeMovieDto = new BoxOfficeMovieDto();
        boxOfficeMovieDto.movieId = movie.getMovieId();
        boxOfficeMovieDto.trailerUrl = movie.getTrailers().get(0).getTrailer_url();
        boxOfficeMovieDto.posterUrl = movie.getPoster().getPoster_image_url();
        boxOfficeMovieDto.rank = rank;
        boxOfficeMovieDto.movieNm = movieNm;
        boxOfficeMovieDto.genre = genreVos;
        boxOfficeMovieDto.stillCut = stillCutVos;
        boxOfficeMovieDto.backDrop = movie.getBackDrop().getBackDrop_image_url();
        boxOfficeMovieDto.plot = movie.getPlot().getPlotText();

        return boxOfficeMovieDto;
    }

    private ArrayList<GenreVo> mapGenres(List<Genre> genres) {
        ArrayList<GenreVo> genreVos = new ArrayList<>();
        for (Genre genre : genres) {
            GenreVo genreVo = new GenreVo();
            genreVo.setGenreNm(genre.getGenreNm());
            genreVos.add(genreVo);
        }
        return genreVos;
    }

    private ArrayList<StillCutVo> mapStillCuts(List<StillCut> stillCuts) {
        ArrayList<StillCutVo> stillCutVos = new ArrayList<>();
        for (StillCut stillCut : stillCuts) {
            StillCutVo stillCutVo = new StillCutVo();
            stillCutVo.setStillCut_url(stillCut.getStillCut_url());
            stillCutVos.add(stillCutVo);
        }
        return stillCutVos;
    }
}
