# Servlet


## Servlet Request
### 1. 헤더정보 가져오기
````java
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
        String remoteHost = req.getRemoteHost();
        String remoteAddr = req.getRemoteAddr();
        String remoteUser = req.getRemoteUser();

        String localName = req.getLocalName();
        String localAddr = req.getLocalAddr();
        int localPort = req.getLocalPort();
    }

    private void printHeaderUtils(HttpServletRequest req) {
        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        Locale locale = req.getLocale(); // 메인 locale
        String contentType = req.getContentType();
        int contentLength = req.getContentLength();

        req.getLocales().asIterator().forEachRemaining((lc) -> System.out.println("locale = " + lc));
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("cookie = " + cookie);
            }
        }
    }

    private void printHeaderInfo(HttpServletRequest req) {
        req.getHeaderNames().asIterator().forEachRemaining((name) -> System.out.println("headerName = " + name));
    }

    private void printRequestInfo(HttpServletRequest req) {
        String method = req.getMethod(); // GET, POST...
        String protocol = req.getProtocol(); // HTTP/1.1...
        String scheme = req.getScheme();  // HTTP...
        StringBuffer requestURL = req.getRequestURL(); // "http://localhost:8080/hello"
        String requestURI = req.getRequestURI(); // "/hello"
        String queryString = req.getQueryString(); 
        boolean secure = req.isSecure(); // https여부
    }
}

````

### 2. 쿼리 파라미터 가져오기 
   - GET + Query param || POST + x-www-form-urlencoded 모두 가져올 수 있다.
   - 값이 없을 경우 null 날라옴
````java
@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        printRequestQuery(req, resp);
    }

    private void printRequestQuery(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("전부 ----");
        req.getParameterNames().asIterator().forEachRemaining((param) -> System.out.println("param = " + param + " = " + req.getParameter(param)));

        log.info("단일 ----");
        String username = req.getParameter("username");
        String age = req.getParameter("age");
        
        log.info("복수 ----");
        String[] usernames = req.getParameterValues("username");
        for (String user : usernames) {
            System.out.println("user = " + user);
        }
        
        resp.getWriter().write("OK");
    }
}
````

### 3. Body를 String으로 받기
- GET, POST 모두 값이 받아짐
- x-www-form-urlencoded로도 값이 받아짐
````java
@WebServlet(name = "requestBodyStringServlet", urlPatterns = "/request-body-string")
public class RequestBodyStringServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            ServletInputStream inputStream = req.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

            log.info("messageBody = {}", messageBody);
            resp.getWriter().write("OK");
    }
}
````

### 4. Body JSON으로 받기
- Spring에서 제공하는 JSON 변환 라이브러리를 통해 변환 할 수 있음- Jackson
  +  다른 라이브러리로 Gson 있음 
````java
@WebServlet(name = "requestBodyJsonServlet", urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet extends HelloServlet {

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환 라이브러리

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletInputStream inputStream = req.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class); // 변환

        log.info("username = {}", helloData.getUsername());
        log.info("age = {}", helloData.getAge());

        resp.getWriter().write("OK");
    }
}
````

## Servlet Response
### 1. 응답에 정보 추가하기
````java
    @WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
    public class ResponseHeaderServlet extends HelloServlet {

        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setStatus(HttpServletResponse.SC_OK); // status 설정 
           
            // charset을 선언해주지 않으면 "ISO-8859-1"로 설정됨
            resp.setHeader("Content-Type", "text/plain;charset=utf-8");
            
            // 캐쉬를 무효화함
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            
            resp.setHeader("my-header", "hello"); // custom 헤더도 추가할 수 있음

            resp.getWriter().write("OK");
        }
        
        private void redirect(HttpServletResponse resp) throws IOException {
            resp.setStatus(HttpServletResponse.SC_FOUND); // stauts 302
            resp.sendRedirect("/basic/hello-form.html"); // 해당 html 파일로 리다이렉트 시킴
            
            
            // "Location"을 통해 직접 리다이렉트 주소를 정해줄 수도 있음
            // resp.setHeader("Location", "/basic/hello-form.html") 
        }

        // 쿠키를 추가
        private void cookie(HttpServletResponse resp) {
            Cookie cookie = new Cookie("myCookie", "good"); 
            cookie.setMaxAge(600);
            resp.addCookie(cookie);
        }

        // setHeader 대신 값을 추가할 수 있음
        private void content(HttpServletResponse resp) {
            resp.setContentType("text/plain");
            resp.setCharacterEncoding("utf-8");
        }
    }
````
### 2. HTML을 응답하기
````java
@WebServlet(name = "responseHtmlServlet", urlPatterns = "/response-html")
public class ResponseHtmlServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        // 없어도 되지만 없을 경우 한글로 인코딩이 되지 않을 수도 있음
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");

        PrintWriter writer = resp.getWriter();
        writer.println("<html>");
        writer.println("<body>");
        writer.println("<div>안녕</div>");
        writer.println("</body>");
        writer.println("</html>");
    }
}
````

### 3. JSON 응답하기
+ application/json은 charset=utf-8 같은 파라미터를 지원하지 않음
  + resp.getWriter()를 사용하면 자동으로 추가되는데 resp.getOutputStream()으로 해결 가능하다
  + utf-8을 지정해주지 않으면 한글 깨짐 Why?
````java
@WebServlet(name = "responseJsonServlet", urlPatterns = "/response-json")
@RequiredArgsConstructor
public class ResponseJsonServlet extends HelloServlet {

    private final ObjectMapper objectMapper;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        HelloData helloData = new HelloData();
        helloData.setUsername("kim");
        helloData.setAge(20);

        String result = objectMapper.writeValueAsString(helloData); // 객체를 String으로 변환
        resp.getWriter().write(result);
    }
}
````
## Servlet with JSP
### 1. Servlet MVC
   + WEB-INF 디렉토리 하위에 JSP 파일 생성 시 컨트롤러를 통해서만 접근할 수 있다
   + Redriect => 클라이언트에게 갔다가 다시 호출 / Forword => 서버 내부에서 호출

#### -> View와 Service logic을 분리할 수 있지만 아직 공통 처리가 어렵다.
````java
@WebServlet(name = "mvcMemberSaveServlet", urlPatterns = "/servlet-mvc/members/save")
public class MvcMemberSaveServlet extends HttpServlet {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        int age = Integer.parseInt(req.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        req.setAttribute("member", member); // 데이터를 JSP에 전달 -> getAttribute || ${}과 같은 JSP 문법

        String viewPath = "/WEB-INF/views/save-result.jsp";
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewPath);
        requestDispatcher.forward(req, resp);
    }
}
````