package vn.com.ocb.internal.persistence;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;

public interface BaseSqlQuery<T> {

    Optional<T> querySingle(String sql, LinkedHashMap<String, Object> params);

    Collection<T> queryMultiple(String sql, LinkedHashMap<String, Object> params);

}
