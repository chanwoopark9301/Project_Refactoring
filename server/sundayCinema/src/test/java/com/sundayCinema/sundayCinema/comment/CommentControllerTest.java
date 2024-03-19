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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
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
    @Test
    public void getAllCommentsForMovieTest() throws Exception {

        String movieId="2";
        String url = "/api/comments/movie/"+movieId;

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get(url));
        MvcResult result = actions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println("/////////////////////////////////////////////"+contentAsString);

        actions.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));

    }
}