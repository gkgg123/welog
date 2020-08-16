package com.web.blog.controller.post;

import com.web.blog.model.BasicResponse;


import com.web.blog.model.account.repository.PostRepository;
import com.web.blog.model.post.Mrcomment;
import com.web.blog.model.post.repository.ModifyRequestCommentRepository;
import com.web.blog.service.post.ModifyRequestService;
import com.web.blog.utils.TokenUtils;
import io.swagger.annotations.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@Controller
@RequestMapping("/modfrequest")
public class ModifyRequestController {

  @Autowired
  ModifyRequestService service;

  @Autowired
  PostRepository postRepository;

  @Autowired
  ModifyRequestCommentRepository repository;

  @Autowired
  TokenUtils tokenUtils;

  @GetMapping("/post/{pid}")
  @ApiOperation("해당 pid의 모든 comment 호출")
  public ResponseEntity<List<Mrcomment>> getCommentsByPost(@PathVariable int pid){
    return new ResponseEntity<List<Mrcomment>>(repository.findAllByPid(pid),HttpStatus.OK);
  }

  @PostMapping("/post/{pid}")
  @ApiOperation(value = "수정 요청")
  @ApiImplicitParams({@ApiImplicitParam(name = "pid", value = "게시물 id", required = true),
          @ApiImplicitParam(name = "jsonObj",
                  value = "수정 요청할 스트링 (requiredString), 요청 내용 (comment), jwt (Authorization)을 json으로",
                  required = true)
  })
  public ResponseEntity<BasicResponse> makeComment(@PathVariable int pid, @RequestBody String jsonObj) throws ParseException {
    BasicResponse response = new BasicResponse();

    JSONParser jsonParser = new JSONParser();
    JSONObject object = (JSONObject) jsonParser.parse(jsonObj);
    System.out.println(object.toJSONString());
    String jwt = (String) object.get("Authorization");
    String strcomment = (String) object.get("comment");
    String rString = (String) object.get("requiredString");

    Mrcomment comment = new Mrcomment();
    comment.setPid(pid);
    comment.setPwriter(postRepository.findByPid(pid).getAuthor());
    comment.setCwriter(tokenUtils.getUserNameFromToken(jwt));
    comment.setRstring(rString);
    comment.setRcomment(strcomment);

    service.createNew(comment);

    response.status = true;
    response.data = "SUCCESS";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/comment/{cid}")
  @ApiOperation(value = "특정 코멘트만 가져옴, 해당 글을 읽었을 시 ischecked 는 true로")
  public ResponseEntity<Mrcomment> getComment(@PathVariable int cid, @RequestBody String jsonObj){
    Mrcomment comment = repository.findByCid(cid);

    comment.setIschecked(true);
    repository.save(comment);

    return new ResponseEntity<Mrcomment>(comment,HttpStatus.OK);

  }

  @GetMapping("/author")
  @ApiOperation(value = "블로그 주인의 모든 수정 요청을 불러옴")
  public ResponseEntity<List<Mrcomment>> getCommentList(@RequestBody String jsonObj) throws ParseException {
    JSONParser jsonParser = new JSONParser();
    JSONObject object = (JSONObject) jsonParser.parse(jsonObj);

    String jwt = (String) object.get("Authorization");

    return new ResponseEntity<List<Mrcomment>>
            (repository.findAllByPwriter(tokenUtils.getUserNameFromToken(jwt)),HttpStatus.OK);
  }



}
