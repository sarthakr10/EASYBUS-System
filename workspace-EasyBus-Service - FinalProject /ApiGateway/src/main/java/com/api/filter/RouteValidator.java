package com.api.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

@Component
public class RouteValidator {
	
	private static final Logger logger = LoggerFactory.getLogger(RouteValidator.class);

    // List of open API endpoints that do not require authentication
    public static final List<String> openApiEndpoints = List.of(
            "/auth/register",
            "/auth/token",
            "/auth/validate"
    );

    // Predicate to check if a request is secured
    public Predicate<ServerHttpRequest> isSecured =
    		request -> {
                String path = request.getURI().getPath();
                boolean isSecured = openApiEndpoints.stream().noneMatch(uri -> path.contains(uri));
                if (!isSecured) {
                    logger.info("Unsecured access: {}", path);
                }
                return isSecured;
            };

}

