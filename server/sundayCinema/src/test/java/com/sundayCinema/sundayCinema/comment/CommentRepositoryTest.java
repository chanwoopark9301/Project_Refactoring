package com.sundayCinema.sundayCinema.comment;

import com.sundayCinema.sundayCinema.member.Member;
import com.sundayCinema.sundayCinema.member.MemberRepository;
import com.sundayCinema.sundayCinema.movie.entity.movieMainInfo.Movie;
import com.sundayCinema.sundayCinema.movie.repository.movieInfoRepo.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MemberRepository memberRepository;
    @BeforeEach
    public void before(){
        Member member = new Member();
        member.setMemberId(1L);
        member.setEmail("test@gmail.com");
        member.setPassword("test");
        member.setUsername("test");
        member.setProfileImageUrl("test");
        memberRepository.save(member);
        Movie movie = new Movie();
        movie.setMovieId(1L);
        movie.setMovieNm("test123");
        movie.setMovieNmEn("test123");
        movie.setMovieCd("asdf");
        movie.setMovieNmOg("112");
        movie.setShowTm("1");
        movie.setPrdtYear("1");
        movie.setOpenDt("1");
        movie.setPrdtStatNm("d");
        movie.setTypeNm("1");
        movieRepository.save(movie);
    }
    @Test
    public void saveCommentTest(){

        Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setMember(memberRepository.findById(1L).orElseThrow());
        comment.setMovie(movieRepository.findMovieByMovieId(1l));
        comment.setContent("test 중입니다");
        comment.setScore(5.0);
        comment.setCreatedAt(LocalDateTime.parse("2022-01-01T10:00:00"));
        comment.setModifiedAt(LocalDateTime.parse("2022-01-01T10:00:00"));
        Comment savedComment = commentRepository.save(comment);
        assertNotNull(savedComment);
        assertTrue(comment.getContent().equals(savedComment.getContent()));
        assertEquals(comment.getScore(), savedComment.getScore());
    }
}