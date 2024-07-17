package com.example.socialservice.exception;

import com.example.socialservice.exception.error.ErrorCodes;
import com.example.socialservice.exception.error.ErrorMessages;
import com.example.socialservice.exception.error.GrpcException;

public class UserAlreadyLikedException extends GrpcException {
    public UserAlreadyLikedException() {
        super(ErrorCodes.USER_ALREADY_LIKED_CODE, ErrorMessages.USER_ALREADY_LIKED);
    }
}