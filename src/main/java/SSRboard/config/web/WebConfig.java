package SSRboard.config.web;

import SSRboard.config.web.interceptor.LogInterceptor;
import SSRboard.config.web.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 비로그인 상태일때 접근 가능한 컨트롤러 추가.
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LogInterceptor())
                .order(1).addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/member", "/member/join", "/member/login", "/member/joinPage",
                        "/board/detail/{boardId}", "/page/**",
                        "/css/**", "/*.ico", "/error");
    }
}
