package com.sundayCinema.sundayCinema.movie.entity.movieMainInfo;

import lombok.Getter;

@Getter
public enum MovieCategoryCode {
    COMPREHENSIVE_BOX_OFFICE(""),
    KOREA_BOX_OFFICE("K"),
    FOREIGN_BOX_OFFICE("F"),
    GENRE_MOVIE("G");


    private final String value;
    MovieCategoryCode(String value) {
        this.value = value;
    }


}
