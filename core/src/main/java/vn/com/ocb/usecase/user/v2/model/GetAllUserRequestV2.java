package vn.com.ocb.usecase.user.v2.model;

import vn.com.ocb.pipeline.request.Request;
import vn.com.ocb.usecase.user.model.UserResponse;

import java.util.List;

public class GetAllUserRequestV2 extends Request<List<UserResponse>> {
    @Override
    public boolean isValid() {
        return true;
    }
}
