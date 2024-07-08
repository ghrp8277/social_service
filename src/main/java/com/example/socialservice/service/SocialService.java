package com.example.socialservice.service;

import com.example.socialservice.dto.PostDto;
import com.example.socialservice.entity.*;
import com.example.socialservice.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SocialService {
    private static final Logger logger = LoggerFactory.getLogger(SocialService.class);

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    public PostDto convertToDto(Post post) {
        return PostDto.fromEntity(post);
    }

    @Transactional
    public Follow followUser(Long followerId, Long followeeId) {
        if (!followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            Follow follow = new Follow();
            follow.setFollowerId(followerId);
            follow.setFolloweeId(followeeId);
            followRepository.save(follow);
            logActivity(followerId, "follow", followeeId, String.format("User %d followed user %d", followerId, followeeId));
            return follow;
        }

        return followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId).orElse(null);
    }

    @Transactional
    public Follow unfollowUser(Long followerId, Long followeeId) {
        Follow follow = followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId).orElse(null);

        if (follow != null) {
            followRepository.deleteByFollowerIdAndFolloweeId(followerId, followeeId);

            logActivity(followerId, "unfollow", followeeId, String.format("User %d unfollowed user %d", followerId, followeeId));

            return follow;
        }

        return null;
    }

    public List<Follow> getFollowers(Long userId) {
        return followRepository.findByFolloweeId(userId);
    }

    public List<Follow> getFollowing(Long userId) {
        return followRepository.findByFollowerId(userId);
    }

    @Transactional(readOnly = true)
    public Optional<PostDto> getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.map(PostDto::fromEntityWithComments);
    }

    @Transactional(readOnly = true)
    public Page<Post> getPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return postRepository.findAllPosts(pageRequest);
    }

    @Transactional
    public Post createPost(Long userId, String title, String author, String accountName, String content) {
        Post post = new Post();
        post.setUserId(userId);
        post.setTitle(title);
        post.setAuthor(author);
        post.setAccountName(accountName);
        post.setContent(content);
        Post savedPost = postRepository.save(post);
        logActivity(userId, "post", savedPost.getId(), String.format("User %d created a post with ID %d", userId, savedPost.getId()));
        return savedPost;
    }

    @Transactional
    public Post updatePost(Long postId, Long userId, String title, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("User not authorized to update this post");
        }
        post.setTitle(title);
        post.setContent(content);
        Post updatedPost = postRepository.save(post);
        logActivity(userId, "update_post", postId, String.format("User %d updated post with ID %d", userId, postId));
        return updatedPost;
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("User not authorized to delete this post");
        }
        postRepository.deleteById(postId);
        logActivity(userId, "delete_post", postId, String.format("User %d deleted post with ID %d", userId, postId));
    }

    @Transactional
    public Post incrementPostViews(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        post.incrementViews();
        return postRepository.save(post);
    }

    @Transactional
    public Post incrementPostLikes(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        boolean alreadyLiked = likeRepository.findByUserIdAndPostId(userId, postId).isPresent();
        if (alreadyLiked) {
            throw new RuntimeException("User has already liked this post");
        }

        Like like = new Like();
        like.setUserId(userId);
        like.setPost(post);
        likeRepository.save(like);

        post.incrementLikes();
        logActivity(userId, "like_post", postId, String.format("Post with ID %d got a new like", postId));
        return postRepository.save(post);
    }

    @Transactional
    public Post decrementPostLikes(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        Like like = likeRepository.findByUserIdAndPostId(userId, postId)
            .orElseThrow(() -> new RuntimeException("User has not liked this post"));

        likeRepository.delete(like);

        post.decrementLikes();
        logActivity(userId, "unlike_post", postId, String.format("Post with ID %d got a like removed", postId));
        return postRepository.save(post);
    }

    @Transactional
    public Comment incrementCommentLikes(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));

        Long postId = comment.getPost().getId();
        boolean postExists = postRepository.existsById(postId);
        if (!postExists) {
            throw new RuntimeException("Post not found for the given comment");
        }

        boolean alreadyLiked = likeRepository.findByUserIdAndCommentId(userId, commentId).isPresent();
        if (alreadyLiked) {
            throw new RuntimeException("User has already liked this comment");
        }

        Like like = new Like();
        like.setUserId(userId);
        like.setComment(comment);
        likeRepository.save(like);

        comment.incrementLikes();
        logActivity(userId, "like_comment", commentId, String.format("Comment with ID %d got a new like", commentId));
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment decrementCommentLikes(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));

        Long postId = comment.getPost().getId();
        boolean postExists = postRepository.existsById(postId);
        if (!postExists) {
            throw new RuntimeException("Post not found for the given comment");
        }

        Like like = likeRepository.findByUserIdAndCommentId(userId, commentId)
            .orElseThrow(() -> new RuntimeException("User has not liked this comment"));

        likeRepository.delete(like);

        comment.decrementLikes();
        logActivity(userId, "unlike_comment", commentId, String.format("Comment with ID %d got a like removed", commentId));
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment createComment(Long postId, Long userId, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        Comment comment = new Comment();
        comment.setPost(post); // Post 객체를 설정
        comment.setUserId(userId);
        comment.setContent(content);
        Comment savedComment = commentRepository.save(comment);
        logActivity(userId, "comment", savedComment.getId(), String.format("User %d created a comment with ID %d", userId, savedComment.getId()));
        return savedComment;
    }

    @Transactional
    public Comment createReply(Long postId, Long userId, Long parentCommentId, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        Comment parentComment = commentRepository.findById(parentCommentId).orElseThrow(() -> new RuntimeException("Parent comment not found"));
        Comment reply = new Comment();
        reply.setPost(post); // Post 객체를 설정
        reply.setUserId(userId);
        reply.setContent(content);
        reply.setParentComment(parentComment);
        Comment savedReply = commentRepository.save(reply);
        logActivity(userId, "reply", savedReply.getId(), String.format("User %d replied to comment ID %d with new comment ID %d", userId, parentCommentId, savedReply.getId()));
        return savedReply;
    }

    @Transactional
    public Comment updateComment(Long commentId, Long userId, String content) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("User not authorized to update this comment");
        }
        comment.setContent(content);
        Comment updatedComment = commentRepository.save(comment);
        logActivity(userId, "update_comment", commentId, String.format("User %d updated comment with ID %d", userId, commentId));
        return updatedComment;
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("User not authorized to delete this comment");
        }
        commentRepository.deleteById(commentId);
        logActivity(userId, "delete_comment", commentId, String.format("User %d deleted comment with ID %d", userId, commentId));
    }

    @Transactional
    public void markActivityAsRead(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
            .orElseThrow(() -> new RuntimeException("Activity not found"));
        activity.setRead(true);
        activityRepository.save(activity);
    }

    @Transactional(readOnly = true)
    public Page<Activity> getUnreadFeedActivities(Long userId, int page, int size) {
        List<Long> followeeIds = followRepository.findByFollowerId(userId).stream()
                .map(Follow::getFolloweeId)
                .collect(Collectors.toList());

        if (followeeIds.isEmpty()) {
            return Page.empty();
        }

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return activityRepository.findByUserIdInAndIsReadFalseOrderByCreatedAtDesc(followeeIds, pageRequest);
    }

    @Transactional(readOnly = true)
    public Page<Activity> getFeedActivities(Long userId, int page, int size) {
        List<Long> followeeIds = followRepository.findByFollowerId(userId).stream()
                .map(Follow::getFolloweeId)
                .collect(Collectors.toList());

        if (followeeIds.isEmpty()) {
            return Page.empty();
        }

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return activityRepository.findByUserIdInOrderByCreatedAtDesc(followeeIds, pageRequest);
    }

    @Transactional(readOnly = true)
    public Optional<Activity> getLatestActivityForFollowees(Long userId) {
        List<Long> followeeIds = followRepository.findByFollowerId(userId).stream()
                .map(Follow::getFolloweeId)
                .collect(Collectors.toList());

        if (followeeIds.isEmpty()) {
            return Optional.empty();
        }

        return activityRepository.findTopByUserIdInOrderByCreatedAtDesc(followeeIds);
    }

    private void logActivity(Long userId, String type, Long referenceId, String message) {
        Activity activity = new Activity();
        activity.setUserId(userId);
        activity.setType(type);
        activity.setReferenceId(referenceId);
        activity.setMessage(message);
        activityRepository.save(activity);
    }
}
