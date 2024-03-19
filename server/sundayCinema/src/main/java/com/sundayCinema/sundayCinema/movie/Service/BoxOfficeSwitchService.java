package com.sundayCinema.sundayCinema.movie.Service;

import com.sundayCinema.sundayCinema.movie.dto.mainPageDto.BoxOfficeMovieDto;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.*;
import com.sundayCinema.sundayCinema.movie.mapper.BoxOfficeMovieMapper;
import com.sundayCinema.sundayCinema.movie.repository.boxOfficeRepo.BoxOfficeMovieRepository;
import com.sundayCinema.sundayCinema.movie.repository.boxOfficeRepo.ForeignBoxOfficeRepository;
import com.sundayCinema.sundayCinema.movie.repository.boxOfficeRepo.GenreBoxOfficeRepository;
import com.sundayCinema.sundayCinema.movie.repository.boxOfficeRepo.KoreaBoxOfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BoxOfficeSwitchService {
    @Autowired private final BoxOfficeMovieRepository boxOfficeMovieRepository;
    @Autowired private final KoreaBoxOfficeRepository koreaBoxOfficeRepository;
    @Autowired private final ForeignBoxOfficeRepository foreignBoxOfficeRepository;
    @Autowired private final GenreBoxOfficeRepository genreBoxOfficeRepository;
    @Autowired private final BoxOfficeMovieMapper boxOfficeMovieMapper;


    //박스 오피스 타입을 변환해서 저장
    public void saveBoxOffice(MovieCategoryCode movieCategoryCode, BoxOfficeMovie boxOfficeMovie){
        if(movieCategoryCode.equals(MovieCategoryCode.COMPREHENSIVE_BOX_OFFICE)){
            BoxOfficeMovie newBoxOfficeMovie = new BoxOfficeMovie(boxOfficeMovie);
            boxOfficeMovieRepository.save(newBoxOfficeMovie);
        }
        else if(movieCategoryCode.equals(MovieCategoryCode.KOREA_BOX_OFFICE)){
            koreaBoxOfficeRepository.save(createKoreaBoxOffice(boxOfficeMovie));
        }
        else if(movieCategoryCode.equals(MovieCategoryCode.FOREIGN_BOX_OFFICE)){
            foreignBoxOfficeRepository.save(createForeignBoxOffice(boxOfficeMovie));
        }
    }
    @Cacheable(value = "Top10ReadMapper.findTop10")
    public List<BoxOfficeMovieDto> loadBoxOfficeByBoxNm(String boxOfficeCd) {
        List<BoxOfficeMovieDto> boxOfficeMovieDtos = new ArrayList<>();

        if ("".equals(boxOfficeCd)) {
            List<BoxOfficeMovie> boxOfficeMovies = boxOfficeMovieRepository.findAll();
            for (BoxOfficeMovie box : boxOfficeMovies) {
                BoxOfficeMovieDto boxOfficeMovieDto = boxOfficeMovieMapper.boxOfficeResponseDto(box);
                boxOfficeMovieDtos.add(boxOfficeMovieDto);
            }
        } else if ("K".equals(boxOfficeCd)) {
            List<KoreaBoxOffice> koreaBoxOffices = koreaBoxOfficeRepository.findAll();
            for (KoreaBoxOffice koreaBox : koreaBoxOffices) {
                BoxOfficeMovieDto boxOfficeMovieDto = boxOfficeMovieMapper.boxOfficeResponseDto(koreaBox);
                boxOfficeMovieDtos.add(boxOfficeMovieDto);
            }
        } else if ("F".equals(boxOfficeCd)) {
            List<ForeignBoxOffice> foreignBoxOffices = foreignBoxOfficeRepository.findAll();
            for (ForeignBoxOffice foreignBox : foreignBoxOffices) {
                BoxOfficeMovieDto boxOfficeMovieDto = boxOfficeMovieMapper.boxOfficeResponseDto(foreignBox);
                boxOfficeMovieDtos.add(boxOfficeMovieDto);
            }
        }

        return boxOfficeMovieDtos;
    }
    private KoreaBoxOffice createKoreaBoxOffice(BoxOfficeMovie boxOfficeMovie) {
        KoreaBoxOffice koreaBoxOffice = new KoreaBoxOffice(boxOfficeMovie);

        return koreaBoxOffice;
    }

    private ForeignBoxOffice createForeignBoxOffice(BoxOfficeMovie boxOfficeMovie) {
        ForeignBoxOffice foreignBoxOffice = new ForeignBoxOffice(boxOfficeMovie);

        return foreignBoxOffice;
    }
    private GenreBoxOffice createGenreBoxOffice(BoxOfficeMovie boxOfficeMovie) {
        GenreBoxOffice genreBoxOffice = new GenreBoxOffice(boxOfficeMovie);
        return genreBoxOffice;
    }
}
