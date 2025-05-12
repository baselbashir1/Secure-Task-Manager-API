package com.task.managerapi.mappers;

import com.task.managerapi.dto.responses.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagedMapperImpl implements PagedMapper {

    @Override
    public <T> PagedResponse<T> mapToPagedResponse(List<T> responseList, Page<?> responsePage) {
        return new PagedResponse<>(
                responseList,
                responsePage.getNumber(),
                responsePage.getSize(),
                responsePage.getTotalElements(),
                responsePage.getTotalPages(),
                responsePage.isLast()
        );
    }
}