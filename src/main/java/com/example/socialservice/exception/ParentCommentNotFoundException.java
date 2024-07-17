package com.example.socialservice.exception;

import com.example.socialservice.exception.error.ErrorCodes;
import com.example.socialservice.exception.error.ErrorMessages;
import com.example.socialservice.exception.error.GrpcException;

public class ParentCommentNotFoundException extends GrpcException {
    public ParentCommentNotFoundException() {
        super(ErrorCodes.PARENT_COMMENT_NOT_FOUND_CODE, ErrorMessages.PARENT_COMMENT_NOT_FOUND);
    }
}
