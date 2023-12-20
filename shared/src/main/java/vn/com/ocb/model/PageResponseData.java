package vn.com.ocb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseData<T> {
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalSize;
    private Long totalPage;
    private Collection<T> data;
}
