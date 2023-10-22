package org.example;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/time")
public class TimezoneValidateFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String timezoneParam = request.getParameter("timezone");

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (isValidTimezone(timezoneParam)) {
            chain.doFilter(request, response);
        } else {
            response.setContentType("text/html; charset=utf-8");
            httpResponse.setCharacterEncoding("UTF-8");
            response.getWriter().write("<h1>Invalid timezone</h1>");
            ((HttpServletResponse) response).setStatus(400);
            response.getWriter().close();
        }
    }

    private boolean isValidTimezone(String timezoneParam) {
        final List<String> UTCTimezones = Arrays.asList(
                "UTC 0", "UTC 1", "UTC 2", "UTC 3", "UTC 4", "UTC 5", "UTC 6", "UTC 7", "UTC 8", "UTC 9", "UTC 10",
                "UTC 11", "UTC 12", "UTC 13", "UTC-1", "UTC-2", "UTC-3", "UTC-4", "UTC-5", "UTC-6", "UTC-7", "UTC-8",
                "UTC-9", "UTC-10", "UTC-11", "UTC-12"
        );
        return UTCTimezones.contains(timezoneParam) || timezoneParam == null;

    }
}