package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {
    public static final int MILLISECONDS_IN_HOUR = 3600000;
    public static final String COOKIE_NAME = "lastTimeZone";
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("webapps/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {



        String timezoneParam = request.getParameter("timezone");


        if (timezoneParam != null) {
            timezoneParam = URLEncoder.encode(timezoneParam, "UTF-8");
            Cookie cookie = new Cookie(COOKIE_NAME, timezoneParam);
            response.addCookie(cookie);
        }

        String lastTimeZone = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    lastTimeZone = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    break;
                }
            }
        }

        TimeZone timezone = TimeZone.getTimeZone("UTC");

        if (timezoneParam  != null) {
            String stringZoneID = timezoneParam.substring(3).trim();

            int zoneId = Integer.parseInt(stringZoneID);
            timezone.setRawOffset(zoneId * MILLISECONDS_IN_HOUR);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        dateFormat.setTimeZone(timezone);
        String currentTime = dateFormat.format(new Date());

        Context context = new Context();
        context.setVariable("currentTime", currentTime);
        engine.process("time", context, response.getWriter());
    }
}