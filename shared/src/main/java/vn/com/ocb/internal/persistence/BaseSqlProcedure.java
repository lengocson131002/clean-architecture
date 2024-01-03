package vn.com.ocb.internal.persistence;

import java.util.LinkedHashMap;

public interface BaseSqlProcedure {

    int executeProcedure(String procedure, LinkedHashMap<String, Object> params);

    int executeProcedureWithTransaction(String procedure, LinkedHashMap<String, Object> params);

}
