package com.smartContactManager.controller;

import com.smartContactManager.entity.User;
import com.smartContactManager.helper.Message;
import com.smartContactManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

//@RestController
//@RequestMapping("/smart-contact")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("title", "About - Smart Contact Manager");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("title", "Register - Smart Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    //handling registering user:

    /**
     * Model is used to send data
     * ModelAttribute is used to receive the data (as object)
     * RequestParam is used to receive the data
     * @param user
     * @param checkbox
     * @return
     */
    @PostMapping("/do_register")
    public String registerUser(
            @Valid
            @ModelAttribute("user") User user,
            @RequestParam(value = "checkbox", defaultValue = "false") Boolean checkbox,
            Model model,
            BindingResult bindingResult,
            HttpSession httpSession){

        try {
            if(!checkbox){
                System.out.println("you have not agreed terms and conditions!!!");
                throw new Exception("you have not agreed terms and conditions!!!");
            }

            if(bindingResult.hasErrors()){
                System.out.println("Error"+bindingResult.toString());
                model.addAttribute("user",user);
                return "signup";
            }

            user.setRole("NORMAL");
            user.setEnabled(true);
            user.setImageUrl("default.png");

            /**
             * to save user:
             * we need repository object
             *
             */

            System.out.println(user);
            System.out.println(checkbox);

            User addUser = this.userService.addUser(user);

            //to send the user data as it is:
            model.addAttribute("user",new User());
            httpSession.setAttribute("message",new Message("Successfully registered!","alert-success"));
            return "signup";

        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("user",user);
            httpSession.setAttribute("message",new Message("Something went wrong!"+e.getMessage(),"alert-danger"));
            return "signup";
        }

        //return "signup";
    }
}
