package com.example.socialservice.exception;

import com.example.socialservice.exception.error.ErrorCodes;
import com.example.socialservice.exception.error.ErrorMessages;
import com.example.socialservice.exception.error.GrpcException;

public class UserNotLikedException extends GrpcException {
    public UserNotLikedException() {
        super(ErrorCodes.USER_NOT_LIKED_CODE, ErrorMessages.USER_NOT_LIKED);
    }
}
