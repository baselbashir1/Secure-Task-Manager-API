package com.task.managerapi.mappers;

import com.task.managerapi.dto.responses.PagedResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PagedMapper {
    <T> PagedResponse<T> mapToPagedResponse(List<T> responseList, Page<?> responsePage);
}