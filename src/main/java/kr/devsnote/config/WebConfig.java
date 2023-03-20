package kr.devsnote.config;

import kr.devsnote.intercepter.InterceptorAdapter;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Arrays;
import java.util.List;

/**
 * 리소스 매핑과 도메인 접근 허용 설정
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final List<String> URL_PTTERNS = Arrays.asList("/**");

    @Autowired
    @Qualifier("sqlSession")
    private SqlSessionTemplate sqlSession;
    public void addCorsMapping(CorsRegistry registry) {
        // 모든 URI에 대해 모든 도메인은 접근을 허용한다.
        registry.addMapping("/**")
                //	.allowedOriginPatterns("http://**", "https://**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(false)
                .exposedHeaders("Authorization", "Content-Disposition");
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("file:" + Constants.getPATH(Constants.UPLOADS.UPLOAD_ROOT));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new InterceptorAdapter(sqlSession)).addPathPatterns(URL_PTTERNS);
    }
    @Bean
    public Startup startup() {
        return new Startup();
    }

    @Bean
    MappingJackson2JsonView jsonView() {
        return new MappingJackson2JsonView();
    }

}
