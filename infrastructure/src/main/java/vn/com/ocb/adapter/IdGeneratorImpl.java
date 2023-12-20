package vn.com.ocb.adapter;

import lombok.RequiredArgsConstructor;
import vn.com.ocb.port.IdGenerator;

import javax.inject.Inject;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class IdGeneratorImpl implements IdGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
