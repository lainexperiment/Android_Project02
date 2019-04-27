package com.lainexperiment.project02;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MessageController
{
    // TODO There are two approaches for DataContainer Class. 1- getData method. 2- down casting
    private ArrayList<DataContainer> dataContainers;
    private MoshiAPI api;

    public MessageController()
    {
        this.dataContainers = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
        api = retrofit.create(MoshiAPI.class);
    }

    public void getPosts()
    {
        Call<List<Post>> postsCall = api.getPosts();
        postsCall.enqueue(new Callback<List<Post>>()
        {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response)
            {
                if (!response.isSuccessful())
                {
                    // TODO handle error
                    return;
                }

                List<Post> posts = response.body();
                if (posts == null)
                {
                    // TODO handle null posts
                    return;
                }
                dataContainers.clear();
                dataContainers.addAll(posts);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t)
            {
                // TODO handle error
            }
        });
    }

    public void getComments(int postId)
    {
        Call<List<Comment>> commentsCall = api.getComments(postId);
        commentsCall.enqueue(new Callback<List<Comment>>()
        {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response)
            {
                if (!response.isSuccessful())
                {
                    // TODO handle error
                    return;
                }

                List<Comment> comments = response.body();
                if (comments == null)
                {
                    // TODO handle null comments
                    return;
                }
                dataContainers.clear();
                dataContainers.addAll(comments);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t)
            {
                // TODO handle error
            }
        });
    }
}
