# 前言
mvp模式，rxjava响应式编程，retrofit网络强求已经出来很久了，很多前辈写了很多不错的文章，在此不得不感谢这些前辈无私奉献的开源精神，能让我们站在巨人的肩膀上望得更远。（我仅仅是开源代码的搬运工和组装工）对于这些知识点介绍几位杰出的大神。

mvp插件：<a href="http://yugai.github.io/2017/02/27/AndroidStudio-MVPPlugin/#more">AndroidStudio MVPPlugin插件</a>
mvp项目：<a href="https://github.com/gslovemy/RxJavaRetrofitOkhttpMvp">比较好的mvp模式的demo</a>（加了rxjava和retrofit的封装）
RxJava ：<a href="http://gank.io/post/560e15be2dca930e00da1083">给 Android 开发者的 RxJava 详解</a>
Retrofit：<a href="http://wuxiaolong.me/2016/01/15/retrofit/">Android Retrofit 2.0使用</a>

本文内容是基于TMvp + Retrofit + RxJava做的一些简单的搬运和封装。参考了很多文章（主要参考的是小河马大神的<a href="http://www.jianshu.com/p/bd758f51742e">Android RxJava+Retrofit完美封装（缓存，请求，生命周期管理）</a>）加入了一些自己的理解，请多指教，后续有什么问可github留言，源码地址：<a href="https://github.com/TBoyLi/TMvpSimple">Android TMvp+RxJava+Retrofit完美封装（缓存，请求，生命周期管理）</a>

# 主题
本栏目主要由三个部分组成（如下图）

![Tmvp.png](http://upload-images.jianshu.io/upload_images/1780580-ccf3c041094010e1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
很明显，第一部分就是简单的mvp（泛型）模式而已，第二部分主要是rxjava和retrofit的简单封装（没有生命周期管理，请求，缓存等），而第三部分就是解决第二部分没有得动作。

##第一部分（TMvp）
MVP架构模式细解：MVP是Model(数据,网络) View(界面) Presenter(表现层)的缩写，它是MVC架构的变种，强调Model和View的最大化解耦和单一职责原则
1. Model：负责数据的来源和封装，比如网络请求类，数据库操作类以及java bean，如果有必要则提供接口暴露自己处理数据的状态和进度,自己写一个回调就行
2. View：负责UI相关，如布局UI的初始化，各种listener的设置。在Android中，我们通常写的Activity和Fragment就是属于View层；在web开发中，html则是View层
3. Presenter：专门从C独立出来的业务逻辑层，主要负责处理原先View层的业务逻辑，解决了Activity的臃肿问题，让Activity只负责处理UI，职责更加明确；并且将View层的业务逻辑抽取到P层之后，View层与Model层也实现了解耦；便于后期代码的扩展和维护,并且业务逻辑层独立后代码还得到很大的重用性
4.  总结：MVC模式下，V和C纠缠不清，并且View和Model相互关联，而MVP模式下Model和VIew解耦，便于单元测试，项目维护，代码重用

包目录结构：
- model : LoginModel
- presenter：LoginPresenter
- view：Activity、Fragment、etc
- base：BaseAcitivity、BasePresenter、etc
- mvp：IView、IModel、IPresenter
- contract： 契约类、接口定义
- unitls：工具栏

##第二部分（Rxjava+Retrofit）（略）
这部分可以在第三部分更加深入的了解

##第三部分 （最终版）
先看最终封装出来的简单是用（HttpUtils）,这里我只做简单的陈述和使用，具体细节请认真查看<a href="http://www.jianshu.com/p/bd758f51742e">Android RxJava+Retrofit完美封装（缓存，请求，生命周期管理）</a>小河马大神已经写的很详细呢，有些地方呢可能要结合项目代码来消化。一遍不能呢两边。两边不行呢在继续看。敲一遍。搞定。也可以看这篇<a href="http://gank.io/post/56e80c2c677659311bed9841">RxJava 与 Retrofit 结合的最佳实践</a>，这两篇大同小异，只不过呢马神呢在后一篇的基础上添加了生命周期的管理，对于大神来说就是追求完美。
```
public class HttpUtil {

    /**
     * 构造方法私有
     */
    private HttpUtil() {
    }

    /**
     * 在访问HttpUtil时创建单例
     */
    private static class SingletonHolder {
        private static final HttpUtil INSTANCE = new HttpUtil();
    }

    /**
     * 获取单例
     */
    public static HttpUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 添加线程管理并订阅
     * @param ob
     * @param subscriber
     * @param cacheKey 缓存kay
     * @param event Activity 生命周期
     * @param lifecycleSubject
     * @param isSave 是否缓存
     * @param forceRefresh 是否强制刷新
     */
    public void toSubscribe(Observable ob,
                            final ProgressSubscriber subscriber,
                            String cacheKey,
                            final ActivityLifeCycleEvent event,
                            final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject,
                            boolean isSave,
                            boolean forceRefresh) {
        //数据预处理
        Observable.Transformer<HttpResult<Object>, Object> result = RxHelper.handleResult(event,lifecycleSubject);
        Observable observable = ob.compose(result)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //显示Dialog和一些其他操作
                        subscriber.showProgressDialog();
                    }
                });
        RetrofitCache.load(cacheKey,observable,isSave,forceRefresh).subscribe(subscriber);
    }
}
```
上述呢已经很明显单例获取对象直接调用toSubscribe添加线程管理并订阅，下面一次介绍订阅参数：

###第一个参数：
Observable，简单点就是Retrofit定义返回的Observable对象（当然肯定是对Retrofit的一个封装，例子直接看http包下面的Api等）
```
public class Api {

    private static ApiService SERVICE;

    /**
     * 请求超时时间
     */
    private static final int DEFAULT_TIMEOUT = 5;

    public static ApiService getDefault(){
        if (SERVICE == null) {

            /**
             * 手动创建一个OkHttpClient并设置超时时间
             */
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

            /**
             * 对所有请求添加请求头(全局header,可局部动态添加header)
             */
            httpClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    okhttp3.Response originalResponse = chain.proceed(request);
                    return originalResponse.newBuilder().header("key1", "value1").addHeader("key2", "value2").build();

                }
            });

            SERVICE = new Retrofit.Builder()
                    .client(httpClientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(Url.BASE_URL)
                    .build().create(ApiService.class);

        }
        return SERVICE;
    }

}
```
这里呢是简单的Retrofit的封装，需要调用什么接口自行在ApiService里加就ok，我这里直接定于简单的三个而已，纯粹实践：
```
public interface ApiService {

    //登录接口
    @GET("/student/mobileRegister")
    Observable<HttpResult<UserEntity>> login(@Query("phone") String phone, @Query("password") String psw);

    //获取电影接口
    @GET("top250")
    Observable<HttpResult<List<Subject>>> getTopMovie(@Query("start") int start, @Query("count") int count);

    //获取用户接口
    @GET("top250")
    Observable<HttpResult<Subject>> getUser( @Query("touken") String touken);

    //后续需要什么样的自行添加就行了。记住返回的一定要是Observable对象
}

```
然后直接
```
Observable observable = Api.getDefault().getTopMovie(start, count);
```
返回的就是第一个参数需要的对象了。

###第二个参数：
ProgressSubscriber，这里呢我们来想一个问题：在Rxjava中我们什么时候来显示Dialog呢。一开始觉得是放在Subscriber<T>的onStart中。onStart可以用作流程开始前的初始化。然而 onStart()由于在 subscribe()发生时就被调用了，因此不能指定线程，而是只能执行在 subscribe()被调用时的线程。所以onStart并不能保证永远在主线程运行。这怎么办呢！

![howtodo.jpg](http://upload-images.jianshu.io/upload_images/1780580-9f7d2cf1e8bb1ef0.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这里我们自定义一个类ProgressSubscriber继承Subscriber<T>
```
public abstract class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SimpleLoadDialog dialogHandler;
    private boolean isConnected;

    public ProgressSubscriber(Context context) {
        isConnected = NetUtils.isConnected(context);
        dialogHandler = new SimpleLoadDialog(context,this,true);
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (isConnected) { //这里自行替换判断网络的代码
            _onError("网络不可用");
        } else if (e instanceof ApiException) {
            _onError(e.getMessage());
        } else {
            _onError("请求失败，请稍后再试...");
        }
        dismissProgressDialog();
    }

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }


    /**
     * 显示Dialog
     */
    public void showProgressDialog(){
        if (dialogHandler != null) {
            dialogHandler.show();
        }
    }

    /**
     * 隐藏Dialog
     */
    private void dismissProgressDialog(){
        if (dialogHandler != null) {
            dialogHandler.dismiss();
            dialogHandler=null;
        }
    }


    protected abstract void _onNext(T t);
    protected abstract void _onError(String message);
}
```
初始化ProgressSubscriber新建了一个我们自己定义的ProgressDialog并且传入一个自定义接口ProgressCancelListener。此接口是在SimpleLoadDialog消失onCancel的时候回调的。用于终止网络请求。
ProgressSubscriber其他就很简单了，在onCompleted()和onError()的时候取消Dialog。需要的时候调用showProgressDialog即可。

###第三个参数：
缓存kay，就是一个字符串，没什么可介绍的。最好放到常量类里面。方便管理

###第四个参数：
ActivityLifeCycleEvent，简单就是一个枚举，而里面就是activity的生命周期：
```
public enum ActivityLifeCycleEvent {
    CREATE,
    START,
    RESUME,
    PAUSE,
    STOP,
    DESTROY
}

```
###第五个参数：
PublishSubject<ActivityLifeCycleEvent>，Activity生命周期管理，基本的网络请求都是向服务器请求数据，客户端拿到数据后更新UI。但也不排除意外情况，比如请求回数据途中Activity已经不在了，这个时候就应该取消网络请求。
要实现上面的功能其实很简单，两部分

随时监听Activity(Fragment)的生命周期并对外发射出去； 在我们的网络请求中，接收生命周期
并进行判断，如果该生命周期是自己绑定的，如Destory，那么就断开数据向下传递的过程
实现以上功能需要用到Rxjava的Subject的子类PublishSubject
在你的BaseActivity中添加如下代码
```
public abstract class BaseActivity<V extends IView,T extends BasePresenter<V>> extends AppCompatActivity implements IView {
    public T mPresenter;

    /**
     *  基本的网络请求都是向服务器请求数据，客户端拿到数据后更新UI。但也不排除意外情况，比如请求回数据途中Activity已经不在了，这个时候就应该取消网络请求。
     *  要实现上面的功能其实很简单，两部分
     *
     *  随时监听Activity(Fragment)的生命周期并对外发射出去； 在我们的网络请求中，接收生命周期
     *  并进行判断，如果该生命周期是自己绑定的，如Destory，那么就断开数据向下传递的过程
     *  实现以上功能需要用到Rxjava的Subject的子类PublishSubject
     *
     */

    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.CREATE);
        super.onCreate(savedInstanceState);
        mPresenter= getInstance(this,1);
        mPresenter.attachView((V) this);
    }

    @Override
    protected void onStart() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.START);
        super.onStart();
    }

    @Override
    protected void onResume() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.RESUME);
        super.onResume();
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
        if (mPresenter!=null)
        mPresenter.detachView();
    }

    @Override
    public Context getContext(){
        return this;
    }

    @Override
    public PublishSubject getLifeCycle() {
        return lifecycleSubject;
    }

    public  <T> T getInstance(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }
}

```
这样的话，我们把所有生命周期事件都传给了PublishSubject了，或者说PublishSubject已经接收到了并能够对外发射各种生命周期事件的能力了。（直接调用getLifeCycle就获取到了PublishSubject了）

现在我们要让网络请求的时候去监听这个PublishSubject，在收到相应的生命周期后取消网络请求，这又用到了我们神奇的compose(),我们需要修改handleResult代码如下
```
public static <T> Observable.Transformer<HttpResult<T>, T> handleResult(final
                                                                            ActivityLifeCycleEvent event, final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject) {
        return new Observable.Transformer<HttpResult<T>, T>() {
            @Override
            public Observable<T> call(Observable<HttpResult<T>> tObservable) {
                Observable<ActivityLifeCycleEvent> compareLifecycleObservable =
                        lifecycleSubject.takeFirst(new Func1<ActivityLifeCycleEvent, Boolean>() {
                            @Override
                            public Boolean call(ActivityLifeCycleEvent activityLifeCycleEvent) {
                                return activityLifeCycleEvent.equals(event);
                            }
                        });
                return tObservable.flatMap(new Func1<HttpResult<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(HttpResult<T> result) {
                        if (result.getCount() != 0) {
                            return createData(result.getSubjects());
                        } else {
                            return Observable.error(new ApiException(result.getCount()));
                        }
                    }
                }).takeUntil(compareLifecycleObservable).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
 }
```
调用的时候增加了两个参数一个是ActivityLifeCycleEvent 其实就是一些枚举表示Activity的生命周期
另外一个参数就是我们在BaseActivity添加的PublishSubject，这里用到了takeUntil()它的作用是监听我们创建compareLifecycleObservable
compareLifecycleObservable中就是判断了如果当前生命周期Activity
一样就发射数据，一旦compareLifecycleObservable 对外发射了数据，就自动把当前的Observable(也就是网络请求的Observable)停掉。当然有个库是专门针对这种情况的，叫<a href="https://github.com/trello/RxLifecycle">RxLifecycle</a>，不过要继承他自己的RxActivity，当然这个库不只是针对网络请求，其他所有的Rxjava都可以。有需要的可以去看看。

###第六个参数和第七个参数：
这两个参数主要是针对缓存问题的：
```
public class RetrofitCache {
    /**
     * @param cacheKey 缓存的Key
     * @param fromNetwork
     * @param isSave       是否缓存
     * @param forceRefresh 是否强制刷新
     * @param <T>
     * @return
     */
    public static <T> Observable<T> load(final String cacheKey,
                                         Observable<T> fromNetwork,
                                         boolean isSave, boolean forceRefresh) {
        Observable<T> fromCache = Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                T cache = (T) Hawk.get(cacheKey);
                if (cache != null) {
                    subscriber.onNext(cache);
                } else {
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        //是否缓存
        if (isSave) {
            /**
             * 这里的fromNetwork 不需要指定Schedule,在handleRequest中已经变换了
             */
            fromNetwork = fromNetwork.map(new Func1<T, T>() {
                @Override
                public T call(T result) {
                    Hawk.put(cacheKey, result);
                    return result;
                }
            });
        }
        //强制刷新
        if (forceRefresh) {
            return fromNetwork;
        } else {
            return Observable.concat(fromCache, fromNetwork).takeFirst(new Func1<T, Boolean>() {
                @Override
                public Boolean call(T t) {
                    return t!=null;
                }
            });
        }
    }

}
```
几个参数注释上面已经写得很清楚了，不需要过多的解释。这里我们先取了一个Observable<T>对象fromCache,里面的操作很简单，去缓存里面找个key对应的缓存，如果有就发射数据。在fromNetwork里面做的操作仅仅是缓存数据这一操作。最后判断如果强制刷新就直接返回fromNetwork反之用Observable.concat()做一个合并。concat操作符将多个Observable结合成一个Observable并发射数据。这里又用了first()。fromCache和fromNetwork任何一步一旦发射数据后面的操作都不执行。

OK，简单的使用呢到此为止了。对于需要的基础知识还是挺多了。多花时间看看敲敲，你一定会成为大神的。再次唠叨，我只是代码的搬运工。哈哈哈哈哈哈哈…………

END




