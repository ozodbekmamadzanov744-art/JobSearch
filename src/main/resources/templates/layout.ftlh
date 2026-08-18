<#import "/spring.ftl" as spring>
<#macro layout title="JobSearch">
    <!doctype html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>${title} — JobSearch</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
    </head>
    <body>
    <#assign known = SPRING_SECURITY_CONTEXT??>
    <#if known>
        <#assign currentUser = SPRING_SECURITY_CONTEXT.authentication.principal>
    </#if>
    <nav class="navbar navbar-expand-lg bg-primary mb-4" data-bs-theme="dark">
        <div class="container">
            <a class="navbar-brand" href="/pages/vacancies">JobSearch</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item"><a class="nav-link" href="/pages/vacancies">Вакансии</a></li>
                    <#if known && currentUser.user.accountType == "EMPLOYER">
                        <li class="nav-item"><a class="nav-link" href="/pages/resumes">Резюме</a></li>
                    <#elseif known && currentUser.user.accountType == "APPLICANT">
                        <li class="nav-item"><a class="nav-link" href="/pages/companies">Компании</a></li>
                    </#if>
                </ul>
            </div>

            <#if known>
                <p class="text-light m-2 mb-0">Привет, ${currentUser.username}</p>
                <a href="/pages/cabinet" class="btn btn-outline-light m-1">Личный кабинет</a>
                <a href="/logout" class="btn btn-outline-danger m-1">Выйти</a>
            <#else>
                <a href="/pages/auth/login" class="btn btn-outline-light m-1">Войти</a>
                <a href="/pages/auth/register" class="btn btn-outline-info m-1">Регистрация</a>
            </#if>
        </div>
    </nav>
    <div class="container">
        <#nested>
    </div>
    </body>
    </html>
</#macro>

<#-- Универсальная постраничная навигация для org.springframework.data.domain.Page -->
<#macro pagination pageObj baseUrl extra="">
    <#if pageObj.totalPages gt 1>
        <nav aria-label="Постраничная навигация" class="mt-3">
            <ul class="pagination">
                <li class="page-item <#if pageObj.first>disabled</#if>">
                    <a class="page-link" href="${baseUrl}?page=${pageObj.number - 1}${extra}">&laquo; Назад</a>
                </li>
                <#list 0..<pageObj.totalPages as i>
                    <li class="page-item <#if i == pageObj.number>active</#if>">
                        <a class="page-link" href="${baseUrl}?page=${i}${extra}">${i + 1}</a>
                    </li>
                </#list>
                <li class="page-item <#if pageObj.last>disabled</#if>">
                    <a class="page-link" href="${baseUrl}?page=${pageObj.number + 1}${extra}">Вперёд &raquo;</a>
                </li>
            </ul>
        </nav>
    </#if>
</#macro>