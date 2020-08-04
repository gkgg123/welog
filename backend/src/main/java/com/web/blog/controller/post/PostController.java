package com.web.blog.controller.post;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.web.blog.dao.Account.AccountDao;
import com.web.blog.dao.post.LikeDao;
import com.web.blog.dao.post.PostDao;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.post.Post;

import com.web.blog.service.account.AccountService;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
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
    @Autowired
    LikeDao likeDao;
    @Autowired
    AccountDao accountDao;

    @GetMapping("/latest")
    @ApiOperation(value = "최신 글 조회")
    public ResponseEntity<List<Post>> retrievePostbyLatest(){
        return new ResponseEntity<List<Post>>(postDao.findAll(Sort.by("createDate").descending()), HttpStatus.OK);
    }

    @GetMapping("/popularity")
    @ApiOperation(value = "인기 글 조회")
    public ResponseEntity<List<Post>> retrievePostbyPopularity(){
        return new ResponseEntity<List<Post>>(postDao.findAll(Sort.by("count").descending()), HttpStatus.OK);
    }

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
    public Object create(@RequestBody Post post, @PathVariable String author) {
        BasicResponse result = new BasicResponse();
        post.setPostno(post.getPostno()+1);
        post.setAuthor(author);
        postDao.save(post);
        result.status = true;
        result.data = SUCCESS;
        result.object = post;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{author}/update/{pid}")
    @ApiOperation(value = "글 수정")
    public Object update(@RequestBody Post post, @PathVariable int pid){
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

    @GetMapping("/tag/{tag}")
    @ApiOperation(value = "태그 검색")
    public ResponseEntity<List<Post>> retrievePostbyTags(@PathVariable String tag){
        return new ResponseEntity<List<Post>>(postDao.findByTagsContaining(","+tag+","), HttpStatus.OK);
    }

    @PostMapping("/{author}/{pid}/like")
    @ApiOperation(value = "좋아요 클릭 / 0 : 좋아요x / 1 : 좋아요")
    public int clickLike(@PathVariable String author, @PathVariable int pid){
        int uid = accountDao.findByUsername(author).getId();
        return likeDao.findByPidAndUid(pid, uid).getIsLike() == 0 ? 1: 0;
    }

}
