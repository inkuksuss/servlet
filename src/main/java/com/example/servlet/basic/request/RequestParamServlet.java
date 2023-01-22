package com.example.servlet.basic.request;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * parameter transfer
 */
@Slf4j
@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        printRequestQuery(req, resp);
    }

    private void printRequestQuery(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("전부 ----");
        req.getParameterNames().asIterator().forEachRemaining((param) -> System.out.println("param = " + param + " = " + req.getParameter(param)));
        String username = req.getParameter("username");
        String age = req.getParameter("age");
        log.info("단일 ----");
        System.out.println("username = " + username);
        System.out.println("age = " + age);
        log.info("복수 ----");
        String[] usernames = req.getParameterValues("username");
        for (String user : usernames) {
            System.out.println("user = " + user);
        }
        resp.getWriter().write("OK");
    }
}
