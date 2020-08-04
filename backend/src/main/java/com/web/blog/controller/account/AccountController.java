package com.web.blog.controller.account;

import com.web.blog.enums.role.AccountGrade;
import com.web.blog.model.BasicResponse;

import com.web.blog.model.account.Account;
import com.web.blog.service.account.AccountService;
import com.web.blog.utils.TokenUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@RequestMapping("/user")
@RestController
public class AccountController {
  @Autowired
  TokenUtils tokenUtils;
  @Autowired
  AccountService accountService;
  @GetMapping("/signup")
  @ApiOperation(value = "회원 가입")
  public String signup(HttpServletRequest httpServletRequest){
    try {
      Account account = new Account();
      account.setUseremail(httpServletRequest.getHeader("useremail"));
      account.setPassword(httpServletRequest.getHeader("password"));
      account.setUsername(httpServletRequest.getHeader("nickname"));
      System.out.println(account.getUsername());
      account.setGrade(AccountGrade.USER);
      accountService.createNew(account);
    }catch (Exception e){
      e.printStackTrace();
      return "signup fail";
    }
    return "signup success";
  }
}