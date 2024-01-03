package vn.com.ocb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.com.ocb.model.response.BaseResponse;
import vn.com.ocb.model.response.BaseResponseBuilder;
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
    BaseResponse<List<UserResponse>> getAllUsers() {
        GetAllUserRequest request = new GetAllUserRequest();
        try {
            return BaseResponseBuilder.buildSuccessResponse(
                    request.getRequestId(), requestPipeline.send(request).join());
        } catch (Exception ex) {
            ex.printStackTrace();
            return BaseResponseBuilder.buildErrorResponse(request.getRequestId(), ex);
        }
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    BaseResponse<UserDetailResponse> getUser(@PathVariable("id") String id) {
        GetUserRequest request = new GetUserRequest(id);
        try {
            return BaseResponseBuilder.buildSuccessResponse(
                    request.getRequestId(),
                    requestPipeline.send(request).join());
        } catch (Exception ex) {
            ex.printStackTrace();
            return BaseResponseBuilder.buildErrorResponse(request.getRequestId(), ex);
        }
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    BaseResponse<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            return BaseResponseBuilder.buildSuccessResponse(
                    request.getRequestId(),
                    requestPipeline.send(request).join());
        } catch (Exception ex) {
            ex.printStackTrace();
            return BaseResponseBuilder.buildErrorResponse(request.getRequestId(), ex);
        }
    }

    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    BaseResponse<UserResponse> updateUser(@PathVariable("id") String id, @RequestBody UpdateUserRequest request) {
        request.setUserId(id);
        try {
            return BaseResponseBuilder.buildSuccessResponse(
                    request.getRequestId(),
                    requestPipeline.send(request).join());
        } catch (Exception ex) {
            ex.printStackTrace();
            return BaseResponseBuilder.buildErrorResponse(request.getRequestId(), ex);
        }
    }

    @GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    BaseResponse<UserDetailResponse> userDetailResponse(@RequestBody LoginRequest request) {
        try {
            return BaseResponseBuilder.buildSuccessResponse(
                    request.getRequestId(),
                    requestPipeline.send(request).join());
        } catch (Exception ex) {
            ex.printStackTrace();
            return BaseResponseBuilder.buildErrorResponse(request.getRequestId(), ex);
        }
    }

}
