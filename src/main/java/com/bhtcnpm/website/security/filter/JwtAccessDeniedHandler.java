package com.bhtcnpm.website.security.filter;

import com.bhtcnpm.website.constant.security.SecurityConstant;
import com.bhtcnpm.website.model.dto.CustomHttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //We have two options:
        //1. We will honestly return forbidden status code to inform user that they don't have enough permission to interact with resources.
        //2. We will hide the fact that this resource really exist.
        //But Front End needs to implement information hiding too. By seperating functionality (guest apart from admin).
        CustomHttpResponse httpResponse
                = CustomHttpResponse.builder()
                .httpStatusCode(HttpStatus.FORBIDDEN.value())
                .httpStatus(HttpStatus.FORBIDDEN)
                .reason(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message(SecurityConstant.ACCESS_DENIED_MESSAGE)
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, httpResponse);
        outputStream.flush();
    }
}
