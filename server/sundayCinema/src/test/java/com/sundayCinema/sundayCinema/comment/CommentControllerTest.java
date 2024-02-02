package com.sundayCinema.sundayCinema.comment;

import com.google.api.client.json.Json;
import com.google.gson.Gson;
import org.apache.http.HttpRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;

import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private Gson gson;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommentService commentService;
    @Autowired
    private CommentMapper mapper;
    @Test
    public void postCommentTest() throws Exception {
        CommentDto.CommentPostDto commentPostDto =
                new CommentDto.CommentPostDto("This Movie's Good!", 5.0, 1L, 1L);
        Comment stubPostComment= mapper.commentPostDtoToComment(commentPostDto);
        CommentDto.CommentResponseDto responseDto = mapper.commentToCommentResponseDto(stubPostComment);
        stubPostComment.setCommentId(1L);
        given(commentService.createComment(Mockito.any(CommentDto.CommentPostDto.class)))
                        .willReturn(responseDto);


        String content = gson.toJson(commentPostDto);

        ResultActions actions = mockMvc.perform(post("/api/comments/post")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        actions.andExpect(status().isOk());

    }
}