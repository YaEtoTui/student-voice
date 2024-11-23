package ru.urfu.sv.studentvoice.controllers.user;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.model.domain.dto.user.UserInfoDto;
import ru.urfu.sv.studentvoice.services.user.UserService;

@RestController
@RequestMapping(Links.BASE_API + Links.USERS)
public class UsersApiController {

    @Autowired
    private UserService userService;

//    @PostMapping("create-from-file")
//    @Parameters(value = {
//            @Parameter(name = "file", required = true)
//    })
//@PreAuthorize("@RolesAC.isAdmin()")
//    public ResponseEntity<Map<String, Object>> uploadUsers(@RequestParam("file") MultipartFile file) throws IOException {
//        ExtendedModelMap model = new ExtendedModelMap();
//        usersController.uploadUsers(file, model);
//
//        ActionResultResponse result = ActionResultResponse.fromActionResult(model.getAttribute(RESULT));
//        if (!result.isSuccess()) {
//            return ResponseEntity.ok().body(Map.of(RESULT, result));
//        }
//
//        String encodedFile = (String) model.getAttribute(CREATED_USERS);
//        return ResponseEntity.ok().body(Map.of(RESULT, result, "createdUsersFile", orNull(encodedFile)));
//    }

    @Operation(summary = "Создание пользователя с ролью")
    @RequestMapping(path = "create", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody UserInfoDto userInfoDto) {
        userService.createUser(userInfoDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//    @GetMapping("list")
//    @PreAuthorize("@RolesAC.isAdmin()")
//    public ResponseEntity<Map<String, Object>> usersList(HttpServletRequest request) {
//        ExtendedModelMap model = new ExtendedModelMap();
//        usersController.usersPage(request, model);
//        return ResponseEntity.ok().body(Map.of("users", orNull(model.getAttribute("users"))));
//    }

//    @PostMapping("update")
//    @Parameters(value = {
//            @Parameter(name = "username", in = ParameterIn.QUERY, required = true),
//            @Parameter(name = "password", in = ParameterIn.QUERY),
//            @Parameter(name = "professorName", in = ParameterIn.QUERY),
//    })
//    @PreAuthorize("@RolesAC.isAdmin()")
//    public ResponseEntity<Map<String, Object>> updateUser(HttpServletRequest request) {
//        ExtendedModelMap model = new ExtendedModelMap();
//        usersController.updateUser(request, model);
//
//        ActionResultResponse result = ActionResultResponse.fromActionResult(model.getAttribute(RESULT));
//        return ResponseEntity.ok().body(Map.of(RESULT, result));
//    }

//    @PostMapping("delete")
//    @Parameters(value = {
//            @Parameter(name = "username", in = ParameterIn.QUERY, required = true)
//    })
//    @PreAuthorize("@RolesAC.isAdmin()")
//    public ResponseEntity<Map<String, Object>> deleteUser(HttpServletRequest request) {
//        ExtendedModelMap model = new ExtendedModelMap();
//        usersController.deleteUser(request.getParameter(USERNAME), model);
//
//        ActionResultResponse result = ActionResultResponse.fromActionResult(model.getAttribute(RESULT));
//        return ResponseEntity.ok().body(Map.of(RESULT, result));
//    }
}