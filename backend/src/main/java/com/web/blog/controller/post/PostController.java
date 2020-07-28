package com.web.blog.controller.post;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.web.blog.dao.post.PostDao;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.post.Post;

import com.web.blog.model.user.User;
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
@RequestMapping("/post")
@RestController
public class PostController {
    String SUCCESS = "success";

    @Autowired
    PostDao postDao;

//    @GetMapping("/latest")
//    @ApiOperation(value = "최신 글 조회")
//    public ResponseEntity<List<Post>> retrievePostbyLatest(){
//        return new ResponseEntity<List<Post>>(postDao.findAllByCreateDate(), HttpStatus.OK);
//    }
//
//    @GetMapping("/popularity")
//    @ApiOperation(value = "인기 글 조회")
//    public ResponseEntity<List<Post>> retrievePostbyPopularity(){
//        return new ResponseEntity<List<Post>>(postDao.findAllOrderByCount(), HttpStatus.OK);
//    }

    @GetMapping("/{author}/")
    @ApiOperation(value = "해당 유저 전체 글 조회")
    public ResponseEntity<List<Post>> retrievePost(@PathVariable String author) throws Exception{
        return new ResponseEntity<List<Post>>(postDao.getPostByAuthor(author),HttpStatus.OK);
    }


    @GetMapping("/{author}/{pid}")
    @ApiOperation(value = "글 조회")
    public ResponseEntity<Post> ClickPost(@PathVariable String author, @PathVariable int pid) throws Exception{
        Post post = postDao.getPostByAuthorAndPid(author,pid);
        post.setCount(post.getCount()+1);
        postDao.save(post);

        return new ResponseEntity<Post>(post,HttpStatus.OK);
    }

    @PostMapping("/{author}/create")
    @ApiOperation(value = "글쓰기")
    public Object create(@RequestBody Post post, HttpSession session) {
        User user = (User) session.getAttribute("User");
        BasicResponse result = new BasicResponse();
        if(post.getTitle()==null)
            System.out.printf("??");
        else
            System.out.printf(post.getTitle());
        post.setPostno(post.getPostno()+1);
        postDao.save(post);
        result.status = true;
        result.data = SUCCESS;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{author}/update/{pid}")
    @ApiOperation(value = "글 수정")
    public Object update(@RequestBody Post post, @PathVariable int pid, HttpSession session){
        Object o = session.getAttribute("User");
        BasicResponse result = new BasicResponse();
        post.setPid(pid);
        postDao.save(post);
        result.status = true;
        result.data = SUCCESS;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{author}/delete/{pid}")
    @ApiOperation(value = "글 삭제")

    public Object delete(@Valid @PathVariable int pid, @PathVariable String author) {
        Post findPost = postDao.getPostByAuthorAndPid(author, pid);
        BasicResponse result = new BasicResponse();
        if(findPost != null){
            postDao.delete(findPost);
            result.data = SUCCESS;
            result.status = true;
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.data = "fail";
        result.status = false;
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }

}
