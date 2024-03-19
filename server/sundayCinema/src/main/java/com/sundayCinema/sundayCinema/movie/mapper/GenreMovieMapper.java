package com.sundayCinema.sundayCinema.movie.mapper;

import com.sundayCinema.sundayCinema.movie.dto.mainPageDto.GenreMovieDto;
import com.sundayCinema.sundayCinema.movie.dto.detaiPagelDto.StillCutVo;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.Movie;
import com.sundayCinema.sundayCinema.movie.entity.movieMediaInfo.StillCut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
public class GenreMovieMapper {
    public GenreMovieDto responseGenreMovieDto(Movie movie, String genreNm){

        String trailer="";
        if(movie.getTrailers().isEmpty()){
            trailer= "https://www.kmdb.or.kr/trailer/trailerPlayPop?pFileNm=MK006689_P03.mp4";
        }else{trailer= movie.getTrailers().get(0).getTrailer_url();}
        GenreMovieDto genreMovieDto = new GenreMovieDto();

        ArrayList<StillCutVo> stillCutVos =new ArrayList<>();
        for (int i = 0; i < movie.getStillCuts().size(); i++) {
            StillCutVo stillCutVo = new StillCutVo();
            StillCut stillCut = movie.getStillCuts().get(i);
            stillCutVo.setStillCut_url(stillCut.getStillCut_url());
            stillCutVos.add(stillCutVo);
        }
        genreMovieDto.movieId = movie.getMovieId();
        genreMovieDto.movieNm = movie.getMovieNm();
        genreMovieDto.posterUrl = movie.getPoster().getPoster_image_url();
        genreMovieDto.genre = genreNm;
        genreMovieDto.trailerUrl =trailer;
        genreMovieDto.backDrop = movie.getBackDrop().getBackDrop_image_url();
        genreMovieDto.stillCut= stillCutVos;
        genreMovieDto.plot = movie.getPlot().getPlotText();
        return genreMovieDto;
    }
}
