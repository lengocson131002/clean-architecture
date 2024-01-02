package vn.com.rest.handler;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;
import vn.com.ocb.pipeline.request.RequestPipeline;
import vn.com.ocb.usecase.auth.model.request.LoginRequest;
import vn.com.ocb.usecase.user.model.request.CreateUserRequest;
import vn.com.ocb.usecase.user.model.request.GetAllUserRequest;
import vn.com.ocb.usecase.user.model.request.GetUserRequest;
import vn.com.ocb.usecase.user.model.request.UpdateUserRequest;
import vn.com.rest.utils.ResponseHelper;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserHandler {
    private final RequestPipeline pipeline;

    public void handleLoginUser(RoutingContext rc) {
        JsonObject reqBody = rc.body().asJsonObject();
        String email = reqBody.getString("email");
        String password = reqBody.getString("password");
        LoginRequest request = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        pipeline.send(request).whenComplete((res, ex) -> {
            boolean isError = ex != null;
            if (!isError) {
                ResponseHelper.sendJson(rc, request.getRequestId(), res);
            } else {
                ResponseHelper.sendError(rc, request.getRequestId(), ex);
            }
        });
    }

    public void handleGetAllUsers(RoutingContext rc) {
        GetAllUserRequest request = new GetAllUserRequest();
        pipeline.send(request)
                .whenComplete((res, thr) -> {
                    if (thr == null) {
                        ResponseHelper.sendJson(rc, request.getRequestId(), res);
                    } else {
                        ResponseHelper.sendError(rc, request.getRequestId(), thr);
                    }
                });
    }

    public void handleGetUserById(RoutingContext rc) {
        String id = rc.request().getParam("userId");
        GetUserRequest request = new GetUserRequest(id);
        pipeline.send(request)
                .whenComplete((res, ex) -> {
                    boolean isError = ex != null;
                    if (!isError) {
                        ResponseHelper.sendJson(rc, request.getRequestId(), res);
                    } else {
                        ResponseHelper.sendError(rc, request.getRequestId(), ex);
                    }
                });
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

        pipeline.send(request)
                .whenComplete((res, ex) -> {
                    boolean isError = ex != null;
                    if (!isError) {
                        ResponseHelper.sendJson(rc, request.getRequestId(), res);
                    } else {
                        ResponseHelper.sendError(rc, request.getRequestId(), ex);
                    }
                });
    }

    public void handleCreateUser(RoutingContext rc) {
        JsonObject reqBody = rc.body().asJsonObject();
        CreateUserRequest request = CreateUserRequest.builder()
                .email(reqBody.getString("email"))
                .password(reqBody.getString("password"))
                .firstName(reqBody.getString("firstName"))
                .lastName(reqBody.getString("lastName"))
                .build();

        pipeline.send(request)
                .whenComplete((res, ex) -> {
                    boolean isError = ex != null;
                    if (!isError) {
                        ResponseHelper.sendJson(rc, request.getRequestId(), res);
                    } else {
                        ResponseHelper.sendError(rc, request.getRequestId(), ex);
                    }
                });
    }
}
