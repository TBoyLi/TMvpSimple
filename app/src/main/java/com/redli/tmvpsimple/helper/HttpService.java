package com.redli.tmvpsimple.helper;

import com.redli.tmvpsimple.bean.MovieBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by redli on 2017/3/15.
 */



public class HttpService {
    public interface MovieService {
        @GET("top250")
        Observable<MovieBean> getTopMovie(@Query("start") int start, @Query("count") int count);
    }
}
