package com.sundayCinema.sundayCinema.comment;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

public class CommentDto {

    @Getter
    @Setter
    public static class CommentPostDto {
        public CommentPostDto(String content, double score, Long movieId, Long memberId) {
            this.content = content;
            this.score = score;
            this.movieId = movieId;
            this.memberId = memberId;
        }

        @NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
        private String content;

        @NotNull(message = "평점은 필수 입력 항목입니다.")
        private double score;

        @NotNull(message = "영화 ID는 필수 입력 항목입니다.")
        private Long movieId;

        @NotNull(message = "사용자 ID는 필수 입력 항목입니다.")
        private Long memberId;
    }

    @Getter
    @Setter
    public static class CommentPatchDto {
        @NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
        private String content;
        @NotNull(message = "commentId는 필수 사항입니다.")
        private Long commentId;
        @NotNull(message = "사용자 Id는 필수 사항입니다.")
        private Long movieId;
        @NotNull(message = "평점은 필수 입력 항목입니다.")
        private double score;

    }
    @Getter
    @Setter
    public static class CommentResponseDto{
        @NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
        private String content;

        private Long commentId;

        @NotNull(message = "평점은 필수 입력 항목입니다.")
        private double score;
    }
}
