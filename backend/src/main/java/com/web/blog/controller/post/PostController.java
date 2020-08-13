package com.web.blog.controller.post;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.web.blog.model.ErrorResponse;
import com.web.blog.model.account.Account;
import com.web.blog.model.account.repository.LikeRepository;
import com.web.blog.model.account.repository.PostRepository;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.account.repository.AccountRepository;
import com.web.blog.model.file.Image;
import com.web.blog.model.file.repository.ImageRepository;
import com.web.blog.model.post.LikeInfo;
import com.web.blog.model.post.Post;

import com.web.blog.model.post.PostInfo;
import com.web.blog.utils.TokenUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
    @Autowired
    TokenUtils tokenUtils;
    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/latest")
    @ApiOperation(value = "최신 글 조회")
    public ResponseEntity<List<PostInfo>> retrievePostbyLatest(){
        List<Post> post = postRepository.findAll(Sort.by("createDate").descending());
        List<PostInfo> postInfo = new ArrayList<>();
        List<String> userlist;
        List<Image> images;
        for(Post p : post ){
            userlist = likeRepository.findByPid(p.getPid());
            images = imageRepository.findByPid(p.getPid());
            postInfo.add(new PostInfo(p,likeRepository.countByPid(p.getPid()), userlist, images));
        }
        return new ResponseEntity<List<PostInfo>>(postInfo, HttpStatus.OK);
    }

    @GetMapping("/popularity")
    @ApiOperation(value = "인기 글 조회")
    public ResponseEntity<List<PostInfo>> retrievePostbyPopularity(){
        List<Post> post = postRepository.findAll(Sort.by("count").descending());
        List<PostInfo> postInfo = new ArrayList<>();
        List<String> userlist;
        List<Image> images;
        for(Post p : post ){
            userlist = likeRepository.findByPid(p.getPid());
            images = imageRepository.findByPid(p.getPid());
            postInfo.add(new PostInfo(p,likeRepository.countByPid(p.getPid()), userlist, images));
        }
        return new ResponseEntity<List<PostInfo>>(postInfo, HttpStatus.OK);
    }

    @GetMapping("/{author}")
    @ApiOperation(value = "해당 유저 전체 글 조회")
    public ResponseEntity<Object> retrievePost(@PathVariable String author) throws Exception{
        Account account = accountRepository.findByUsername(author);
        if(account == null){
            String msg = "not found";
            return new ResponseEntity<>(msg ,HttpStatus.CONFLICT);
        }
        List<Post> post = postRepository.getPostByAuthor(author);

        List<PostInfo> postInfo = new ArrayList<>();
        List<String> userlist;
        List<Image> images;
        for(Post p : post ){
            userlist = likeRepository.findByPid(p.getPid());
            images = imageRepository.findByPid(p.getPid());
            postInfo.add(new PostInfo(p,likeRepository.countByPid(p.getPid()), userlist, images));
        }
        return new ResponseEntity<>(postInfo, HttpStatus.OK);
    }


    @GetMapping("/{author}/{pid}")
    @ApiOperation(value = "글 조회")
    public Object ClickPost(@PathVariable String author, @PathVariable int pid) throws Exception{
        Post post = postRepository.getPostByAuthorAndPid(author,pid);
        post.setCount(post.getCount()+1);
        postRepository.save(post);

        int likeCount = likeRepository.countByPid(pid);
        List<String> userlist = likeRepository.findByPid(pid);
        List<Image> images = imageRepository.findByPid(pid);
        PostInfo postInfo = new PostInfo(post, likeCount, userlist, images);

        return new ResponseEntity<>(postInfo,HttpStatus.OK);
    }

    @PostMapping("/{author}")
    @ApiOperation(value = "글쓰기")
    public Object create(@PathVariable String author, @RequestBody String jsonObj) throws ParseException {
        BasicResponse result = new BasicResponse();
        //json parsing 부분
        JSONParser jsonParse = new JSONParser();
        JSONObject obj = (JSONObject) jsonParse.parse(jsonObj);

        String token = (String) obj.get("token");
        if(!author.equals(tokenUtils.getUserNameFromToken(token))){
            result.status = false;
            result.data = "fail";
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        JSONArray commentArray = (JSONArray) obj.get("post");
        JSONObject commentObject = (JSONObject) commentArray.get(0);
        JSONArray imageArray = (JSONArray) obj.get("images");

        String title = (String) commentObject.get("title");
        String content = (String) commentObject.get("content");
        String tags = (String) commentObject.get("tags");

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setPostno(postRepository.findByAuthor(author)+1);
        post.setAuthor(author);
        post.setTags(tags);
        postRepository.save(post);
        for(Object object : imageArray){
            JSONObject imageObject = (JSONObject) object;
            String str = (String) imageObject.get("image");
            Image image = imageRepository.findByIid(str);
            System.out.println(str);
            image.setPid(post.getPid());
            imageRepository.save(image);
        }

        result.status = true;
        result.data = SUCCESS;
        result.object = post;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{author}/{pid}")
    @ApiOperation(value = "글 수정")
    public Object update(@PathVariable String author, @PathVariable int pid, @RequestBody String jsonObj) throws ParseException {
        BasicResponse result = new BasicResponse();
        JSONParser jsonParse = new JSONParser();
        JSONObject obj = (JSONObject) jsonParse.parse(jsonObj);

        String token = (String) obj.get("token");
        if(!author.equals(tokenUtils.getUserNameFromToken(token))){
            result.status = false;
            result.data = "fail";
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        JSONArray commentArray = (JSONArray) obj.get("post");
        JSONObject commentObject = (JSONObject) commentArray.get(0);
        JSONArray imageArray = (JSONArray) obj.get("images");

        String title = (String) commentObject.get("title");
        String content = (String) commentObject.get("content");
        String tags = (String) commentObject.get("tags");

        Post post = postRepository.getPostByAuthorAndPid(author, pid);
        post.setTitle(title);
        post.setContent(content);
        post.setTags(tags);

        postRepository.save(post);
        List<Image> images = imageRepository.findByPid(pid);
        for(Image image : images){
            imageRepository.delete(image);
        }
        if(!imageArray.isEmpty()) {
            for (Object object : imageArray) {
                JSONObject imageObject = (JSONObject) object;
                String str = (String) imageObject.get("image");
                Image image = imageRepository.findByIid(str);
                System.out.println(str);
                image.setPid(post.getPid());
                imageRepository.save(image);
            }
        }
        
        result.status = true;
        result.data = SUCCESS;
        result.object = post;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{author}/{pid}")
    @ApiOperation(value = "글 삭제")

    public Object delete(@Valid @PathVariable int pid, @PathVariable String author, @RequestBody String jsonObj) throws ParseException {
        Post findPost = postRepository.getPostByAuthorAndPid(author, pid);
        BasicResponse result = new BasicResponse();
        JSONParser jsonParse = new JSONParser();
        JSONObject obj = (JSONObject) jsonParse.parse(jsonObj);

        String token = (String) obj.get("token");
        if((findPost != null) &&(tokenUtils.getUserNameFromToken(token).equals(author))){
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
        String username = accountRepository.findByUsername(author).getUsername();
        return likeRepository.findByPidAndUsername(pid, username).getLikeit();
    }

    @PostMapping("/{author}/{pid}/likeit")
    @ApiOperation(value = "좋아요 클릭 / 0 : 좋아요x / 1 : 좋아요")
    public Object clickLike(@PathVariable String author, @PathVariable int pid){
        String username = accountRepository.findByUsername(author).getUsername();
        LikeInfo likeInfo = new LikeInfo();

        likeInfo.setPid(pid);
        likeInfo.setUsername(username);
        likeInfo.setLikeit(likeRepository.findByPidAndUsername(pid, username).getLikeit() == 0 ? 1: 0);
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
