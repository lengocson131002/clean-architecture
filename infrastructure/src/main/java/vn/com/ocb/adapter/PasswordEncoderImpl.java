package vn.com.ocb.adapter;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import vn.com.ocb.port.PasswordEncoder;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PasswordEncoderImpl implements PasswordEncoder {
    @Override
    public String encode(String password) {
        return DigestUtils.sha256Hex(password);
    }
}
