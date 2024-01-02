package vn.com.ocb.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<T> {
    private Integer statusCode;
    private String requestId;
    private String code;
    private String error;
    private String message;
    private T data;
}
