package com.lainexperiment.android_project02;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MoshiAPI
{
    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("posts/{postId}/comments")
    Call<List<Comment>> getComments(@Path("postId") int postId);
}
