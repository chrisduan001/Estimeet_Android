package estimeet.meetup.di.Modules;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import estimeet.meetup.R;
import estimeet.meetup.gsonconverter.BoolConverter;
import estimeet.meetup.gsonconverter.DateTimeConverter;
import estimeet.meetup.gsonconverter.DoubleConverter;
import estimeet.meetup.gsonconverter.IntConverter;
import estimeet.meetup.gsonconverter.StringConverter;
import estimeet.meetup.network.EstimeetApi;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

/**
 * Created by AmyDuan on 6/02/16.
 */
@Module
public class ServiceModule {

    private static final String API_URL = "https://estimeetprojapi.azurewebsites.net/api/register";
    private static final String TEST_URL = "https://estimeetapi.azurewebsites.net/api/";

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(String.class, new StringConverter())
                .registerTypeAdapter(int.class, new IntConverter())
                .registerTypeAdapter(double.class, new DoubleConverter())
                .registerTypeAdapter(boolean.class, new BoolConverter())
                .registerTypeAdapter(Date.class, new DateTimeConverter())
                .create();
    }
//    Following method was used by retrofit 2.0 +
//    @Provides @Singleton
//    public RestAdapter provideRetrofit(Gson gson) {
//        OkHttp client = new OkHttpClient();
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
//        client.interceptors().add(interceptor);
//
//        return new Retrofit.Builder()
//                .baseUrl(API_URL)
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .client(client)
//                .build();
//    }
//
//    @Provides @Singleton
//    public EstimeetApi provideRestApi(Retrofit retrofit) {
//        return retrofit.create(EstimeetApi.class);
//    }

    @Provides @Singleton
    public RestAdapter provideRestAdapter(Context applicationContext, Gson gson) {
        final Resources r = applicationContext.getResources();
        final ErrorHandler errorHandler = new ErrorHandler() {
            @Override
            public Throwable handleError(RetrofitError cause) {
                String message;
                switch (cause.getKind()) {
                    case NETWORK:
                        message = r.getString(R.string.network_error);
                        break;
                    default:
                        message = r.getString(R.string.http_generic_error);
                        break;
                }
                return new RuntimeException(message, cause);
            }
        };

        return new RestAdapter.Builder()
                .setEndpoint(TEST_URL)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS)
                .setErrorHandler(errorHandler)
                .build();
    }

    @Provides @Singleton
    public EstimeetApi provideEstimeetApi(RestAdapter restAdapter) {
        return restAdapter.create(EstimeetApi.class);
    }
}
