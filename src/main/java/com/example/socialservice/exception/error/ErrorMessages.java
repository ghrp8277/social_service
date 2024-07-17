package com.example.socialservice.exception.error;

public class ErrorMessages {
    public static final String POST_NOT_FOUND = "Post not found";
    public static final String COMMENT_NOT_FOUND = "Comment not found";
    public static final String PARENT_COMMENT_NOT_FOUND = "Parent comment not found";
    public static final String ACTIVITY_NOT_FOUND = "Activity not found";
    public static final String USER_ALREADY_LIKED = "User has already liked this post/comment";
    public static final String USER_NOT_LIKED = "User has not liked this post/comment";
    public static final String UNAUTHORIZED_UPDATE_COMMENT = "User not authorized to update this comment";
    public static final String UNAUTHORIZED_DELETE_COMMENT = "User not authorized to delete this comment";
    public static final String POST_NOT_FOUND_FOR_COMMENT = "Post not found for the given comment";
    public static final String UNAUTHORIZED_DELETE_POST = "User not authorized to delete this post";
    public static final String UNAUTHORIZED_UPDATE_POST = "User not authorized to update this post";
    public static final String NO_FOLLOW_RELATIONSHIP = "No follow relationship found to unfollow";
    public static final String NO_ACTIVITIES_FOUND = "No activities found for followees";
    public static final String FOLLOW_ALREADY_EXISTS = "User is already following this user";
    public static final String POST_STOCK_NOT_FOUND = "Post stock not found";
}
