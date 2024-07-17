package com.example.socialservice.exception;

import com.example.socialservice.exception.error.ErrorCodes;
import com.example.socialservice.exception.error.ErrorMessages;
import com.example.socialservice.exception.error.GrpcException;

public class CommentNotFoundException extends GrpcException {
    public CommentNotFoundException() {
        super(ErrorCodes.COMMENT_NOT_FOUND_CODE, ErrorMessages.COMMENT_NOT_FOUND);
    }
}
