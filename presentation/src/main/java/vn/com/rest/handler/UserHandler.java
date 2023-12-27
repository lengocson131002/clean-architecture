package vn.com.rest.handler;

import an.awesome.pipelinr.Pipeline;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;
import vn.com.ocb.adapter.config.KafkaRequestNotifier;
import vn.com.ocb.config.RequestNotifier;
import vn.com.ocb.exception.ResponseCode;
import vn.com.ocb.model.BaseResponse;
import vn.com.ocb.usecase.auth.model.LoginRequest;
import vn.com.ocb.usecase.user.model.*;
import vn.com.ocb.usecase.user.v2.model.GetAllUserRequestV2;
import vn.com.rest.utils.ResponseHelper;

import javax.inject.Inject;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserHandler {

    private final Pipeline pipeline;

//    private final RequestNotifier notifier;

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
        RequestNotifier<GetAllUserRequestV2, List<UserResponse>> requestNotifier = new KafkaRequestNotifier<>(GetAllUserRequestV2.class);
        requestNotifier.send(new GetAllUserRequestV2())
                .whenComplete((res, thr) -> {
                    if (thr == null) {
                        ResponseHelper.sendJson(rc, res);
                    } else {
                        BaseResponse<Object> response = BaseResponse
                                .builder()
                                .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                                .message(thr.getMessage())
                                .build();
                        ResponseHelper.sendJson(rc, response);
                    }
                });
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
