package com.redli.tmvpsimple.http;

import com.redli.tmvpsimple.bean.HttpResult;
import com.redli.tmvpsimple.bean.Subject;
import com.redli.tmvpsimple.bean.UserEntity;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by redli on 2017/3/30.
 */

public interface ApiService {
    @GET("/student/mobileRegister")
    Observable<HttpResult<UserEntity>> login(@Query("phone") String phone, @Query("password") String psw);


    @GET("top250")
    Observable<HttpResult<List<Subject>>> getTopMovie(@Query("start") int start, @Query("count") int count);

    @GET("top250")
    Observable<HttpResult<Subject>> getUser( @Query("touken") String touken);
}
