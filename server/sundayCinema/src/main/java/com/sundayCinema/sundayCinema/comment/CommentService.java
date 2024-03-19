package com.sundayCinema.sundayCinema.comment;

import com.sundayCinema.sundayCinema.exception.BusinessLogicException;
import com.sundayCinema.sundayCinema.exception.ExceptionCode;
import com.sundayCinema.sundayCinema.member.Member;
import com.sundayCinema.sundayCinema.member.MemberRepository;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.DetailMovie;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.Movie;
import com.sundayCinema.sundayCinema.movie.repository.movieInfoRepo.MovieRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper,
                          MemberRepository memberRepository, MovieRepository movieRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.memberRepository = memberRepository;
        this.movieRepository = movieRepository;
    }


    public CommentDto.CommentResponseDto createComment(CommentDto.CommentPostDto commentPostDto) {

        verifyExistsEmail();

        Comment comment = commentMapper.commentPostDtoToComment(commentPostDto);
        Member member = memberRepository.findById(commentPostDto.getMemberId()).orElseThrow();
        Movie movie = movieRepository.findMovieByMovieId(commentPostDto.getMovieId());
        comment.setMember(member);
        comment.setMovie(movie);
        comment = commentRepository.save(comment);
        CommentDto.CommentResponseDto commentResponseDto = commentMapper.commentToCommentResponseDto(comment);
        return commentResponseDto;
    }

    public CommentDto.CommentResponseDto updateComment(CommentDto.CommentPatchDto commentPatchDto) {

        Comment comment = commentRepository.findById(commentPatchDto.getCommentId()).orElse(null);
        if (comment != null) {

            if (commentPatchDto.getContent() != null) {
                comment.setContent(commentPatchDto.getContent());
            }
            if (commentPatchDto.getScore() > 0) {
                comment.setScore(commentPatchDto.getScore());
            }
            comment = commentRepository.save(comment);
            return commentMapper.commentToCommentResponseDto(comment);
        }
        return null;
    }


    // Get comments for a movie
    public CommentDto.CommentResponseDto getCommentsForMovie(long movieId, long memberId) {

        verifyExistsEmail();


        Optional<Comment> commentOptional = commentRepository.findByMovieMovieIdAndMemberMemberId(movieId, memberId);

        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            return commentMapper.commentToCommentResponseDto(comment);

        } else {
            throw new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND);

        }

    }

    @Cacheable(value = "getAllCommentsForMovie")
    public List<CommentDto.CommentResponseDto> getAllCommentsForMovie(Long movieId) {

        List<Comment> comments = commentRepository.findByMovieMovieId(movieId);

        return commentMapper.commentsToCommentResponseDtos(comments);
    }


    // Delete a comment
    public boolean deleteComment(long commentId) {
        verifyExistsEmail();

        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            commentRepository.delete(comment);
            return true;
        }
        return false;
    }

    @Cacheable(value = "calculateAverageRatingForMovie")
    public double calculateAverageRatingForMovie(Long movieId) {

        List<Comment> comments = commentRepository.findByMovieMovieId(movieId);
        if (comments.isEmpty()) {
            return 0.0;
        }
        double totalScore = comments.stream().mapToDouble(Comment::getScore).sum();
        return totalScore / comments.size();
    }

    private Member verifyExistsEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            return memberRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        } else {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_SIGNED_IN);
        }
    }

}