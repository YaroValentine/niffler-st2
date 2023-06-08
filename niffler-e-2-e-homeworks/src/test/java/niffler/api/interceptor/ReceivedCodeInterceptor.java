package niffler.api.interceptor;

import niffler.api.context.SessionContext;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class ReceivedCodeInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        String location = response.header("Location");
        if (location != null && location.contains("code=")) {
            SessionContext.getInstance().setCode(StringUtils.substringAfter(location, "code="));
        }
        return response;
    }

}
