package estimeet.meetup.di.Modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import estimeet.meetup.gsonconverter.BoolConverter;
import estimeet.meetup.gsonconverter.DateTimeConverter;
import estimeet.meetup.gsonconverter.DoubleConverter;
import estimeet.meetup.gsonconverter.IntConverter;
import estimeet.meetup.gsonconverter.StringConverter;
import estimeet.meetup.network.EstimeetApi;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by AmyDuan on 6/02/16.
 */
@Module
public class ServiceModule {

    private static final String API_URL = "https://estimeetprojapi.azurewebsites.net/api/register/";
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

    @Provides @Singleton
    public Retrofit provideRetrofit(Gson gson) {
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client.interceptors().add(interceptor);

        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    @Provides @Singleton
    public EstimeetApi provideRestApi(Retrofit retrofit) {
        return retrofit.create(EstimeetApi.class);
    }
}
