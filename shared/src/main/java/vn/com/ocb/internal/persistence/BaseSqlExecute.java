package vn.com.ocb.internal.persistence;

import java.util.LinkedHashMap;

public interface BaseSqlExecute {

    int execute(String sql, LinkedHashMap<String, Object> params);

    int executeWithTransaction(String sql, LinkedHashMap<String, Object> params);

}
