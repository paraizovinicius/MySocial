package com.paraizo.mysocial;

import com.paraizo.mysocial.model.Follow;
import com.paraizo.mysocial.model.FollowStatus;
import com.paraizo.mysocial.model.Post;
import com.paraizo.mysocial.model.PostComment;
import com.paraizo.mysocial.model.PostImage;
import com.paraizo.mysocial.model.PostLike;
import com.paraizo.mysocial.model.User;

public final class TestDataUtil {
    private TestDataUtil(){
        // private constructor to prevent instantiation
    }

    public static User createUserA(){
        return User.builder()
            .username("paraizovinicius")
            .email("paraizovinicius@gmail.com")
            .password("Vinicius@2026")
            .build();
    }

    public static User createUserB(){
        return User.builder()
            .username("borgespedro")
            .email("borgespedro@gmail.com")
            .password("Pedro@2026")
            .build();
    }

    public static User createUserC(){
        return User.builder()
            .username("souzaantonio")
            .email("souzaantonio@gmail.com")
            .password("Antonio@2026")
            .build();
    }

    public static Post createPostA(final User user){
        return Post.builder()
            .user(user)
            .title("Sagrada Familia is awesome!")
            .latitude(41.40338)
            .longitude(2.17403)
            .build();
    }

    public static Post createPostB(final User user){
        return Post.builder()
            .user(user)
            .title("Eiffel Tower is incredible!")
            .latitude(48.85883)
            .longitude(2.29391)
            .build();
    }
    
    public static Post createPostC(final User user){
        return Post.builder()
            .user(user)
            .title("Christ the Redeemer is amazing!")
            .latitude(-22.95185)
            .longitude(-43.21103)
            .build();
    }

    // public static PostImage createImageA(final Post post){
    //     return PostImage.builder()
    //         .storageKey("")
    //         .build();
    // }

    public static PostLike createPostLike(final User user, final Post post){
        return PostLike.builder()
            .post(post)
            .user(user)
            .build();
    }

    public static PostComment createPostCommentA(final User user, final Post post){
        return PostComment.builder()
            .post(post)
            .user(user)
            .content("COMMENT A")
            .build();
    }

    public static PostComment createPostCommentB(final User user, final Post post){
        return PostComment.builder()
            .post(post)
            .user(user)
            .content("COMMENT B")
            .build();
    }

    public static Follow createFollow(User userA, User userB){
        return Follow.builder()
            .follower(userB)
            .following(userA)
            .status(FollowStatus.ACCEPTED) // mocked ACCEPTED
            .build();
    }
}
