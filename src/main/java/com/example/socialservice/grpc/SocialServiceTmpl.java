package com.example.socialservice.grpc;

import com.example.grpc.*;
import com.example.socialservice.dto.PostDto;
import com.example.socialservice.entity.Activity;
import com.example.socialservice.entity.Comment;
import com.example.socialservice.entity.Follow;
import com.example.socialservice.entity.Post;
import com.example.socialservice.service.SocialService;
import com.example.socialservice.util.GrpcResponseHelper;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GrpcService
public class SocialServiceTmpl extends SocialServiceGrpc.SocialServiceImplBase {
    @Autowired
    private SocialService socialService;

    @Autowired
    private GrpcResponseHelper grpcResponseHelper;

    @Override
    public void followUser(FollowUserRequest request, StreamObserver<Response> responseObserver) {
        Follow follow = socialService.followUser(request.getFollowerId(), request.getFolloweeId());
        Map<String, Object> response = new HashMap<>();
        response.put("follower_id", follow.getFollowerId());
        response.put("followee_id", follow.getFolloweeId());
        grpcResponseHelper.sendJsonResponse("follow", response, responseObserver);
    }

    @Override
    public void unfollowUser(UnfollowRequest request, StreamObserver<Response> responseObserver) {
        Follow follow = socialService.unfollowUser(request.getFollowerId(), request.getFolloweeId());

        if (follow != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("follower_id", follow.getFollowerId());
            response.put("followee_id", follow.getFolloweeId());
            grpcResponseHelper.sendJsonResponse("unfollow", response, responseObserver);
        } else {
            grpcResponseHelper.sendErrorResponse( "No follow relationship found to unfollow", responseObserver);
        }
    }

    @Override
    public void getFollowers(GetFollowersRequest request, StreamObserver<Response> responseObserver) {
        List<Follow> followers = socialService.getFollowers(request.getUserId());
        List<Long> followerIds = followers.stream().map(Follow::getFollowerId).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("followerIds", followerIds);
        grpcResponseHelper.sendJsonResponse("followers", response, responseObserver);
    }

    @Override
    public void getPosts(GetPostsRequest request, StreamObserver<Response> responseObserver) {
        Page<Post> postsPage = socialService.getPosts(request.getPage(), request.getPageSize());
        List<PostDto> postDtos = postsPage.getContent().stream()
            .map(socialService::convertToDto)
            .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("total_pages", postsPage.getTotalPages());
        response.put("total_elements", postsPage.getTotalElements());
        response.put("current_page", postsPage.getNumber());
        response.put("posts", postDtos);

        try {
            grpcResponseHelper.sendJsonResponse("posts", response, responseObserver);
        } catch (Exception e) {
            grpcResponseHelper.sendErrorResponse("Failed to process response", responseObserver);
        }
    }

    @Override
    public void getPostById(GetPostByIdRequest request, StreamObserver<Response> responseObserver) {
        Optional<PostDto> postDtoOptional = socialService.getPostById(request.getPostId());
        if (postDtoOptional.isPresent()) {
            PostDto postDto = postDtoOptional.get();
            grpcResponseHelper.sendJsonResponse("post", postDto, responseObserver);
        } else {
            grpcResponseHelper.sendErrorResponse("Post not found", responseObserver);
        }
    }

    @Override
    public void createPost(CreatePostRequest request, StreamObserver<Response> responseObserver) {
        Post post = socialService.createPost(request.getUserId(), request.getTitle(), request.getAuthor(), request.getAccountName(), request.getContent());
        Map<String, Object> response = new HashMap<>();
        response.put("post_id", post.getId());
        response.put("user_id", post.getUserId());
        grpcResponseHelper.sendJsonResponse("post", response, responseObserver);
    }

    @Override
    public void updatePost(UpdatePostRequest request, StreamObserver<Response> responseObserver) {
        try {
            Post post = socialService.updatePost(request.getPostId(), request.getUserId(), request.getTitle(), request.getContent());
            Map<String, Object> response = new HashMap<>();
            response.put("post_id", post.getId());
            response.put("user_id", post.getUserId());
            grpcResponseHelper.sendJsonResponse("post", response, responseObserver);
        } catch (RuntimeException e) {
            grpcResponseHelper.sendErrorResponse(e.getMessage(), responseObserver);
        }
    }

    @Override
    public void deletePost(DeletePostRequest request, StreamObserver<Response> responseObserver) {
        try {
            socialService.deletePost(request.getPostId(), request.getUserId());
            Map<String, Object> response = new HashMap<>();
            response.put("post_id", request.getPostId());
            response.put("message", "Post deleted successfully");
            grpcResponseHelper.sendJsonResponse("delete_post", response, responseObserver);
        } catch (RuntimeException e) {
            grpcResponseHelper.sendErrorResponse(e.getMessage(), responseObserver);
        }
    }

    @Override
    public void incrementPostViews(IncrementPostViewsRequest request, StreamObserver<Response> responseObserver) {
        Post post = socialService.incrementPostViews(request.getPostId());
        Map<String, Object> response = new HashMap<>();
        response.put("post_id", post.getId());
        response.put("views", post.getViews());
        grpcResponseHelper.sendJsonResponse("post_views", response, responseObserver);
    }

    @Override
    public void incrementPostLikes(IncrementPostLikesRequest request, StreamObserver<Response> responseObserver) {
        try {
            Post post = socialService.incrementPostLikes(
                request.getPostId(),
                request.getUserId()
            );
            Map<String, Object> response = new HashMap<>();
            response.put("post_id", post.getId());
            response.put("likes", post.getLikes());
            grpcResponseHelper.sendJsonResponse("post_likes", response, responseObserver);
        } catch (RuntimeException e) {
            grpcResponseHelper.sendErrorResponse(e.getMessage(), responseObserver);
        } catch (Exception e) {
            grpcResponseHelper.sendErrorResponse("An unexpected error occurred", responseObserver);
        }
    }

    @Override
    public void decrementPostLikes(DecrementPostLikesRequest request, StreamObserver<Response> responseObserver) {
        try {
            Post post = socialService.decrementPostLikes(
                request.getPostId(),
                request.getUserId()
            );
            Map<String, Object> response = new HashMap<>();
            response.put("post_id", post.getId());
            response.put("likes", post.getLikes());
            grpcResponseHelper.sendJsonResponse("post_likes", response, responseObserver);
        } catch (RuntimeException e) {
            grpcResponseHelper.sendErrorResponse(e.getMessage(), responseObserver);
        } catch (Exception e) {
            grpcResponseHelper.sendErrorResponse("An unexpected error occurred", responseObserver);
        }
    }

    @Override
    public void createComment(CreateCommentRequest request, StreamObserver<Response> responseObserver) {
        try {
            Comment comment = socialService.createComment(request.getPostId(), request.getUserId(), request.getContent());
            Map<String, Object> response = new HashMap<>();
            response.put("comment_id", comment.getId());
            response.put("post_id", comment.getPost().getId());
            grpcResponseHelper.sendJsonResponse("comment", response, responseObserver);
        } catch (RuntimeException e) {
            grpcResponseHelper.sendErrorResponse(e.getMessage(), responseObserver);
        }
    }

    @Override
    public void updateComment(UpdateCommentRequest request, StreamObserver<Response> responseObserver) {
        try {
            Comment comment = socialService.updateComment(request.getCommentId(), request.getUserId(), request.getContent());
            Map<String, Object> response = new HashMap<>();
            response.put("comment_id", comment.getId());
            response.put("content", comment.getContent());
            grpcResponseHelper.sendJsonResponse("update_comment", response, responseObserver);
        } catch (RuntimeException e) {
            grpcResponseHelper.sendErrorResponse(e.getMessage(), responseObserver);
        }
    }

    @Override
    public void deleteComment(DeleteCommentRequest request, StreamObserver<Response> responseObserver) {
        try {
            socialService.deleteComment(request.getCommentId(), request.getUserId());
            Map<String, Object> response = new HashMap<>();
            response.put("comment_id", request.getCommentId());
            response.put("message", "Comment deleted successfully");
            grpcResponseHelper.sendJsonResponse("delete_comment", response, responseObserver);
        } catch (RuntimeException e) {
            grpcResponseHelper.sendErrorResponse(e.getMessage(), responseObserver);
        }
    }

    @Override
    public void incrementCommentLikes(IncrementCommentLikesRequest request, StreamObserver<Response> responseObserver) {
        try {
            Comment comment = socialService.incrementCommentLikes(request.getCommentId(), request.getUserId());
            Map<String, Object> response = new HashMap<>();
            response.put("comment_id", comment.getId());
            response.put("likes", comment.getLikes());
            grpcResponseHelper.sendJsonResponse("comment_likes", response, responseObserver);
        } catch (RuntimeException e) {
            grpcResponseHelper.sendErrorResponse(e.getMessage(), responseObserver);
        } catch (Exception e) {
            grpcResponseHelper.sendErrorResponse("An unexpected error occurred", responseObserver);
        }
    }

    @Override
    public void decrementCommentLikes(DecrementCommentLikesRequest request, StreamObserver<Response> responseObserver) {
        try {
            Comment comment = socialService.decrementCommentLikes(request.getCommentId(), request.getUserId());
            Map<String, Object> response = new HashMap<>();
            response.put("comment_id", comment.getId());
            response.put("likes", comment.getLikes());
            grpcResponseHelper.sendJsonResponse("comment_likes", response, responseObserver);
        } catch (RuntimeException e) {
            grpcResponseHelper.sendErrorResponse(e.getMessage(), responseObserver);
        } catch (Exception e) {
            grpcResponseHelper.sendErrorResponse("An unexpected error occurred", responseObserver);
        }
    }

    @Override
    public void createReply(CreateReplyRequest request, StreamObserver<Response> responseObserver) {
        try {
            Comment reply = socialService.createReply(request.getPostId(), request.getUserId(), request.getParentCommentId(), request.getContent());
            Map<String, Object> response = new HashMap<>();
            response.put("reply_id", reply.getId());
            response.put("post_id", reply.getPost().getId());
            response.put("parent_comment_id", reply.getParentComment().getId());
            grpcResponseHelper.sendJsonResponse("reply", response, responseObserver);
        } catch (RuntimeException e) {
            grpcResponseHelper.sendErrorResponse(e.getMessage(), responseObserver);
        }
    }

    @Override
    public void getUnreadFeedActivities(GetUnreadFeedActivitiesRequest request, StreamObserver<Response> responseObserver) {
        Page<Activity> activitiesPage = socialService.getUnreadFeedActivities(request.getUserId(), request.getPage(), request.getPageSize());
        List<Activity> activities = activitiesPage.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("total_pages", activitiesPage.getTotalPages());
        response.put("total_elements", activitiesPage.getTotalElements());
        response.put("current_page", activitiesPage.getNumber());
        response.put("activities", activities);

        try {
            grpcResponseHelper.sendJsonResponse("unread_activities", response, responseObserver);
        } catch (Exception e) {
            grpcResponseHelper.sendErrorResponse("Failed to process response", responseObserver);
        }
    }

    @Override
    public void getFeedActivities(GetFeedActivitiesRequest request, StreamObserver<Response> responseObserver) {
        Page<Activity> activitiesPage = socialService.getFeedActivities(request.getUserId(), request.getPage(), request.getPageSize());
        List<Activity> activities = activitiesPage.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("total_pages", activitiesPage.getTotalPages());
        response.put("total_elements", activitiesPage.getTotalElements());
        response.put("current_page", activitiesPage.getNumber());
        response.put("activities", activities);

        try {
            grpcResponseHelper.sendJsonResponse("activities", response, responseObserver);
        } catch (Exception e) {
            grpcResponseHelper.sendErrorResponse("Failed to process response", responseObserver);
        }
    }

    @Override
    public void markActivityAsRead(MarkActivityAsReadRequest request, StreamObserver<Response> responseObserver) {
        try {
            socialService.markActivityAsRead(request.getActivityId());
            Map<String, Object> response = new HashMap<>();
            response.put("activity_id", request.getActivityId());
            response.put("message", "Activity marked as read");
            grpcResponseHelper.sendJsonResponse("mark_as_read", response, responseObserver);
        } catch (RuntimeException e) {
            grpcResponseHelper.sendErrorResponse(e.getMessage(), responseObserver);
        }
    }

    @Override
    public void getLatestActivityForFollowees(GetLatestActivityRequest request, StreamObserver<Response> responseObserver) {
        Optional<Activity> latestActivityOptional = socialService.getLatestActivityForFollowees(request.getUserId());

        if (latestActivityOptional.isPresent()) {
            Activity latestActivity = latestActivityOptional.get();
            grpcResponseHelper.sendJsonResponse("latest_activity", latestActivity, responseObserver);
        } else {
            grpcResponseHelper.sendErrorResponse("No activities found for followees", responseObserver);
        }
    }
}
