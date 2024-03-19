package com.sundayCinema.sundayCinema.comment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/post")
    public ResponseEntity<CommentDto.CommentResponseDto> createComment(
            @Valid @RequestBody CommentDto.CommentPostDto commentPostDto) {


        CommentDto.CommentResponseDto response = commentService.createComment(commentPostDto);

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto.CommentResponseDto> updateComment(
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentDto.CommentPatchDto commentPatchDto) {
        commentPatchDto.setCommentId(commentId);

        CommentDto.CommentResponseDto response = commentService.updateComment(commentPatchDto);

        return ResponseEntity.ok(response);

    }


    @GetMapping("/movie/{movieId}/{memberId}")
    public ResponseEntity<?> getCommentsForMovie(
            @PathVariable("movieId") Long movieId,
            @PathVariable(name = "memberId", required = false) Long memberId) {

        // movieId와 memberId가 null이면 BadRequest로 처리
        if (movieId == null || memberId == null) {
            return ResponseEntity.badRequest().body("movieId and memberId must not be null.");
        }

       CommentDto.CommentResponseDto comment = commentService.getCommentsForMovie(movieId, memberId);
        return ResponseEntity.ok(comment);
    }
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> getAllCommentsForMovie(@PathVariable("movieId") Long movieId) {

        // movieId와 memberId가 null이면 BadRequest로 처리
        if (movieId == null) {
            return ResponseEntity.badRequest().body("movieId must not be null.");
        }

        List<CommentDto.CommentResponseDto> comments = commentService.getAllCommentsForMovie(movieId);
        return ResponseEntity.ok(comments);
    }




    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") long commentId) {
        if (commentService.deleteComment(commentId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/movie/{movieId}/average-rating")
    public ResponseEntity<Double> getAverageRatingForMovie(@PathVariable("movieId") Long movieId
                                                           ) {
        // movieId와 memberId가 null이면 Bad Request로 처리
        if (movieId == null) {
            return ResponseEntity.badRequest().build();
        }

        double averageRating = commentService.calculateAverageRatingForMovie(movieId);
        return ResponseEntity.ok(averageRating);
    }

}