package com.example.socialservice.exception;

import com.example.socialservice.exception.error.ErrorCodes;
import com.example.socialservice.exception.error.ErrorMessages;
import com.example.socialservice.exception.error.GrpcException;

public class NoActivitiesFoundException extends GrpcException {
    public NoActivitiesFoundException() {
        super(ErrorCodes.NO_ACTIVITIES_FOUND_CODE, ErrorMessages.NO_ACTIVITIES_FOUND);
    }
}