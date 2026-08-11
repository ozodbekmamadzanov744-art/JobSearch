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
    <nav class="navbar navbar-expand-lg bg-primary mb-4" data-bs-theme="dark">
        <div class="container">
            <a class="navbar-brand" href="/pages/vacancies">JobSearch</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item"><a class="nav-link" href="/pages/vacancies">Вакансии</a></li>
                    <li class="nav-item"><a class="nav-link" href="/pages/resumes">Резюме</a></li>
                </ul>
            </div>

            <#assign known = SPRING_SECURITY_CONTEXT??>
            <#if known>
                <#assign user = SPRING_SECURITY_CONTEXT.authentication.principal>
                <p class="text-light m-2 mb-0">Привет, ${user.username}</p>
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