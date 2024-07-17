package com.example.socialservice.exception.error;

import io.grpc.Status;

public class ErrorCodes {
    public static final Status.Code POST_NOT_FOUND_CODE = Status.Code.NOT_FOUND;
    public static final Status.Code COMMENT_NOT_FOUND_CODE = Status.Code.NOT_FOUND;
    public static final Status.Code PARENT_COMMENT_NOT_FOUND_CODE = Status.Code.NOT_FOUND;
    public static final Status.Code ACTIVITY_NOT_FOUND_CODE = Status.Code.NOT_FOUND;
    public static final Status.Code UNAUTHORIZED_CODE = Status.Code.PERMISSION_DENIED;
    public static final Status.Code USER_ALREADY_LIKED_CODE = Status.Code.ALREADY_EXISTS;
    public static final Status.Code USER_NOT_LIKED_CODE = Status.Code.NOT_FOUND;
    public static final Status.Code POST_NOT_FOUND_FOR_COMMENT_CODE = Status.Code.NOT_FOUND;
    public static final Status.Code NO_FOLLOW_RELATIONSHIP_CODE = Status.Code.NOT_FOUND;
    public static final Status.Code NO_ACTIVITIES_FOUND_CODE = Status.Code.NOT_FOUND;
    public static final Status.Code FOLLOW_ALREADY_EXISTS_CODE = Status.Code.ALREADY_EXISTS;
    public static final Status.Code POST_STOCK_NOT_FOUND_CODE = Status.Code.NOT_FOUND;
}