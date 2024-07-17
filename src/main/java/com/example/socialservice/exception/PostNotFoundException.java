package com.example.socialservice.exception;

import com.example.socialservice.exception.error.ErrorCodes;
import com.example.socialservice.exception.error.ErrorMessages;
import com.example.socialservice.exception.error.GrpcException;

public class PostNotFoundException extends GrpcException {
    public PostNotFoundException() {
        super(ErrorCodes.POST_NOT_FOUND_CODE, ErrorMessages.POST_NOT_FOUND);

    }
}
