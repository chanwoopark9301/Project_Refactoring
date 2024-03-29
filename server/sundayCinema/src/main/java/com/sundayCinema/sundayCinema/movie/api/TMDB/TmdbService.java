package com.sundayCinema.sundayCinema.movie.api.TMDB;

import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.BoxOfficeMovie;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TmdbService {
    private final String apiKey = "ccba510dce22da81439817bcf25e5121"; // application.properties에서 읽어올 수도 있음
    private final String tmdbBaseUrl = "https://api.themoviedb.org/3";
    private final RestTemplate restTemplate = new RestTemplate();


    public MovieDetails getMovieDetailsByTitle(BoxOfficeMovie boxOfficeMovie) {
        String movieTitle = boxOfficeMovie.getMovieNm();

        MovieDetails movieDetails = new MovieDetails(); // null 방지를 위한 기본값 부여

        // 영화 제목을 사용하여 API에서 영화 정보 가져오기
        String searchUrl = tmdbBaseUrl + "/search/movie?api_key=" + apiKey + "&language=ko-KR&query=" + movieTitle;
        SearchResults searchResults = restTemplate.getForObject(searchUrl, SearchResults.class);

        if (searchResults != null && searchResults.getResults() != null && !searchResults.getResults().isEmpty()) {
            int movieId = searchResults.getResults().get(0).getId();

            // 영화 ID를 사용하여 영화 상세 정보 가져오기
            String movieDetailsUrl = tmdbBaseUrl + "/movie/" + movieId + "?api_key=" + apiKey + "&language=ko-KR";
            movieDetails = restTemplate.getForObject(movieDetailsUrl, MovieDetails.class);

            if (movieDetails != null) {
                // 배경화면 및 포스터 이미지 URL을 절대 URL로 변환
                if(movieDetails.getBackdropPath() != null){
                    movieDetails.setBackdropPath(makeAbsoluteImageUrl(movieDetails.getBackdropPath()));
                }else  movieDetails.setBackdropPath("https://call.nts.go.kr/images/ap/cm/img_coming.png");
                if(movieDetails.getPosterPath() !=null) {
                    movieDetails.setPosterPath(makeAbsoluteImageUrl(movieDetails.getPosterPath()));
                } else movieDetails.setPosterPath(makeAbsoluteImageUrl("https://call.nts.go.kr/images/ap/cm/img_coming.png"));
                // 영화 예고편 키 값을 가져오기
                String trailerUrl = tmdbBaseUrl + "/movie/" + movieId + "/videos?api_key=" + apiKey + "&language=ko-KR";
                VideoResults videoResults = restTemplate.getForObject(trailerUrl, VideoResults.class);

                if (videoResults != null && videoResults.getResults() != null && !videoResults.getResults().isEmpty()) {
                    // 예고편의 첫 번째 동영상 키 반환
                    String trailerKey = videoResults.getResults().get(0).getKey();
                    movieDetails.setKey(trailerKey);

                }else  movieDetails.setKey("mXZZvpTvtIQ");
                return movieDetails;
            }
        }
        movieDetails.setTitle("기본값");
        movieDetails.setOverview("컨텐츠 준비중입니다.");
        movieDetails.setPosterPath("https://call.nts.go.kr/images/ap/cm/img_coming.png");
        movieDetails.setBackdropPath("https://call.nts.go.kr/images/ap/cm/img_coming.png");
        movieDetails.setKey("mXZZvpTvtIQ");
        return movieDetails;
    }


    // TMDB에서 제공하는 상대 이미지 URL을 절대 URL로 변환
    private String makeAbsoluteImageUrl(String relativeImageUrl) {
        if (relativeImageUrl != null && !relativeImageUrl.isEmpty()) {
            return "https://image.tmdb.org/t/p/w780" + relativeImageUrl;
        }
        return "https://call.nts.go.kr/images/ap/cm/img_coming.png";
    }
}