package vn.com.ocb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.com.ocb.pipeline.request.RequestPipeline;
import vn.com.ocb.usecase.auth.model.request.LoginRequest;
import vn.com.ocb.usecase.user.model.request.CreateUserRequest;
import vn.com.ocb.usecase.user.model.request.GetAllUserRequest;
import vn.com.ocb.usecase.user.model.request.GetUserRequest;
import vn.com.ocb.usecase.user.model.request.UpdateUserRequest;
import vn.com.ocb.usecase.user.model.response.UserDetailResponse;
import vn.com.ocb.usecase.user.model.response.UserResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final RequestPipeline requestPipeline;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    List<UserResponse> getAllUsers() {
        GetAllUserRequest request = new GetAllUserRequest();
        return requestPipeline
                .send(request)
                .join();
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDetailResponse getUser(@PathVariable("id") String id) {
        GetUserRequest request = new GetUserRequest(id);
        return requestPipeline.send(request)
                .join();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    UserResponse createUser(@RequestBody CreateUserRequest request) {
        return requestPipeline.send(request)
                .join();
    }

    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserResponse updateUser(@PathVariable("id") String id, @RequestBody UpdateUserRequest request) {
        request.setUserId(id);
        return requestPipeline
                .send(request)
                .join();
    }

    @GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDetailResponse userDetailResponse(@RequestBody LoginRequest request) {
        return requestPipeline.send(request)
                .join();
    }

}
