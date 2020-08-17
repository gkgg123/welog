package com.web.blog.controller.account;

import com.web.blog.enums.AccountGrade;
import com.web.blog.enums.ErrorCode;
import com.web.blog.exception.account.PasswordNotMatchedException;
import com.web.blog.model.BasicResponse;

import com.web.blog.model.account.Account;
import com.web.blog.property.JwtProperties;
import com.web.blog.service.account.AccountService;
import com.web.blog.utils.TokenUtils;
import io.swagger.annotations.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@RequestMapping("/user")
@RestController
public class AccountController {
  @Autowired
  AccountService accountService;

  @Autowired
  TokenUtils tokenUtils;

  @RequestMapping(value = "/signup", method = RequestMethod.POST)
  @ApiOperation(value = "회원 가입")
  public String signup(@RequestBody Account account){
    account.setGrade(AccountGrade.USER);
    accountService.createNew(account);

    return "signup success";
  }

  @PutMapping("/pwchange")
  @ApiOperation(value = "패스워드 변경입니다. 헤더에 jwt토큰도 보내주세용")
  @ApiImplicitParams({
          @ApiImplicitParam(name = "originalPassword", value = "원래 비밀번호", required = true,
                  dataType = "string", paramType = "query", defaultValue = "" ),
          @ApiImplicitParam(name = "changePassowrd", value = "바꿀 비밀번호" , required = true,
                  dataType = "string", paramType = "query", defaultValue = "")
  })
  public ResponseEntity<BasicResponse> changePassword(
          @RequestParam(value = "originalPassword", required = true) String originPw,
          @RequestParam(value = "changePassowrd", required = true) String chgPw,
          @RequestHeader(value = JwtProperties.HEADER_STRING, required = true) String jwt) throws ParseException {
    BasicResponse response = new BasicResponse();

    if(!accountService.checkPassword(originPw, chgPw, tokenUtils.getUserNameFromToken(jwt))){
      throw new PasswordNotMatchedException(ErrorCode.PASSWORD_NOT_MATCHED);
    }
    response.status = true;
    response.data = "Password changed Successfully";

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}