package com.sundayCinema.sundayCinema.movie.api.ApiRepoService;


import com.sundayCinema.sundayCinema.movie.Service.BoxOfficeSwitchService;
import com.sundayCinema.sundayCinema.movie.api.KOBIS.KobisService;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.BoxOfficeMovie;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.Movie;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.MovieCategoryCode;
import com.sundayCinema.sundayCinema.movie.entity.movieDetailInfo.*;
import com.sundayCinema.sundayCinema.movie.repository.boxOfficeRepo.BoxOfficeMovieRepository;
import com.sundayCinema.sundayCinema.movie.repository.boxOfficeRepo.ForeignBoxOfficeRepository;
import com.sundayCinema.sundayCinema.movie.repository.boxOfficeRepo.KoreaBoxOfficeRepository;
import com.sundayCinema.sundayCinema.movie.repository.movieInfoRepo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KobisRepoService {
    @Autowired
    private final MovieRepository movieRepository;
    @Autowired
    private final ActorRepository actorRepository;
    @Autowired
    private final DirectorRepository directorRepository;
    @Autowired
    private final MovieAuditRepository movieAuditRepository;
    @Autowired
    private final NationRepository nationRepository;
    @Autowired
    private final GenreRepository genreRepository;
    @Autowired
    private final BoxOfficeMovieRepository boxOfficeMovieRepository;
    @Autowired
    private final KoreaBoxOfficeRepository koreaBoxOfficeRepository;
    @Autowired
    private final ForeignBoxOfficeRepository foreignBoxOfficeRepository;
    @Autowired
    private final KobisService kobisService;
    @Autowired
    private final BoxOfficeSwitchService boxOfficeSwitchService;


    // 이넘이 메모리 어디에 위치하는지 공부 필요.
    // final 사용 필요 -> dto 와 vo 에 대한 이해 필요.
    @Transactional
    public List<BoxOfficeMovie> searchAndSaveMovieAndBoxOffice(MovieCategoryCode movieCategoryCode) throws Exception {
        List<BoxOfficeMovie> returnBoxList = new ArrayList<>();
        List<BoxOfficeMovie> boxOfficeList = kobisService.searchingTop10BoxOffice(movieCategoryCode); //검색
        saveMovieDetails(boxOfficeList);
        switch (movieCategoryCode) {                                                                  //기존 자료 삭제
            case COMPREHENSIVE_BOX_OFFICE:
                boxOfficeMovieRepository.deleteAll();
                break;
            case KOREA_BOX_OFFICE:
                koreaBoxOfficeRepository.deleteAll();
                break;
            case FOREIGN_BOX_OFFICE:
                foreignBoxOfficeRepository.deleteAll();
                break;
            default:
                // 지원하지 않는 타입
                throw new IllegalArgumentException("Unsupported box office type: " + movieCategoryCode);
        }

        for (BoxOfficeMovie boxOfficeMovie : boxOfficeList) {
            Movie movie = movieRepository.findByMovieCd(boxOfficeMovie.getMovieCd());
            boxOfficeMovie.setMovie(movie);
            returnBoxList.add(boxOfficeMovie);
            boxOfficeSwitchService.saveBoxOffice(movieCategoryCode, boxOfficeMovie); // 저장
        }
        return returnBoxList;
    }


    //영화 세부 정보 저장
    public void saveMovieDetails(List<BoxOfficeMovie> boxList) throws Exception {
        List<Movie> movieList = kobisService.searchingMovieInfo(boxList);
        for (int i = 0; i < movieList.size(); i++) { //foreach 문 사용 필요 ->  근데 스트림이 더 깔끔(자바 인액션, 자바 모던 프로젝트 참고 필요)
            if (verifyExistMovie(movieList.get(i).getMovieCd())) {
                continue;
            }// 박스오피스 객체가 영화 레포에 중복으로 존재하면 아무것도 안함
            movieRepository.save(movieList.get(i));
            saveDirectors(movieList.get(i));
            saveActors(movieList.get(i));
            saveGenres(movieList.get(i));
            saveMovieAudit(movieList.get(i));
            saveNations(movieList.get(i));

        }
    }

    public boolean verifyExistMovie(String movieCd) {
        boolean movieExists = movieRepository.existsByMovieCd(movieCd);

        return movieExists;
    }

    public void saveDirectors(Movie movie) {
        List<Director> directors = movie.getDirectors();

        if (directors != null && !directors.isEmpty()) {
            Long maxDirectorId = directorRepository.findMaxDirectorId();

            for (int i = 0; i < directors.size(); i++) {
                Director director = directors.get(i);
                director.setMovie(movie);

                directorRepository.save(director);
            }
        }
    }

    public void saveGenres(Movie movie) {
        for (int i = 0; i < movie.getGenres().size(); i++) {
            if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
                Genre genre = movie.getGenres().get(i);
                genre.setMovie(movie);

                genreRepository.save(genre);

            }
        }
    }


    public void saveNations(Movie movie) {
        for (int i = 0; i < movie.getNations().size(); i++) {
            if (movie.getNations().get(i) != null && !movie.getNations().isEmpty()) {
                Nation nation = movie.getNations().get(i);
                nation.setMovie(movie);
                nationRepository.save(nation);
            }
        }
    }

    public void saveMovieAudit(Movie movie) {
        for (int i = 0; i < movie.getAudits().size(); i++) {
            if (movie.getAudits() != null && !movie.getAudits().isEmpty()) {
                MovieAudit audit = movie.getAudits().get(i);
                audit.setMovie(movie);
                movieAuditRepository.save(audit);
            }
        }
    }


    public void saveActors(Movie movie) {
        List<Actor> actors = movie.getActors();
        int maxActorsToSave = Math.min(actors.size(), 4); // 최대 4명까지만 저장

        if (maxActorsToSave > 0) {
            Long maxActorId = actorRepository.findMaxActorId();

            for (int i = 0; i < maxActorsToSave; i++) {
                Actor actor = actors.get(i);
                actor.setMovie(movie);
                actorRepository.save(actor);
            }
        }
    }
}
