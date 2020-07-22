package com.web.blog.controller.post;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.web.blog.dao.post.PostDao;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.post.Post;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiOperation;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class PostController {
    String SUCCESS = "success";

    @Autowired
    PostDao postDao;

    @GetMapping("/post/{author}")
    @ApiOperation(value = "조회")
    public ResponseEntity<List<Post>> retrievePost(@PathVariable String author) throws Exception{
        return new ResponseEntity<List<Post>>(postDao.getPostByAuthor(author),HttpStatus.OK);
    }
    @GetMapping("/post/create")
    @ApiOperation(value = "글쓰기")
    public Object create(@RequestBody Post post) {
        BasicResponse result = new BasicResponse();
        postDao.save(post);
        result.status = true;
        result.data = SUCCESS;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping("/post/update/{pid}")
    @ApiOperation(value = "글 수정")
    public Object update(@RequestBody Post post, @PathVariable String pid){
        BasicResponse result = new BasicResponse();
        postDao.save(post);
        result.status = true;
        result.data = SUCCESS;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/post/delete")
    @ApiOperation(value = "글 삭제")

    public Object delete(@Valid @RequestBody Post post, @PathVariable String pid, @RequestParam String author) {
        Post findPost = postDao.getPostByAuthorAndPid(post.get, pid);
        BasicResponse result = new BasicResponse();
        if(post != null){
            postDao.delete(post);
            result.data = SUCCESS;
            result.status = true;
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.data = "fail";
        result.status = false;
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        //return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
