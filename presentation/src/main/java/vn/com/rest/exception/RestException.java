package vn.com.rest.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class RestException extends Exception {
    private static final long serialVersionUID = 2851476111189913616L;

    public RestException(int code) {
        this.code = code;
    }

    private int code;

    private String message;

    @Override
    public String getMessage() {
        return this.message;
    }

    public static RestException create(int code) {
        return new RestException(code);
    }

}
