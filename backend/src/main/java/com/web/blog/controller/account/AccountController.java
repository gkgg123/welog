package com.web.blog.controller.account;

import com.web.blog.config.S3Service;
import com.web.blog.enums.AccountGrade;
import com.web.blog.model.BasicResponse;

import com.web.blog.model.account.Account;
import com.web.blog.model.account.repository.AccountRepository;
import com.web.blog.model.file.Image;
import com.web.blog.model.file.repository.ImageRepository;
import com.web.blog.service.account.AccountService;
import com.web.blog.utils.TokenUtils;
import io.swagger.annotations.ApiOperation;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;

@ApiResponses(value = {@ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class)})

@RequestMapping("/user")
@RestController
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ImageRepository imageRepository;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ApiOperation(value = "회원 가입")
    public String signup(@RequestBody Account account) {
        account.setGrade(AccountGrade.USER);
        accountService.createNew(account);

        return "signup success";
    }

    @GetMapping(value = "/{author}")
    public Account getAccount(@PathVariable String author){
        return accountRepository.findByUsername(author);
    }

    @PostMapping(value = "/{author}/profile")
    public Object inputProfile(@PathVariable String author, @RequestPart List<MultipartFile> files) throws IOException, ParseException {
        S3Service s3 = new S3Service();

        BasicResponse result = new BasicResponse();
        Account account = accountRepository.findByUsername(author);
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String iid = author + "_" + fileName;
            Image image = imageRepository.findByIid(iid);
            if(image == null)
                image = new Image();
            String path = s3.upload(file, iid);
            account.setProfileUrl(path);

            image.setIid(iid);
            image.setIname(fileName);
            image.setPath(path);
            image.setPid(-1);
            imageRepository.save(image);

            result.data = "success";
            result.status = true;
            result.object = image;

        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @DeleteMapping(value = "/{author}/profile")
    public Object deleteProfile(@PathVariable String author){
        BasicResponse result = new BasicResponse();
        Account account = accountRepository.findByUsername(author);

        Image image = imageRepository.findByPath(account.getProfileUrl());
        if(image != null)
            imageRepository.delete(image);
        account.setProfileUrl("no_img");
        result.data = "success";
        result.status = true;
        result.object = null;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/{author}/description")
    public Object inputDescription(@PathVariable String author, @RequestBody String description){
        BasicResponse result = new BasicResponse();

        Account account = accountRepository.findByUsername(author);
        account.setUserDescription(description);
        accountRepository.save(account);

        result.data = "success";
        result.status = true;
        result.object = account;

        return new ResponseEntity<>(result, HttpStatus.OK);

    }


}