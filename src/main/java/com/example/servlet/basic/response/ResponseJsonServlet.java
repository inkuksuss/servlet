package com.example.servlet.basic.response;

import com.example.servlet.basic.HelloData;
import com.example.servlet.basic.HelloServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@WebServlet(name = "responseJsonServlet", urlPatterns = "/response-json")
@RequiredArgsConstructor
public class ResponseJsonServlet extends HelloServlet {

    private final ObjectMapper objectMapper;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        HelloData helloData = new HelloData();
        helloData.setUsername("인국");
        helloData.setAge(20);

        String result = objectMapper.writeValueAsString(helloData);
        resp.getWriter().write(result);
    }
}
