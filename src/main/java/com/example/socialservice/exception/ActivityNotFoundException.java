package com.example.socialservice.exception;

import com.example.socialservice.exception.error.ErrorCodes;
import com.example.socialservice.exception.error.ErrorMessages;
import com.example.socialservice.exception.error.GrpcException;

public class ActivityNotFoundException extends GrpcException {
    public ActivityNotFoundException() {
        super(ErrorCodes.ACTIVITY_NOT_FOUND_CODE, ErrorMessages.ACTIVITY_NOT_FOUND);
    }
}