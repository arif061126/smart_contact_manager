package com.smartContactManager.controller;

import com.smartContactManager.entity.Contact;
import com.smartContactManager.entity.User;
import com.smartContactManager.helper.Message;
import com.smartContactManager.repository.ContactRepository;
import com.smartContactManager.repository.UserRepository;
import com.smartContactManager.service.ContactService;
import com.smartContactManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class NormalUserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactService contactService;

    /**
     *
     * @param model
     * @param principal
     */
    @ModelAttribute
    public void addCommonData(Model model, Principal principal){
        //this will take user name from login
        String name = principal.getName();
        System.out.println(name);

        //with the help of user name we will fetch user details from db:
        User user = userRepository.getUserByEmail(name);
        System.out.println(user);

        //now we will send all user credentials to frontend
        model.addAttribute(user);
    }

    @GetMapping("/dashboard")
    public String userDashboard(Model model, Principal principal){
        model.addAttribute("title", "User Dashboard - Smart Contact Manager");
        return "user/user_dashboard";
    }

    //open add form handler
    @GetMapping("/add_contact")
    public String openAddContactForm(Model model){
        //to send something to html page:
        model.addAttribute("title","Add Contact - Smart Contact Manager");
        model.addAttribute("contact", new Contact());

        return "user/add_contact";
    }

    //process-contact form
    @PostMapping("/process-contact")
    public String processContact(
            @Valid
            @ModelAttribute("contact") Contact contact,
            BindingResult result,
            @RequestParam("profileImage") MultipartFile file,
            Principal principal,
            HttpSession session){

        /**
         * The name attribute you have given for <input> in html file for type file should be
         * different than "image" field in your entities java file.
         */

        /*String userName = principal.getName();
        System.out.println(userName);
        User user = userRepository.getUserByEmail(userName);*/

        if(result.hasErrors()){
            System.out.println(result.toString());
            return "user/add_contact";
        }

        try {
            //we will get name => who has logged in
            String userName = principal.getName();
            System.out.println(userName);
            User user = userRepository.getUserByEmail(userName);

            //processing and uploading file:
            if(file.isEmpty()){
                //if file is empty then try this message:
                System.out.println("File is empty!!!");
                contact.setImage("user_icon.png");
                session.setAttribute("message", new Message("File is empty!", "alert-warning"));

            }else{
                //upload the file to the folder
                contact.setImage(file.getOriginalFilename());
                //where to upload?=> find the path
                File saveFile = new ClassPathResource("static/img").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath()
                        + File.separator
                        + file.getOriginalFilename());

                // and update the file name to the contact
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image uploaded successfully");
            }

            System.out.println(contact.getFirstName());
            System.out.println(contact.getLastName());
            System.out.println(contact.getEmail());

            //System.out.println(user.getContacts());

            //we have to give contact to user
            user.getContacts().add(contact);

            //before we have to attach user to contact (bidirectional mapping)
            contact.setUser(user);


            userRepository.save(user);

            System.out.println("Contact added successfully!");
            session.setAttribute("message", new Message("Contact added successfully.", "alert-success"));

        }catch (Exception e){
            System.out.println(e.getMessage());
            session.setAttribute("message", new Message("Error with : "+e.getMessage(), "alert-danger"));
        }

        return "user/add_contact";
    }

    //pagination per page = 4[n]
    //current page = 0 [page]
    //pathvariable
    //@GetMapping("/view_contact/{page}")
    @GetMapping("/view_contact")
    public String viewContacts(
            //@PathVariable("page") Integer page,
            @PageableDefault(size = 4, direction = Sort.Direction.DESC, page = 0, value = 4) Pageable pageable,
            Model model,
            Principal principal){

        model.addAttribute("title","View Contact - Smart Contact Manager");
        String userName = principal.getName();
        User user = this.userRepository.getUserByEmail(userName);
        Long userId = user.getId();
        //List<Contact> contacts = user.getContacts();

        //System.out.println(contacts);

        Page<Contact> contacts = this.contactRepository.findContactsByUser(userId, pageable);

        //alternative way:
        //List<Contact> contacts = this.contactRepository.findContactByUser(users);

        //Pageable pageable = PageRequest.of(page, 3);

        System.out.println(contacts);

        //sending contacts to frontend:
        model.addAttribute("contacts",contacts);
        model.addAttribute("currentPage",contacts.getNumber());
        model.addAttribute("totalPage", contacts.getTotalPages());

        /*System.out.println(contacts.getTotalElements());
        System.out.println(contacts.get());
        System.out.println(contacts.getContent());
        System.out.println(contacts.getNumber());
        System.out.println(contacts.getTotalPages());
        System.out.println(contacts.getPageable());
        System.out.println(contacts.getSize());
        System.out.println(contacts.getClass());*/


        return "user/view_contact";
    }

    @GetMapping("/{cId}/contact_detail")
    public String viewContactDetails(
            @PathVariable("cId") Long cId,
            Model model,
            Principal principal){

        Contact contact = this.contactRepository.findById(cId).get();

        String userName = principal.getName();
        User user = this.userRepository.getUserByEmail(userName);


        model.addAttribute("title","Contact Detail - Smart Contact Manager");

        if(user.getId()==contact.getUser().getId()){
            model.addAttribute("contact",contact);
        }else {
            model.addAttribute("message", "You are not authorized user!!!");
        }

        return "user/contact_detail";
    }

    @GetMapping ("/delete/{cId}")
    //@Transactional
    public String deleteContactById(
            @PathVariable(value = "cId") Long cId,
            //Model model,
            //Principal principal,
            HttpSession session){

        this.contactService.deleteContactById(cId);

        /*String userName = principal.getName();

        User user = this.userRepository.getUserByEmail(userName);

        Contact contact = this.contactRepository.findById(cId).get();

        if(user.getId()==contact.getUser().getId()){*/
            /**
             * as contact is cascaded and mapped by user
             * so before delete we have to unlink contact from user
             */
            //contact.setUser(null);
            //this.contactRepository.deleteById(cId);
            /*user.getContacts().remove(contact);

            this.userRepository.save(user);*/

            session.setAttribute("message", new Message("Contact has been deleted successfully.", "alert-success"));
            return "redirect:/user/view_contact";
        /*}else {
            session.setAttribute("message", new Message("You are not authorized!!!", "alert-danger"));
            return "redirect:/user/view_contact";
        }*/
    }

}
