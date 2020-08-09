package com.web.blog.controller.post;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.web.blog.model.account.repository.LikeRepository;
import com.web.blog.model.account.repository.PostRepository;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.account.repository.AccountRepository;
import com.web.blog.model.post.LikeInfo;
import com.web.blog.model.post.Post;

import com.web.blog.model.post.PostInfo;
import org.springframework.data.domain.Sort;
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
    PostRepository postRepository;
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/latest")
    @ApiOperation(value = "최신 글 조회")
    public ResponseEntity<List<PostInfo>> retrievePostbyLatest(){
        List<Post> post = postRepository.findAll(Sort.by("createDate").descending());
        List<PostInfo> postInfo = new ArrayList<>();
        for(Post p : post ){
            postInfo.add(new PostInfo(p,likeRepository.countByPid(p.getPid())));
        }
        return new ResponseEntity<List<PostInfo>>(postInfo, HttpStatus.OK);
    }

    @GetMapping("/popularity")
    @ApiOperation(value = "인기 글 조회")
    public ResponseEntity<List<PostInfo>> retrievePostbyPopularity(){
        List<Post> post = postRepository.findAll(Sort.by("count").descending());
        List<PostInfo> postInfo = new ArrayList<>();
        for(Post p : post ){
            postInfo.add(new PostInfo(p,likeRepository.countByPid(p.getPid())));
        }
        return new ResponseEntity<List<PostInfo>>(postInfo, HttpStatus.OK);
    }

    @GetMapping("/{author}")
    @ApiOperation(value = "해당 유저 전체 글 조회")
    public ResponseEntity<List<PostInfo>> retrievePost(@PathVariable String author) throws Exception{
        List<Post> post = postRepository.getPostByAuthor(author);
        List<PostInfo> postInfo = new ArrayList<>();
        for(Post p : post ){
            postInfo.add(new PostInfo(p,likeRepository.countByPid(p.getPid())));

        }
        return new ResponseEntity<List<PostInfo>>(postInfo, HttpStatus.OK);
    }


    @GetMapping("/{author}/{pid}")
    @ApiOperation(value = "글 조회")
    public Object ClickPost(@PathVariable String author, @PathVariable int pid) throws Exception{
        BasicResponse result = new BasicResponse();
        Post post = postRepository.getPostByAuthorAndPid(author,pid);
        post.setCount(post.getCount()+1);
        postRepository.save(post);
        result.status = true;
        result.data = SUCCESS;
        result.object = post;
        result.count = likeRepository.countByPid(pid);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/{author}")
    @ApiOperation(value = "글쓰기")
    public Object create(@RequestBody Post post, @PathVariable String author) {
        BasicResponse result = new BasicResponse();
        post.setPostno(post.getPostno()+1);
        post.setAuthor(author);
        postRepository.save(post);

        result.status = true;
        result.data = SUCCESS;
        result.object = post;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{author}/{pid}")
    @ApiOperation(value = "글 수정")
    public Object update(@RequestBody Post post, @PathVariable String author, @PathVariable int pid){
        BasicResponse result = new BasicResponse();
        post.setPid(pid);
        post.setAuthor(author);
        postRepository.save(post);
        result.status = true;
        result.data = SUCCESS;
        result.object = post;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{author}/{pid}")
    @ApiOperation(value = "글 삭제")

    public Object delete(@Valid @PathVariable int pid, @PathVariable String author) {
        Post findPost = postRepository.getPostByAuthorAndPid(author, pid);
        BasicResponse result = new BasicResponse();
        if(findPost != null){
            postRepository.delete(findPost);
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
        return new ResponseEntity<List<Post>>(postRepository.findByTagsContaining(","+tag+","), HttpStatus.OK);
    }

    @GetMapping("/{author}/{pid}/likeit")
    @ApiOperation(value= "좋아요인지 아닌지")
    public int likeIt(@PathVariable String author, @PathVariable int pid){
        int uid = accountRepository.findByUsername(author).getId();
        return likeRepository.findByPidAndUid(pid, uid).getIsLike();
    }

    @PostMapping("/{author}/{pid}/likeit")
    @ApiOperation(value = "좋아요 클릭 / 0 : 좋아요x / 1 : 좋아요")
    public Object clickLike(@PathVariable String author, @PathVariable int pid){
        int uid = accountRepository.findByUsername(author).getId();
        LikeInfo likeInfo = new LikeInfo();
        likeInfo.setPid(pid);
        likeInfo.setUid(uid);
        likeInfo.setIsLike(likeRepository.findByPidAndUid(pid, uid).getIsLike() == 0 ? 1: 0);
        likeRepository.save(likeInfo);
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = likeInfo;
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/searchPost")
    @ApiOperation(value = "제목 + 내용 검색")
    public ResponseEntity<List<Post>> searchPost(@RequestParam String text){
        return new ResponseEntity<List<Post>>(postRepository.findByTitleAndContent(text, text), HttpStatus.OK);
    }


}
