package gateway;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.Cookie;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class TokenRelayFilter extends ZuulFilter {
 
	@Override
    public Object run() {
		
		RequestContext context = RequestContext.getCurrentContext();
		Cookie[] cookies = context.getRequest().getCookies();
		
		if(cookies != null) {
		   Optional<Cookie> optional = Arrays.stream(cookies)
                .filter(x -> "access_token".equals(x.getName()))
                .findFirst();
		   if(optional.isPresent()) {
              context.addZuulRequestHeader("Authorization", "Bearer " + optional.get().getValue());
           }
		}
        
        return null;
    }
 
	@Override
    public boolean shouldFilter() {
       return true;
    }
    
	@Override
	public int filterOrder() {
		return 10000;
	}

	@Override
	public String filterType() {
		return "pre";
	}
}