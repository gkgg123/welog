package com.web.blog.controller.post;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.web.blog.model.account.Account;
import com.web.blog.model.account.repository.AccountRepository;
import com.web.blog.model.post.repository.CommentRepository;
import com.web.blog.model.BasicResponse;

import com.web.blog.model.post.Comment;
import com.web.blog.model.post.CommentInfo;
import com.web.blog.utils.TokenUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
@RequestMapping("/post/{pid}/comment")
@RestController
public class CommentController {
    String SUCCESS = "success";

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TokenUtils tokenUtils;

    @GetMapping("/")
    @ApiOperation(value = "해당 글 모든 댓글 조회")
    public Object retrieveComment(@PathVariable int pid) throws Exception{
        List<CommentInfo> commentInfoList = new ArrayList<>();

        List<Comment> comments = commentRepository.getCommentByPid(pid);
        for(Comment c : comments){
            Account account = accountRepository.findByUsername(c.getName());
            commentInfoList.add(new CommentInfo(c, account.getProfileUrl()));
        }
        return new ResponseEntity<>(commentInfoList,HttpStatus.OK);
    }

    @PostMapping("/")
    @ApiOperation(value = "댓글 작성")
    public Object createComment(@PathVariable int pid, @RequestBody String jsonObj) throws ParseException{
        BasicResponse result = new BasicResponse();
        Comment comment = new Comment();
        //json parsing 부분
        JSONParser jsonParse = new JSONParser();
        JSONObject obj = (JSONObject) jsonParse.parse(jsonObj);

        String token = (String) obj.get("token");
        JSONArray commentArray = (JSONArray) obj.get("comment");
        JSONObject commentObject = (JSONObject) commentArray.get(0);


        String content = (String) commentObject.get("content");
        int secret = Integer.parseInt((String) commentObject.get("secret"));
        String name = tokenUtils.getUserNameFromToken(token);

        comment.setPid(pid);
        comment.setContent(content);
        comment.setSecret(secret);
        comment.setName(name);

        System.out.println(comment.toString());
        commentRepository.save(comment);
        result.status = true;
        result.data = "SUCCESS";
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PutMapping("/{cid}")
    @ApiOperation(value = "댓글 수정")
    public Object update(@PathVariable int cid, @PathVariable int pid, @RequestBody String jsonObj) throws ParseException {

        BasicResponse result = new BasicResponse();
        Comment comment = commentRepository.getCommentByCid(cid);

        //json parsing 부분
        JSONParser jsonParse = new JSONParser();
        JSONObject obj = (JSONObject) jsonParse.parse(jsonObj);

        String token = (String) obj.get("token");
        if(!tokenUtils.getUserNameFromToken(token).equals(comment.getName())){
            result.status = false;
            result.data = "fail";
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }
        JSONArray commentArray = (JSONArray) obj.get("comment");
        JSONObject commentObject = (JSONObject) commentArray.get(0);

        String content = (String) commentObject.get("content");
        int secret = Integer.parseInt((String) commentObject.get("secret"));

        comment.setContent(content);
        comment.setSecret(secret);
        commentRepository.save(comment);
        result.status = true;
        result.data = SUCCESS;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{cid}")
    @ApiOperation(value = "글 삭제")

    public Object delete(@Valid @PathVariable int cid, @RequestBody String jsonObj) throws ParseException {
        Comment findComment = commentRepository.getCommentByCid(cid);
        BasicResponse result = new BasicResponse();
        JSONParser jsonParse = new JSONParser();
        JSONObject obj = (JSONObject) jsonParse.parse(jsonObj);

        String token = (String) obj.get("token");
        String name = commentRepository.getCommentByCid(cid).getName();
        if((findComment != null) && (tokenUtils.getUserNameFromToken(token).equals(name))){
            commentRepository.delete(findComment);
            result.data = SUCCESS;
            result.status = true;
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.data = "fail";
        result.status = false;
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }

}
