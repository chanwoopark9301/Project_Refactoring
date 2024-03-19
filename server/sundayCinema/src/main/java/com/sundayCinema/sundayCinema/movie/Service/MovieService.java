package com.sundayCinema.sundayCinema.movie.Service;

import com.sundayCinema.sundayCinema.movie.api.ApiRepoService.KobisRepoService;
import com.sundayCinema.sundayCinema.movie.api.ApiRepoService.MediaRepoService;
import com.sundayCinema.sundayCinema.movie.dto.mainPageDto.GenreMovieDto;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.BoxOfficeMovie;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.MovieCategoryCode;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.Movie;
import com.sundayCinema.sundayCinema.movie.mapper.GenreMovieMapper;
import com.sundayCinema.sundayCinema.movie.repository.movieInfoRepo.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    @Autowired private final MovieRepository movieRepository;
    @Autowired private final GenreMovieMapper genreMovieMapper;
    @Autowired private final KobisRepoService kobisRepoService;
    @Autowired private final MediaRepoService mediaRepoService;

//    @Scheduled(cron = "0 0 0 * * ?") -> 배치 엔진에 대한 관리가 필요(시스템 설계 필요) -> 스프링 배치
    // 생각없이 예외를 던지는 것은 해악 -> 비지니스 로직에 맞는 예외를 던저야지, 신뢰도가 너무 낮다. 예외가 터지면 난 몰라라는 코드
    // 롤백이나 로그를 남길 수 있는, 시스템에 맞는 예외를 던져야함.
    // 실패를 하면, 어떻게 처리할지 -> 이중로그를 남긴다. 재실행을 한다. 롤백을 한다 등등... 모든 것들을 다 설계하고 고민하는 것이 필요.

    // 배치를 할 건데, 어떻게 설계할까? ->
    // 인터페이스, 상속, 템플릿 패턴, 테이블 설계, 전략 패턴(특정 상황에는 알고리즘을 다르게 한다거나) 등등에 대해 고민한 후(극한의 고민... 최소 2주 이상)
    // 충분히 깊이있는 고민을 한 후 배치를 본다.
    public void dailyUpdateAll() throws Exception {
        saveTop10All();
    }


    public void saveTop10All() throws Exception {
        List<BoxOfficeMovie> top10Box= kobisRepoService.searchAndSaveMovieAndBoxOffice(MovieCategoryCode.COMPREHENSIVE_BOX_OFFICE);
        saveMedia(top10Box);
        List<BoxOfficeMovie> kBox= kobisRepoService.searchAndSaveMovieAndBoxOffice(MovieCategoryCode.KOREA_BOX_OFFICE);
        saveMedia(kBox);
        List<BoxOfficeMovie> fBox= kobisRepoService.searchAndSaveMovieAndBoxOffice(MovieCategoryCode.FOREIGN_BOX_OFFICE);
        saveMedia(fBox);
    }

    @Cacheable(value = "Top10ReadMapper.findTop10")
    public List<GenreMovieDto>loadGenreMovie(String nation){
        String[] genreArray={"액션", "코메디", "드라마", "애니메이션", "스릴러", "판타지", "멜로/로맨스", "공포(호러)", "어드밴처", "범죄"};
        List<GenreMovieDto>genreMovieDtos=new ArrayList<>();
        List<Movie> movies = new ArrayList<>();

        if(nation.equals("종합"))movies= movieRepository.findAll();
        else if (nation.equals("국내"))movies= movieRepository.findByNationsNationNm("한국");
        else if(nation.equals("해외")) movies=movieRepository.findByNationsNationNmIsNot("한국");

        for (Movie findMovie : movies) {
           for(String genreNm : genreArray)
            if(parsingGenre(findMovie,genreNm)){
                GenreMovieDto genreMovieDto= genreMovieMapper.responseGenreMovieDto(findMovie,genreNm);
                genreMovieDtos.add(genreMovieDto);
            }
        }
        return genreMovieDtos;
    }

    public void saveMedia(List<BoxOfficeMovie> boxList) throws IOException, GeneralSecurityException {
        mediaRepoService.saveBackDrop(boxList);
        mediaRepoService.savePoster(boxList);
        mediaRepoService.savePlot(boxList);
        mediaRepoService.saveTrailer(boxList);
        mediaRepoService.saveStill(boxList);
        mediaRepoService.saveYoutubeReview(boxList);
    }
    public boolean parsingGenre(Movie movie, String genreNm) {
        for (int i = 0; i < movie.getGenres().size(); i++) {
            if (movie.getGenres().get(i).getGenreNm().equals(genreNm)) {
                return true;
            }
        }
        return false;
    }

}