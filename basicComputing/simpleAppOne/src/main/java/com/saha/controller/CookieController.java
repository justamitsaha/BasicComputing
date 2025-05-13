package com.saha.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class CookieController {

    Log log = LogFactory.getLog(CookieController.class);

    @GetMapping("/api/set-cookie")
    public String setCookie(HttpServletResponse response) {

        String visibleValue = UUID.randomUUID().toString();
        String httpOnlyValue = UUID.randomUUID().toString();
        String thirdPartyValue = UUID.randomUUID().toString();

        // JS-readable cookie
        Cookie jsCookie = new Cookie("JS_COOKIE", visibleValue);
        jsCookie.setPath("/");
        jsCookie.setMaxAge(3600);
        jsCookie.setHttpOnly(false);
        jsCookie.setSecure(false); // set true for HTTPS
        response.addCookie(jsCookie);

        // HttpOnly cookie (not accessible via JS)
        Cookie httpOnlyCookie = new Cookie("HTTP_ONLY_COOKIE", httpOnlyValue);
        httpOnlyCookie.setPath("/");
        httpOnlyCookie.setMaxAge(3600);
        httpOnlyCookie.setHttpOnly(true);
        httpOnlyCookie.setSecure(false); // set true for HTTPS
        response.addCookie(httpOnlyCookie);

        Cookie crossSiteCookie = new Cookie("THIRD_PARTY_COOKIE", UUID.randomUUID().toString());
        crossSiteCookie.setPath("/");
        crossSiteCookie.setMaxAge(3600);
        crossSiteCookie.setHttpOnly(false); // JS can access if needed
        crossSiteCookie.setSecure(true);    // MUST be true for SameSite=None
        crossSiteCookie.setDomain("localhost"); // optional, since same machine
        crossSiteCookie.setAttribute("SameSite", "None"); // Set manually if needed

        // Cross-site cookie (must use header for SameSite=None)
        response.addHeader("Set-Cookie", "THIRD_PARTY_COOKIE=" + thirdPartyValue +
                "; Max-Age=3600; Path=/; Domain=api.com; Secure; SameSite=None");

        log.info(jsCookie.getName() + jsCookie.getValue());
        log.info(httpOnlyCookie.getName() + httpOnlyCookie.getValue());
        log.info(crossSiteCookie.getName() + crossSiteCookie.getValue());

        return "Cookies have been set!";
    }

}
