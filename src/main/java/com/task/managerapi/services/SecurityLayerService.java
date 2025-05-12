package com.task.managerapi.services;

import com.task.managerapi.dto.responses.UserResponse;

public interface SecurityLayerService {
    UserResponse getUserFromToken();
}