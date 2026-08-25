package kg.attractor.jobsearch.util;

import jakarta.servlet.http.HttpServletRequest;

public final class Utility {

    private Utility() {
    }

    /**
     * Возвращает базовый адрес сайта (схема + хост + порт), без пути текущего запроса.
     * Используется для формирования абсолютных ссылок, например в письмах.
     */
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
