package interceptor;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

import java.util.Map;

public class AuthInterceptor implements Interceptor {
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        Map<String, Object> session = invocation.getInvocationContext().getSession();

        if (session.containsKey("user_id")) {
            return invocation.invoke();
        } else {
            return "auth_failed";
        }
    }

    @Override
    public void init() { }

    @Override
    public void destroy() { }
}