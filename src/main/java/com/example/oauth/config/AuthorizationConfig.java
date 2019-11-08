package com.example.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;

    public AuthorizationConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // API 요청 클라이언트의 정보를 설정 가능
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endPoints) {
        endPoints
                .approvalStore(approvalStore())
                .tokenStore(tokenStore());
                // .authenticationManager(authenticationManager);
    }

    /*
    * Access Token, Refresh Token과 관련된 인증데이터 CURD
    */
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    /*
     * 리소스 소유자의 승인 추가, 검색, 취소
     */
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

 /*   @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore());
    }*/

    /**
     * client_id, client_secrect 등을 저장하는 클라이언트 저장소에 대한 CRUD는
     * ClientDetailsService 인터페이스로 구현
      */
/*    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
    }*/

/*
    @Bean
    public ClientDetailsService clientDetailsService() {
        return new InMemoryClientDetailsService(); // TODO: 추후 서비스 구현 필요

    }


*/



}
