package com.web.blog.controller.post;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.web.blog.dao.post.CommentDao;
import com.web.blog.model.BasicResponse;

import com.web.blog.model.post.Comment;
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
    CommentDao commentDao;

    @GetMapping("/")
    @ApiOperation(value = "해당 글 모든 댓글 조회")
    public ResponseEntity<List<Comment>> retrieveComment(@PathVariable int pid) throws Exception{
        return new ResponseEntity<List<Comment>>(commentDao.getCommentByPid(pid),HttpStatus.OK);
    }

    @PostMapping("/create")
    @ApiOperation(value = "댓글 작성")
    public Object createComment(@PathVariable int pid, @RequestBody Comment comment){
        BasicResponse result = new BasicResponse();
        comment.setPid(pid);
        commentDao.save(comment);
        result.status = true;
        result.data = "SUCCESS";
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PutMapping("/update/{cid}")
    @ApiOperation(value = "댓글 수정")
    public Object update(@RequestBody Comment comment, @PathVariable int cid, @PathVariable int pid){

        BasicResponse result = new BasicResponse();
        comment.setPid(pid);
        comment.setCid(cid);
        commentDao.save(comment);
        result.status = true;
        result.data = SUCCESS;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{cid}")
    @ApiOperation(value = "글 삭제")

    public Object delete(@Valid @PathVariable int cid) {
        Comment findComment = commentDao.getCommentByCid(cid);
        BasicResponse result = new BasicResponse();
        if(findComment != null){
            commentDao.delete(findComment);
            result.data = SUCCESS;
            result.status = true;
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.data = "fail";
        result.status = false;
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }

}
