package com.hccake.ballcat.admin.oauth.config;

import com.hccake.ballcat.admin.oauth.CustomAuthenticationEntryPoint;
import com.hccake.ballcat.common.core.util.PasswordUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/25 20:13
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * 密码解析器
     * @return
     */
    @Bean
    protected PasswordEncoder passwordEncoder() {
        return PasswordUtil.ENCODER;
    }

    /**
     * 解决无法注入 authenticationManagerBean 的问题
     *
     * @return
     * @throws Exception
     */
    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint();
    }

}