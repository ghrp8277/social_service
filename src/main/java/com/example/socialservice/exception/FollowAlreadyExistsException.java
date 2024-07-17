package com.example.socialservice.exception;

import com.example.socialservice.exception.error.ErrorCodes;
import com.example.socialservice.exception.error.ErrorMessages;
import com.example.socialservice.exception.error.GrpcException;

public class FollowAlreadyExistsException extends GrpcException {
    public FollowAlreadyExistsException() {
        super(ErrorCodes.FOLLOW_ALREADY_EXISTS_CODE, ErrorMessages.FOLLOW_ALREADY_EXISTS);
    }
}
