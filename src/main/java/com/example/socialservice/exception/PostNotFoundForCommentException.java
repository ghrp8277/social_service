package com.example.socialservice.exception;

import com.example.socialservice.exception.error.ErrorCodes;
import com.example.socialservice.exception.error.ErrorMessages;
import com.example.socialservice.exception.error.GrpcException;

public class PostNotFoundForCommentException extends GrpcException {
    public PostNotFoundForCommentException() {
        super(ErrorCodes.POST_NOT_FOUND_FOR_COMMENT_CODE, ErrorMessages.POST_NOT_FOUND_FOR_COMMENT);

    }
}
