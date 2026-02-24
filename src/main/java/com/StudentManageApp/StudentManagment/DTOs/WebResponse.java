package com.StudentManageApp.StudentManagment.DTOs;

import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class WebResponse <T> {
    private List<T> list;
    private String msg;

    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;

    public void mapper(Page<T> page, Pageable pageable){
        this.setPageNumber(pageable.getPageNumber());
        this.setPageSize(pageable.getPageSize());
        this.setTotalElements(page.getTotalElements());
        this.setTotalPages(page.getTotalPages());
        this.setLastPage(page.isLast());
    }
}
