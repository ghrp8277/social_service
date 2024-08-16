package com.example.socialservice.grpc;

import com.example.grpc.*;
import com.example.socialservice.annotation.GrpcExceptionHandler;
import com.example.socialservice.dto.PostDto;
import com.example.socialservice.entity.*;
import com.example.socialservice.service.SocialService;
import com.example.socialservice.util.GrpcResponseHelper;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GrpcService
public class SocialServiceTmpl extends SocialServiceGrpc.SocialServiceImplBase {
    private final String DEFAULT_RESULTS = "results";

    @Autowired
    private SocialService socialService;

    @Autowired
    private GrpcResponseHelper grpcResponseHelper;

    @Override
    @GrpcExceptionHandler
    public void followUser(FollowUserRequest request, StreamObserver<Response> responseObserver) {
        Follow follow = socialService.followUser(request.getFollowerId(), request.getFolloweeId());
        Map<String, Object> response = new HashMap<>();
        response.put("follower_id", follow.getFollowerId());
        response.put("followee_id", follow.getFolloweeId());
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void unfollowUser(UnfollowRequest request, StreamObserver<Response> responseObserver) {
        Follow follow = socialService.unfollowUser(request.getFollowerId(), request.getFolloweeId());
        Map<String, Object> response = new HashMap<>();
        response.put("follower_id", follow.getFollowerId());
        response.put("followee_id", follow.getFolloweeId());
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void getFollowers(GetFollowersRequest request, StreamObserver<Response> responseObserver) {
        List<Follow> followers = socialService.getFollowers(request.getUserId());
        List<Long> followerIds = followers.stream().map(Follow::getFollowerId).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("followerIds", followerIds);
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void getPosts(GetPostsRequest request, StreamObserver<Response> responseObserver) {
        Page<Post> postsPage = socialService.getPosts(request.getCode(), request.getPage(), request.getPageSize());
        List<PostDto> postDtos = postsPage.getContent().stream()
            .map(socialService::convertToDto)
            .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("total_pages", postsPage.getTotalPages());
        response.put("total_elements", postsPage.getTotalElements());
        response.put("current_page", postsPage.getNumber());
        response.put("posts", postDtos);
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void getPostById(GetPostByIdRequest request, StreamObserver<Response> responseObserver) {
        PostDto postDto = socialService.getPostById(
                request.getPostId(),
                request.getUserId()
        ).get();
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, postDto, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void searchPosts(SearchPostsRequest request, StreamObserver<Response> responseObserver) {
        Page<Post> postsPage = socialService.searchPosts(request.getKeyword(), request.getPage(), request.getPageSize());
        List<PostDto> postDtos = postsPage.getContent().stream()
                .map(socialService::convertToDto)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("total_pages", postsPage.getTotalPages());
        response.put("total_elements", postsPage.getTotalElements());
        response.put("current_page", postsPage.getNumber());
        response.put("posts", postDtos);
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void createPost(CreatePostRequest request, StreamObserver<Response> responseObserver) {
        Post post = socialService.createPost(
                request.getUserId(),
                request.getTitle(),
                request.getAuthor(),
                request.getAccountName(),
                request.getContent(),
                request.getStockCode()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("post_id", post.getId());
        response.put("user_id", post.getUserId());
        response.put("post_stock_name", post.getPostStock().getStockName());
        response.put("post_stock_code", post.getPostStock().getStockCode());
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void updatePost(UpdatePostRequest request, StreamObserver<Response> responseObserver) {
        Post post = socialService.updatePost(request.getPostId(), request.getUserId(), request.getTitle(), request.getContent());
        Map<String, Object> response = new HashMap<>();
        response.put("post_id", post.getId());
        response.put("user_id", post.getUserId());
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void deletePost(DeletePostRequest request, StreamObserver<Response> responseObserver) {
        socialService.deletePost(request.getPostId(), request.getUserId());
        Map<String, Object> response = new HashMap<>();
        response.put("post_id", request.getPostId());
        response.put("message", "Post deleted successfully");
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void incrementPostViews(IncrementPostViewsRequest request, StreamObserver<Response> responseObserver) {
        Post post = socialService.incrementPostViews(request.getPostId());
        Map<String, Object> response = new HashMap<>();
        response.put("post_id", post.getId());
        response.put("views", post.getViews());
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void incrementPostLikes(IncrementPostLikesRequest request, StreamObserver<Response> responseObserver) {
        Post post = socialService.incrementPostLikes(
                request.getPostId(),
                request.getUserId()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("post_id", post.getId());
        response.put("likes", post.getLikes());
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void decrementPostLikes(DecrementPostLikesRequest request, StreamObserver<Response> responseObserver) {
        Post post = socialService.decrementPostLikes(
                request.getPostId(),
                request.getUserId()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("post_id", post.getId());
        response.put("likes", post.getLikes());
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void createComment(CreateCommentRequest request, StreamObserver<Response> responseObserver) {
        Comment comment = socialService.createComment(
                request.getPostId(),
                request.getUserId(),
                request.getUsername(),
                request.getContent()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("id", comment.getId());
        response.put("postId", comment.getPost().getId());
        response.put("userId", comment.getUserId());
        response.put("content", comment.getContent());
        response.put("createdAt", comment.getCreatedAt().toString());
        response.put("likes", comment.getLikes());
        response.put("username", comment.getUsername());
        response.put("parentCommentId", comment.getParentComment() != null ? comment.getParentComment().getId() : null);
        response.put("likedByUser", false);
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void updateComment(UpdateCommentRequest request, StreamObserver<Response> responseObserver) {
        Comment comment = socialService.updateComment(request.getCommentId(), request.getUserId(), request.getContent());
        Map<String, Object> response = new HashMap<>();
        response.put("comment_id", comment.getId());
        response.put("content", comment.getContent());
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void deleteComment(DeleteCommentRequest request, StreamObserver<Response> responseObserver) {
        List<Long> deletedCommentIds = socialService.deleteComment(request.getCommentId(), request.getUserId());
        Map<String, Object> response = new HashMap<>();
        response.put("deletedCommentIds", deletedCommentIds);
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void incrementCommentLikes(IncrementCommentLikesRequest request, StreamObserver<Response> responseObserver) {
        Comment comment = socialService.incrementCommentLikes(request.getCommentId(), request.getUserId());
        Map<String, Object> response = new HashMap<>();
        response.put("comment_id", comment.getId());
        response.put("likes", comment.getLikes());
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void decrementCommentLikes(DecrementCommentLikesRequest request, StreamObserver<Response> responseObserver) {
        Comment comment = socialService.decrementCommentLikes(request.getCommentId(), request.getUserId());
        Map<String, Object> response = new HashMap<>();
        response.put("comment_id", comment.getId());
        response.put("likes", comment.getLikes());
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void createReply(CreateReplyRequest request, StreamObserver<Response> responseObserver) {
        Comment comment = socialService.createReply(
                request.getPostId(),
                request.getUserId(),
                request.getUsername(),
                request.getParentCommentId(),
                request.getContent()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("id", comment.getId());
        response.put("postId", comment.getPost().getId());
        response.put("userId", comment.getUserId());
        response.put("content", comment.getContent());
        response.put("createdAt", comment.getCreatedAt().toString());
        response.put("likes", comment.getLikes());
        response.put("username", comment.getUsername());
        response.put("parentCommentId", comment.getParentComment() != null ? comment.getParentComment().getId() : null);
        response.put("likedByUser", false);
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void getUnreadFeedActivities(GetUnreadFeedActivitiesRequest request, StreamObserver<Response> responseObserver) {
        Page<Activity> activitiesPage = socialService.getUnreadFeedActivities(request.getUserId(), request.getPage(), request.getPageSize());
        List<Activity> activities = activitiesPage.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("total_pages", activitiesPage.getTotalPages());
        response.put("total_elements", activitiesPage.getTotalElements());
        response.put("current_page", activitiesPage.getNumber());
        response.put("activities", activities);
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void getFeedActivities(GetFeedActivitiesRequest request, StreamObserver<Response> responseObserver) {
        Page<Activity> activitiesPage = socialService.getFeedActivities(request.getUserId(), request.getPage(), request.getPageSize());
        List<Activity> activities = activitiesPage.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("total_pages", activitiesPage.getTotalPages());
        response.put("total_elements", activitiesPage.getTotalElements());
        response.put("current_page", activitiesPage.getNumber());
        response.put("activities", activities);

        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void markActivityAsRead(MarkActivityAsReadRequest request, StreamObserver<Response> responseObserver) {
        socialService.markActivityAsRead(request.getActivityId());
        Map<String, Object> response = new HashMap<>();
        response.put("activity_id", request.getActivityId());
        response.put("message", "Activity marked as read");
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void getLatestActivityForFollowees(GetLatestActivityRequest request, StreamObserver<Response> responseObserver) {
        Optional<Activity> latestActivityOptional = socialService.getLatestActivityForFollowees(request.getUserId());
        Activity latestActivity = latestActivityOptional.orElse(null);
        Map<String, Object> response = new HashMap<>();
        response.put("success", latestActivity != null);

        if (latestActivityOptional.isPresent()) {
            response.put("activity_id", latestActivity.getId());
            response.put("user_id", latestActivity.getUserId());
            response.put("type", latestActivity.getType());
            response.put("reference_id", latestActivity.getReferenceId());
            response.put("message", latestActivity.getMessage());
            response.put("created_at", latestActivity.getCreatedAt().toString());
            response.put("is_read", latestActivity.isRead());
        }

        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void createUserSyncInfo(CreateUserSyncInfoRequest request, StreamObserver<Response> responseObserver) {
        UserSyncInfo userSyncInfo = socialService.createUserSyncInfo(
                request.getUserId(),
                request.getUsername(),
                request.getActive()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("id", userSyncInfo.getId());
        response.put("username", userSyncInfo.getUsername());
        response.put("user_id", userSyncInfo.getUserId());
        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }

    @Override
    @GrpcExceptionHandler
    public void getUnfollowedUsers(GetUnfollowedUsersRequest request, StreamObserver<Response> responseObserver) {
        Page<UserSyncInfo> unfollowedUsersPage = socialService.getUnfollowedUsers(request.getUserId(),
                PageRequest.of(request.getPage(), request.getPageSize()));

        Map<String, Object> response = new HashMap<>();
        response.put("users", unfollowedUsersPage.getContent().stream().map(userSyncInfo -> {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", userSyncInfo.getUserId());
            userMap.put("username", userSyncInfo.getUsername());
            userMap.put("active", userSyncInfo.isActive());
            userMap.put("lastSyncedAt", userSyncInfo.getLastSyncedAt().toString());
            return userMap;
        }).collect(Collectors.toList()));

        response.put("totalPages", unfollowedUsersPage.getTotalPages());
        response.put("totalElements", unfollowedUsersPage.getTotalElements());
        response.put("currentPage", unfollowedUsersPage.getNumber());

        grpcResponseHelper.sendJsonResponse(DEFAULT_RESULTS, response, responseObserver);
    }
}
