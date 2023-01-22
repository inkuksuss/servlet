package com.example.servlet.basic.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Locale;

@WebServlet(name = "requestHeaderServlet", urlPatterns = "/request-header")
public class RequestHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        printRequestInfo(req);
        printHeaderInfo(req);
        printHeaderUtils(req);
        printEtcInfo(req);
    }

    private void printEtcInfo(HttpServletRequest req) {
        System.out.println("----ETC INFO----");
        String remoteHost = req.getRemoteHost();
        String remoteAddr = req.getRemoteAddr();
        String remoteUser = req.getRemoteUser();

        String localName = req.getLocalName();
        String localAddr = req.getLocalAddr();
        int localPort = req.getLocalPort();

        System.out.println("remoteHost = " + remoteHost);
        System.out.println("remoteAddr = " + remoteAddr);
        System.out.println("remoteUser = " + remoteUser);
        System.out.println("localName = " + localName);
        System.out.println("localAddr = " + localAddr);
        System.out.println("localPort = " + localPort);
        System.out.println();
    }

    private void printHeaderUtils(HttpServletRequest req) {
        System.out.println("----HEADER UTILS----");
        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        Locale locale = req.getLocale();
        String contentType = req.getContentType();
        int contentLength = req.getContentLength();

        System.out.println("serverName = " + serverName);
        System.out.println("serverPort = " + serverPort);
        req.getLocales().asIterator().forEachRemaining((lc) -> System.out.println("locale = " + lc));
        System.out.println("locale = " + locale);
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("cookie = " + cookie);
            }
        }
        System.out.println("contentType = " + contentType);
        System.out.println("contentLength = " + contentLength);
        System.out.println();
    }

    private void printHeaderInfo(HttpServletRequest req) {
        System.out.println("----HEADER----");
        req.getHeaderNames().asIterator().forEachRemaining((name) -> System.out.println("headerName = " + name));
        System.out.println();
    }

    private void printRequestInfo(HttpServletRequest req) {
        System.out.println("----INFO----");
        String method = req.getMethod();
        String protocol = req.getProtocol();
        String scheme = req.getScheme();
        StringBuffer requestURL = req.getRequestURL();
        String requestURI = req.getRequestURI();
        String queryString = req.getQueryString();
        boolean secure = req.isSecure();

        System.out.println("method = " + method);
        System.out.println("protocol = " + protocol);
        System.out.println("scheme = " + scheme);
        System.out.println("requestURL = " + requestURL);
        System.out.println("requestURI = " + requestURI);
        System.out.println("queryString = " + queryString);
        System.out.println("secure = " + secure);
        System.out.println();
    }
}
