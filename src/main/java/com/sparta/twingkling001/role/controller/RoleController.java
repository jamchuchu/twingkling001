package com.sparta.twingkling001.role.controller;


import com.sparta.twingkling001.api.response.ApiResponse;
import com.sparta.twingkling001.api.response.SuccessType;
import com.sparta.twingkling001.role.entity.Role;
import com.sparta.twingkling001.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

//    @GetMapping("/all")
//    public ResponseEntity<ApiResponse<List<Tag>>> getAllTags(){
//        ApiResponse<List<Tag>> response = ApiResponse.successOf(tagService.getAllTags());
//        return ResponseEntity.ok(response);
//    }

    @GetMapping()
    public ResponseEntity<ApiResponse<Role>> getRole() {
        Role response = roleService.getRole(1);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

}
