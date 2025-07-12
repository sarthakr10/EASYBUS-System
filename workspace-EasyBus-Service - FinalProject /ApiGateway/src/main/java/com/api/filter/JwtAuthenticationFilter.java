package com.api.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import com.api.util.Jwtutil;

import org.springframework.http.HttpMethod;

@Component
public class JwtAuthenticationFilter extends  AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config>  {

    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    @Autowired
    private Jwtutil jwtUtil;

    @Autowired
    private RouteValidator validator;
    
      
        public JwtAuthenticationFilter() {
            super(Config.class);
        }
        
        @Override
        public GatewayFilter apply(Config config) {
            return ((exchange, chain) -> {
                if (validator.isSecured.test(exchange.getRequest())) {
                    if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                        logger.error("Missing Authorization header");
                        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }

                    String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                    HttpMethod method = exchange.getRequest().getMethod();

                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        authHeader = authHeader.substring(7);
                    }

                    try {
                        jwtUtil.validateToken(authHeader);
                        String role = jwtUtil.extractRole(authHeader);
                        String path = exchange.getRequest().getURI().getPath();

                        if (!checkRoleAccess(role, path, method)) {
                            logger.error("Unauthorized access: Role '{}' does not have access to path '{}'", role, path);
                            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }
                    } catch (Exception e) {
                        logger.error("Invalid token or access: {}", e.getMessage());
                        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }
                }
                return chain.filter(exchange);
            });
        } 
       
        private boolean checkRoleAccess(String role, String path,HttpMethod method) {
        	 if (role.equals("ADMIN")) {
                 return true; // Admin can access everything
             } else if (role.equals("USER")) {
              
                 return path.startsWith("/bus/get") || 
                		 path.startsWith("/booking/add")||
                		path.startsWith("/booking/get") ||
                		path.startsWith("/booking/cancelBooking")||
                		path.startsWith("/passenger/getByBookingId")
                 	    && method.equals(HttpMethod.GET);
                
                     
             } 
             return false; // Default: deny access
        }

        public static class Config {

        }
 }
 


