package com.example.socialservice.exception;

import com.example.socialservice.exception.error.ErrorCodes;
import com.example.socialservice.exception.error.GrpcException;

public class UnauthorizedException extends GrpcException {
    public UnauthorizedException(String message) {
        super(ErrorCodes.UNAUTHORIZED_CODE, message);
    }
}
