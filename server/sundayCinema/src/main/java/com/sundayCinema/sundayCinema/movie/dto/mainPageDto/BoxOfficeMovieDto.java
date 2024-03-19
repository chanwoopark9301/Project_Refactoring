package com.sundayCinema.sundayCinema.movie.dto.mainPageDto;

import com.sundayCinema.sundayCinema.movie.dto.detaiPagelDto.StillCutVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoxOfficeMovieDto {
    public Long movieId;
    public String posterUrl;
    public List<StillCutVo> stillCut;
    public String plot;
    public String rank;
    public String movieNm;
    public List<GenreVo> genre;
    public String trailerUrl;
    public String backDrop;

}
