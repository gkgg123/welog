package com.web.blog.controller.account;

import com.web.blog.common.CurrentUser;
import com.web.blog.model.account.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WSSampleController {

  @GetMapping("/")
  public String index(Model model, @CurrentUser Account account) {
    if (account == null) {
      model.addAttribute("message", "Hello Spring Security");
    } else {
      model.addAttribute("message", "Hello, " + account.getUseremail());
    }
    return "index";
  }

  @GetMapping("/login")
  public String loginForm() {return "login";}
  @GetMapping("/logout")
  public String logoutForm() {return "logout";}

  @GetMapping("/signup")
  public String signupForm(Model model){
    model.addAttribute("account", new Account());
    return "signup";
  }
}
