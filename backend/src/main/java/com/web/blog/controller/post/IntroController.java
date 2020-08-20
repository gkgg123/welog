package com.web.blog.controller.post;

import com.web.blog.model.BasicResponse;
import com.web.blog.model.post.repository.IntroRepository;
import com.web.blog.model.post.Intro;
import com.web.blog.utils.TokenUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@RequestMapping("/post/{author}")
@RestController
public class IntroController {

    @Autowired
    IntroRepository introRepository;
    @Autowired
    TokenUtils tokenUtils;

    @PostMapping("/intro")
    @ApiOperation(value = "소개글 작성 및 수정")
    public Object createIntro(@PathVariable String author, @RequestBody String jsonObj) throws ParseException {
        JSONParser jsonParse = new JSONParser();
        JSONObject obj = (JSONObject) jsonParse.parse(jsonObj);

        String token = (String) obj.get("token");
        try{
            if(!author.equals(tokenUtils.getUserNameFromToken(token))){
                return "Not Match";
            }
        Intro intro = introRepository.findByAuthor(author);
        if(intro == null) {
            intro = new Intro();
            intro.setAuthor(author);
        }
        JSONArray introArray = (JSONArray) obj.get("intro");
        JSONObject introObject = (JSONObject) introArray.get(0);
        String content = (String) introObject.get("content");

        intro.setContent(content);
        introRepository.save(intro);

        return intro;

        } catch (Exception e){
            return "Invalid Token";
        }
    }

    @GetMapping("/intro")
    @ApiOperation(value = "소개글 조회")
    public Intro retrieveIntro(@PathVariable String author){
        return introRepository.findByAuthor(author);
    }
}
