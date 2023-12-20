package vn.com.ocb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<T> {
    private Integer code;
    private String message;
    private String error;
    private String traceId;
    private T data;
}
