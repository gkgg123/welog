package com.web.blog.controller.account;

import com.web.blog.model.BasicResponse;

import com.web.blog.model.account.Account;
import com.web.blog.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.security.Principal;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "http://127.0.0.1:3000" })
@RestController
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping("/signup")
    public String processingSignUp(@ModelAttribute Account account){
        account.setGrade("USER");
        accountService.createNew(account);
        return "redirect:/";
    }

}