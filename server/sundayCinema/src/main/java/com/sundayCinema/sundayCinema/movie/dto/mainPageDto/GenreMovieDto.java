package com.sundayCinema.sundayCinema.movie.dto.mainPageDto;

import com.sundayCinema.sundayCinema.movie.dto.detaiPagelDto.StillCutVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GenreMovieDto {

    public String genre;
    public long movieId;
    public String movieNm;
    public String posterUrl;
    public String trailerUrl;
    public List<StillCutVo> stillCut;
    public String plot;
    public String backDrop;
}
