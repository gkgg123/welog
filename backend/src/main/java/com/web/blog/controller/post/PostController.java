package com.web.blog.controller.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.web.blog.model.ErrorResponse;
import com.web.blog.model.account.Account;
import com.web.blog.model.account.repository.CommentRepository;
import com.web.blog.model.account.repository.LikeRepository;
import com.web.blog.model.account.repository.PostRepository;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.account.repository.AccountRepository;
import com.web.blog.model.file.Image;
import com.web.blog.model.file.repository.ImageRepository;
import com.web.blog.model.post.*;

import com.web.blog.utils.TokenUtils;
import io.swagger.annotations.Api;
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

@ApiResponses(value = {@ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class)})

@CrossOrigin(origins = {"http://localhost:3000"})
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
    @Autowired
    CommentRepository commentRepository;

    @GetMapping("/latest")
    @ApiOperation(value = "최신 글 조회")
    public ResponseEntity<List<PostInfo>> retrievePostbyLatest() {
        List<Post> post = postRepository.findAll(Sort.by("createDate").descending());
        List<PostInfo> postInfo = new ArrayList<>();
        List<String> userlist;
        List<Image> images;
        for (Post p : post) {
            userlist = likeRepository.findByPid(p.getPid());
            images = imageRepository.findByPid(p.getPid());
            int commentCnt = commentRepository.getCommentByPid(p.getPid()).size();
            postInfo.add(new PostInfo(p, userlist.size(), commentCnt, userlist, images));
        }
        return new ResponseEntity<List<PostInfo>>(postInfo, HttpStatus.OK);
    }

    @GetMapping("/popularity")
    @ApiOperation(value = "인기 글 조회")
    public ResponseEntity<List<PostInfo>> retrievePostbyPopularity() {
        List<Post> post = postRepository.findAll(Sort.by("count").descending());
        List<PostInfo> postInfo = new ArrayList<>();
        List<String> userlist;
        List<Image> images;
        for (Post p : post) {
            userlist = likeRepository.findByPid(p.getPid());
            images = imageRepository.findByPid(p.getPid());
            int commentCnt = commentRepository.getCommentByPid(p.getPid()).size();
            postInfo.add(new PostInfo(p, userlist.size(), commentCnt, userlist, images));
        }
        return new ResponseEntity<List<PostInfo>>(postInfo, HttpStatus.OK);
    }

    @GetMapping("/{author}")
    @ApiOperation(value = "해당 유저 전체 글 조회")
    public ResponseEntity<Object> retrievePost(@PathVariable String author) throws Exception {
        Account account = accountRepository.findByUsername(author);
        if (account == null) {
            String msg = "not found";
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
        List<Post> post = postRepository.getPostByAuthor(author);

        List<PostInfo> postInfo = new ArrayList<>();
        List<String> userlist;
        List<Image> images;
        for (Post p : post) {
            userlist = likeRepository.findByPid(p.getPid());
            images = imageRepository.findByPid(p.getPid());
            int commentCnt = commentRepository.getCommentByPid(p.getPid()).size();
            postInfo.add(new PostInfo(p, userlist.size(), commentCnt, userlist, images));
        }
        return new ResponseEntity<>(postInfo, HttpStatus.OK);
    }


    @GetMapping("/{author}/{pid}")
    @ApiOperation(value = "글 조회")
    public Object ClickPost(@PathVariable String author, @PathVariable int pid) throws Exception {
        Post post = postRepository.getPostByAuthorAndPid(author, pid);
        Account account = accountRepository.findByUsername(author);
        post.setCount(post.getCount() + 1);
        postRepository.save(post);

        List<String> userlist = likeRepository.findByPid(pid);
        int likeCount = userlist.size();
        int commentCount = commentRepository.getCommentByPid(post.getPid()).size();
        List<Image> images = imageRepository.findByPid(pid);
        PostInfo postInfo = new PostInfo(post, likeCount, commentCount, userlist, images);
        UserAndPost userAndPost = new UserAndPost(postInfo, account);
        return new ResponseEntity<>(userAndPost, HttpStatus.OK);
    }

    @PostMapping("/{author}")
    @ApiOperation(value = "글쓰기")
    public Object create(@PathVariable String author, @RequestBody String jsonObj) throws ParseException {
        BasicResponse result = new BasicResponse();
        //json parsing 부분
        JSONParser jsonParse = new JSONParser();
        JSONObject obj = (JSONObject) jsonParse.parse(jsonObj);

        String token = (String) obj.get("token");
        if (!author.equals(tokenUtils.getUserNameFromToken(token))) {
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
        post.setPostno((postRepository.findByAuthor(author) == null ? 0 : postRepository.findByAuthor(author)) + 1);
        post.setAuthor(author);
        post.setTags(tags);
        postRepository.save(post);
        for (Object object : imageArray) {
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
        if (!author.equals(tokenUtils.getUserNameFromToken(token))) {
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
        for (Image image : images) {
            image.setPid(0);
            imageRepository.save(image);
        }
        if (!imageArray.isEmpty()) {
            for (Object object : imageArray) {
                JSONObject imageObject = (JSONObject) object;
                String str = (String) imageObject.get("image");
                Image image = imageRepository.findByIid(str);
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
        if ((findPost != null) && (tokenUtils.getUserNameFromToken(token).equals(author))) {
            postRepository.delete(findPost);
            result.data = SUCCESS;
            result.status = true;
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.data = "fail";
        result.status = false;
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }


    @GetMapping("/{author}/{pid}/likeit")
    @ApiOperation(value = "좋아요인지 아닌지")
    public int likeIt(@PathVariable String author, @PathVariable int pid, @RequestBody String jsonObj) throws ParseException {
        JSONParser jsonParse = new JSONParser();
        JSONObject obj = (JSONObject) jsonParse.parse(jsonObj);

        String token = (String) obj.get("token");
        String userName = tokenUtils.getUserNameFromToken(token);

        return likeRepository.findByPidAndUsername(pid, userName).getLikeit();
    }

    @PostMapping("/{author}/{pid}/likeit")
    @ApiOperation(value = "좋아요 클릭 / 0 : 좋아요x / 1 : 좋아요")
    public Object clickLike(@PathVariable String author, @PathVariable int pid, @RequestBody String jsonObj) throws ParseException {
        JSONParser jsonParse = new JSONParser();
        JSONObject obj = (JSONObject) jsonParse.parse(jsonObj);

        String token = (String) obj.get("token");
        String userName = tokenUtils.getUserNameFromToken(token);
        LikeInfo likeInfo = likeRepository.findByPidAndUsername(pid, userName);
        if (likeInfo == null) {
            likeInfo = new LikeInfo();
            likeInfo.setPid(pid);
            likeInfo.setUsername(userName);
            likeInfo.setLikeit(1);
            likeInfo.setPostusername(author);
        } else {
            likeInfo.setLikeit(likeInfo.getLikeit() == 0 ? 1 : 0);
        }
        likeRepository.save(likeInfo);
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = likeInfo;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/title")
    @ApiOperation(value = "제목 검색")
    public ResponseEntity<Object> searchTitle(@RequestParam String title) {
        BasicResponse result = new BasicResponse();
        List<Post> postList = postRepository.findByTitleContaining(title);
        List<PostInfo> postInfo = new ArrayList<>();
        if (postList.isEmpty()) {
            result.object = postInfo;
            result.status = false;
            result.data = "fail";
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        List<String> userlist;
        List<Image> images;
        for (Post p : postList) {
            userlist = likeRepository.findByPid(p.getPid());
            images = imageRepository.findByPid(p.getPid());
            int commentCnt = commentRepository.getCommentByPid(p.getPid()).size();
            postInfo.add(new PostInfo(p, userlist.size(), commentCnt, userlist, images));
        }
        return new ResponseEntity<>(postInfo, HttpStatus.OK);
    }

    @GetMapping("/search/content")
    @ApiOperation(value = "내용 검색")
    public ResponseEntity<Object> searchContent(@RequestParam String content) {
        BasicResponse result = new BasicResponse();
        List<Post> postList = postRepository.findByContentContaining(content);
        List<PostInfo> postInfo = new ArrayList<>();
        if (postList.isEmpty()) {
            result.object = postInfo;
            result.status = false;
            result.data = "fail";
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        List<String> userlist;
        List<Image> images;
        for (Post p : postList) {
            userlist = likeRepository.findByPid(p.getPid());
            images = imageRepository.findByPid(p.getPid());
            int commentCnt = commentRepository.getCommentByPid(p.getPid()).size();
            postInfo.add(new PostInfo(p, userlist.size(), commentCnt, userlist, images));
        }
        return new ResponseEntity<>(postInfo, HttpStatus.OK);
    }

    @GetMapping("/search/text")
    @ApiOperation(value = "제목 + 내용 검색")
    public ResponseEntity<Object> searchPost(@RequestParam String text) {
        BasicResponse result = new BasicResponse();
        List<Post> postList = postRepository.findByTitleContainingOrContentContaining(text, text);
        List<PostInfo> postInfo = new ArrayList<>();
        if (postList.isEmpty()) {
            result.object = postInfo;
            result.status = false;
            result.data = "fail";
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        List<String> userlist;
        List<Image> images;
        for (Post p : postList) {
            userlist = likeRepository.findByPid(p.getPid());
            images = imageRepository.findByPid(p.getPid());
            int commentCnt = commentRepository.getCommentByPid(p.getPid()).size();
            postInfo.add(new PostInfo(p, userlist.size(), commentCnt, userlist, images));
        }
        return new ResponseEntity<>(postInfo, HttpStatus.OK);
    }

    @GetMapping("/search/tag")
    @ApiOperation(value = "태그 검색")
    public ResponseEntity<Object> retrievePostbyTags(@RequestParam String tag) {
        BasicResponse result = new BasicResponse();
        List<Post> postList = postRepository.findByTagsContaining("," + tag + ",");
        List<PostInfo> postInfo = new ArrayList<>();
        if (postList.isEmpty()) {
            result.object = postInfo;
            result.status = false;
            result.data = "fail";
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        List<String> userlist;
        List<Image> images;
        for (Post p : postList) {
            userlist = likeRepository.findByPid(p.getPid());
            images = imageRepository.findByPid(p.getPid());
            int commentCnt = commentRepository.getCommentByPid(p.getPid()).size();
            postInfo.add(new PostInfo(p, userlist.size(), commentCnt, userlist, images));
        }
        return new ResponseEntity<>(postInfo, HttpStatus.OK);
    }

    @GetMapping("/{author}/achivement")
    public ResponseEntity<Object> getAchivement(@PathVariable String author) {
        Map<String, Integer> map = new HashMap<>();

        map.put("postCount", postRepository.getPostByAuthor(author).size());
        map.put("likemeCount", likeRepository.getLikeMeCount(author) == null ? 0 : likeRepository.getLikeMeCount(author));
        map.put("likeCount", likeRepository.getLikePostCount(author) == null ? 0 : likeRepository.getLikePostCount(author));
        map.put("mostLike", likeRepository.getMostLikes(author) == null ? 0 : likeRepository.getMostLikes(author));

        return new ResponseEntity<>(map, HttpStatus.OK);
    }


}
