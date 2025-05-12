package com.task.managerapi.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "request_logs")
public class RequestLog extends BaseModel {

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "method")
    private String method;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "message", nullable = false, length = 10000)
    private String message;

    @Column(name = "status_code")
    private Integer statusCode;
}