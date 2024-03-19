package com.sundayCinema.sundayCinema.movie.api.KOBIS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.BoxOfficeMovie;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.Movie;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.MovieCategoryCode;
import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class KobisService {
    @Value("${KOBIS_API_KEY}")
    private String KobisApiKEY; // 발급받은 API 키 값을 입력해주세요

    private String boxResponse = "";
    private String movieResponse = "";
    //전날 박스오피스 조회 ( 오늘 날짜꺼는 안나옴.. )
    LocalDateTime time = LocalDateTime.now().minusDays(1);
    String targetDt = time.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    //ROW 개수
    String itemPerPage = "10";

    //다양성영화(Y)/상업영화(N)/전체(default)
    private final String multiMovieYn = "N";

    //한국영화(K)/외국영화(F)/전체(default) -> enum 코드 사용
//    String repNationCd = "";
//    String repNationCd_Korean = "K";
//    String repNationCd_Foreign = "F";

    //상영지역별 코드/전체(default)
    private final String  wideAreaCd = "";

    String[] movieTypeCdArr = new String[0];


    // 탑텐 박스 오피스저장(박스오피스 객체)

    public List<BoxOfficeMovie> searchingTop10BoxOffice(MovieCategoryCode movieCategoryCode) throws Exception {
        KobisOpenAPIRestService service = new KobisOpenAPIRestService(KobisApiKEY);

        // 일일 박스오피스 서비스 호출 (boolean isJson, String targetDt, String itemPerPage,String multiMovieYn, String repNationCd, String wideAreaCd)
        boxResponse = service.getDailyBoxOffice(true, targetDt, itemPerPage, multiMovieYn, movieCategoryCode.getValue(), wideAreaCd);

        BoxofficeResponse parsingResponse = parsingKobis(boxResponse);
        List<BoxOfficeMovie> dailyList = parsingResponse.getBoxOfficeResult().getDailyBoxOfficeList();

        return dailyList;
    }

    //특정 날짜에 따른 박스오피스 정보 검색

    public List<BoxOfficeMovie> searchingGenreBoxOffice(String targetDt, MovieCategoryCode movieCategoryCode) throws Exception {
        KobisOpenAPIRestService service = new KobisOpenAPIRestService(KobisApiKEY);

        // 일일 박스오피스 서비스 호출 (boolean isJson, String targetDt, String itemPerPage,String multiMovieYn, String repNationCd, String wideAreaCd)
        boxResponse = service.getDailyBoxOffice(true, targetDt, itemPerPage, multiMovieYn, String.valueOf(movieCategoryCode), wideAreaCd);

        BoxofficeResponse parsingResponse = parsingKobis(boxResponse);
        List<BoxOfficeMovie> genreList = parsingResponse.getBoxOfficeResult().getDailyBoxOfficeList();

        return genreList; // 박스오피스 객체 리스트
    }

    //세부 영화 정보 저장(박스 오피스 객체)
    public List<Movie> searchingMovieInfo(List<BoxOfficeMovie> boxOfficeMovieList) throws Exception {
        KobisOpenAPIRestService service = new KobisOpenAPIRestService(KobisApiKEY);
        List<Movie> movieList = new ArrayList<>();

        for (int i=0; i< boxOfficeMovieList.size(); i++) {
            BoxOfficeMovie boxOfficeMovie= boxOfficeMovieList.get(i);
            movieResponse = service.getMovieInfo(true, boxOfficeMovie.getMovieCd());
            Movie parsingMovie = parsingMovieInfo(movieResponse);
            movieList.add(parsingMovie);
        }

        return movieList;
    }

    //  api를 통해 받아온 String 타입의 json 값을 박스오피스 객체로 변환
    public BoxofficeResponse parsingKobis(String response) throws JsonProcessingException {
        String jsonString = response;

        ObjectMapper objectMapper = new ObjectMapper();
        BoxofficeResponse boxOfficeData = objectMapper.readValue(jsonString, BoxofficeResponse.class);

        return boxOfficeData;
    }

    public Movie parsingMovieInfo(String movieResponse) throws JsonProcessingException {
        String jsonString = movieResponse;

        ObjectMapper objectMapper = new ObjectMapper();
        MovieResponse movieData = objectMapper.readValue(jsonString, MovieResponse.class);
        Movie findMovie = movieData.getMovieInfoResult().getMovieInfo();

        return findMovie; //반환한 movie 객체
    }
}
