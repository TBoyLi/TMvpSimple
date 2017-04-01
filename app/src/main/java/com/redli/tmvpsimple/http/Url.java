package com.redli.tmvpsimple.http;

/**
 * Created by redli on 2017/3/30.
 */

public class Url {
    /**
     * URL的简单构成：
     *  [scheme:][//authority][path][?query]
     *
     * 例：http://www.java2s.com:8080/yourpath/fileName.htm?stove=10&path=32&id=4
     *
     *  scheme:  http
     *  authority:  www.java2s.com:8080
     *  path:  /yourpath/fileName.htm
     *  query: 在？后的部分为：stove=10&path=32&id=4
     *
     *  整个网络请求中参数主要可以分成：scheme、authority、path、query、header、body 这六块
     *  header（请求头）和 body（常用于post请求中的请求体，有多种封装方法，不暴露在url中）这两个参数。
     *
     *  Url header boby 等可以动态添加。
     */

    /**
     * 请求地址
     */
    public static final String BASE_URL = "https://api.douban.com/v2/movie/";


    /**
     * 获取电影path
     */
//    public static final String MOVIE_PATH = "v2/movie/";
}
