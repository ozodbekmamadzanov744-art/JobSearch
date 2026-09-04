<#import "/spring.ftl" as spring>

<#macro layout title="JobSearch">
    <!doctype html>
    <html lang="${springMacroRequestContext.locale.language}">
    <head>
        <meta charset="UTF-8">
        <title>${title} — <@spring.message "app.name"/></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="/styles/app.css">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
    </head>

    <body class="app-shell">
    <#assign known = SPRING_SECURITY_CONTEXT??>

    <#if known>
        <#assign currentUser = SPRING_SECURITY_CONTEXT.authentication.principal>
    </#if>

    <nav class="navbar navbar-expand-lg app-navbar" data-bs-theme="dark">
        <div class="container">
            <a class="navbar-brand fw-semibold" href="/pages/vacancies">
                <@spring.message "app.name"/>
            </a>

            <div class="collapse navbar-collapse">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="/pages/vacancies">
                            <@spring.message "nav.vacancies"/>
                        </a>
                    </li>

                    <#if known && currentUser.user.accountType == "EMPLOYER">
                        <li class="nav-item">
                            <a class="nav-link" href="/pages/resumes">
                                <@spring.message "nav.resumes"/>
                            </a>
                        </li>
                    <#elseif known && currentUser.user.accountType == "APPLICANT">
                        <li class="nav-item">
                            <a class="nav-link" href="/pages/companies">
                                <@spring.message "nav.companies"/>
                            </a>
                        </li>
                    </#if>
                </ul>
            </div>

            <div class="btn-group btn-group-sm m-1" role="group"
                 aria-label="<@spring.message 'language.title'/>">
                <a href="?lang=ru" class="btn btn-outline-light <#if springMacroRequestContext.locale.language == 'ru'>active</#if>">
                    RU
                </a>
                <a href="?lang=en" class="btn btn-outline-light <#if springMacroRequestContext.locale.language == 'en'>active</#if>">
                    EN
                </a>
            </div>

            <#if known>
                <p class="text-light m-2 mb-0 small text-nowrap">
                    <@spring.messageArgs "user.greeting", [currentUser.username]/>
                </p>

                <a href="/pages/cabinet" class="btn btn-outline-light btn-sm m-1">
                    <@spring.message "nav.cabinet"/>
                </a>

                <form action="/logout" method="post" class="m-1 d-inline">
                    <#if _csrf??>
                        <input type="hidden"
                               name="${(_csrf.parameterName)!'csrf'}"
                               value="${(_csrf.token)!'--no-token--'}"/>
                    </#if>

                    <button type="submit" class="btn btn-outline-danger btn-sm">
                        <@spring.message "auth.logout"/>
                    </button>
                </form>
            <#else>
                <a href="/pages/auth/login" class="btn btn-outline-light btn-sm m-1">
                    <@spring.message "auth.login"/>
                </a>

                <a href="/pages/auth/register" class="btn btn-light btn-sm m-1">
                    <@spring.message "auth.register"/>
                </a>
            </#if>
        </div>
    </nav>

    <div class="container app-content">
        <#nested>
    </div>
    </body>
    </html>
</#macro>

<#macro pagination pageObj baseUrl extra="">
    <#if pageObj.totalPages gt 1>
        <nav aria-label="<@spring.message 'pagination.label'/>" class="mt-4">
            <ul class="pagination pagination-sm justify-content-center flex-wrap">
                <li class="page-item <#if pageObj.first>disabled</#if>">
                    <a class="page-link"
                       href="${baseUrl}?page=${pageObj.number - 1}${extra}">
                        &laquo; <@spring.message "pagination.previous"/>
                    </a>
                </li>

                <#list 0..<pageObj.totalPages as i>
                    <li class="page-item <#if i == pageObj.number>active</#if>">
                        <a class="page-link"
                           href="${baseUrl}?page=${i}${extra}">
                            ${i + 1}
                        </a>
                    </li>
                </#list>

                <li class="page-item <#if pageObj.last>disabled</#if>">
                    <a class="page-link"
                       href="${baseUrl}?page=${pageObj.number + 1}${extra}">
                        <@spring.message "pagination.next"/> &raquo;
                    </a>
                </li>
            </ul>
        </nav>
    </#if>
</#macro>
