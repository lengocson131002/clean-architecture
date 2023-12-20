package vn.com.rest.handler;

import an.awesome.pipelinr.Pipeline;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;
import vn.com.ocb.usecase.auth.model.LoginRequest;
import vn.com.ocb.usecase.user.model.*;
import vn.com.rest.utils.ResponseHelper;

import javax.inject.Inject;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserHandler {

    private final Pipeline pipeline;

    public void handleLoginUser(RoutingContext rc) {
        JsonObject reqBody = rc.body().asJsonObject();
        String email = reqBody.getString("email");
        String password = reqBody.getString("password");
        UserDetailResponse response = pipeline.send(
                LoginRequest.builder()
                        .email(email)
                        .password(password)
                        .build());

        ResponseHelper.sendJson(rc, response);
    }

    public void handleGetAllUsers(RoutingContext rc) {
        List<UserResponse> res = pipeline.send(new GetAllUserRequest());
        JsonArray usersResponse = new JsonArray(res);
        ResponseHelper.sendJson(rc, usersResponse);
    }

    public void handleGetUserById(RoutingContext rc) {
        String id = rc.request().getParam("userId");
        UserDetailResponse res = pipeline.send(new GetUserRequest(id));
        ResponseHelper.sendJson(rc, res);
    }

    public void handleUpdateUser(RoutingContext rc) {
        String userId = rc.request().getParam("userId");
        JsonObject reqBody = rc.body().asJsonObject();

        UpdateUserRequest request = UpdateUserRequest.builder()
                .userId(userId)
                .email(reqBody.getString("email"))
                .password(reqBody.getString("password"))
                .firstName(reqBody.getString("firstName"))
                .lastName(reqBody.getString("lastName"))
                .build();

        UserResponse res = pipeline.send(request);
        ResponseHelper.sendJson(rc, res);
    }

    public void handleCreateUser(RoutingContext rc) {
        JsonObject reqBody = rc.body().asJsonObject();
        CreateUserRequest request = CreateUserRequest.builder()
                .email(reqBody.getString("email"))
                .password(reqBody.getString("password"))
                .firstName(reqBody.getString("firstName"))
                .lastName(reqBody.getString("lastName"))
                .build();

        UserResponse res = pipeline.send(request);
        ResponseHelper.sendJson(rc, res);
    }
}
