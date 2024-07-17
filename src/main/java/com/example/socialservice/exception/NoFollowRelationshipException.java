package com.example.socialservice.exception;

import com.example.socialservice.exception.error.ErrorCodes;
import com.example.socialservice.exception.error.ErrorMessages;
import com.example.socialservice.exception.error.GrpcException;

public class NoFollowRelationshipException extends GrpcException {
    public NoFollowRelationshipException() {
        super(ErrorCodes.NO_FOLLOW_RELATIONSHIP_CODE, ErrorMessages.NO_FOLLOW_RELATIONSHIP);
    }
}