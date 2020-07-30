package com.web.blog.controller.account;

import com.web.blog.annotation.account.CurrentUser;
import com.web.blog.enums.role.AccountGrade;
import com.web.blog.model.account.Account;
import com.web.blog.service.account.AccountService;
import com.web.blog.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/user")
public class WSSampleController {
}
