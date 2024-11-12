package ru.urfu.sv.studentvoice.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.urfu.sv.studentvoice.utils.consts.AdminPaths;
import ru.urfu.sv.studentvoice.utils.result.ActionResult;
import ru.urfu.sv.studentvoice.utils.consts.Roles;
import ru.urfu.sv.studentvoice.model.domain.entity.User;
import ru.urfu.sv.studentvoice.services.UserService;
import ru.urfu.sv.studentvoice.utils.result.ActionResultFactory;

import static ru.urfu.sv.studentvoice.utils.consts.Parameters.*;
import static ru.urfu.sv.studentvoice.utils.consts.Templates.*;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @PreAuthorize("@AuthoritiesAC.isAdmin()")
    @GetMapping(value = {"/admin", "/admin-home"})
    public String adminHome(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("adminPaths", AdminPaths.getPaths());
        model.addAttribute(USERNAME, userDetails.getUsername());
        return ADMIN_HOME;
    }

    @GetMapping("/admin/create-first")
    public String createFirstAdminPage(Model model) {
        if (userService.isAnyAdminExists()) {
            model.addAttribute(RESULT, ActionResultFactory.firstAdminExist());
        }
        return CREATE_FIRST_ADMIN;
    }

//    @PostMapping("/admin/create-first")
//    public String createFirstAdmin(HttpServletRequest request, Model model) {
//        if (userService.isAnyAdminExists()) {
//            model.addAttribute(RESULT, ActionResultFactory.firstAdminExist());
//            return CREATE_FIRST_ADMIN;
//        }
//
//        User user = User.builder()
//                .username(request.getParameter(USERNAME))
//                .password(request.getParameter(PASSWORD))
//                .role(Roles.ADMIN)
//                .build();
//
//        ActionResult result = userService.createUser(user);
//
//        model.addAttribute(RESULT, result);
//        return CREATE_FIRST_ADMIN;
//    }
}