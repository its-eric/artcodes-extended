package Services.Authorization;
import android.text.TextUtils;


/**
 * Created by gxsha on 11/22/2018.
 */

public class AuthorizationHandler extends BaseClient {

    public static String authToken;

    public static <S> S createService(Class<S> serviceClass) {
        if (!TextUtils.isEmpty(authToken)) {
            return createService(serviceClass, authToken);
        }
        return createService(serviceClass, null);
    }

    private static <S> S createService(
            Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }
}
