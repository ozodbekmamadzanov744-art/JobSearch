<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<#assign pageTitle>
    <@spring.message "company.title"/>
</#assign>

<@layout.layout title=pageTitle>
    <div class="detail-band d-flex align-items-center gap-3 flex-wrap">
        <img src="${company.avatar!'/images/default-avatar.png'}"
             class="rounded-circle profile-avatar"
             alt="<@spring.message 'company.avatarAlt'/>">

        <div>
            <h1 class="h3 mb-1">
                ${company.name}

                <#if company.surname??>
                    ${company.surname}
                </#if>
            </h1>

            <p class="text-muted mb-0">
                ${company.email}
            </p>

            <#if company.phoneNumber??>
                <p class="text-muted mb-0">
                    ${company.phoneNumber}
                </p>
            </#if>
        </div>
    </div>

    <h2 class="h4 mb-3">
        <@spring.message "company.vacancies"/>
    </h2>

    <#if vacancies?size == 0>
        <p class="text-muted">
            <@spring.message "company.vacanciesEmpty"/>
        </p>
    </#if>

    <#list vacancies as vacancy>
        <div class="card shadow-sm mb-3">
            <div class="card-body">
                <h3 class="h5 mb-1">
                    ${vacancy.name}
                </h3>

                <p class="text-muted mb-2">
                    <@spring.messageArgs
                    "vacancy.salary",
                    [vacancy.salary!"—"]/>

                    ·

                    <#if vacancy.updateTime??>
                        <@spring.messageArgs
                        "vacancy.updated",
                        [vacancy.updateTime.toString()?substring(0,10)]/>
                    <#else>
                        <@spring.messageArgs
                        "vacancy.updated",
                        ["—"]/>
                    </#if>
                </p>

                <p class="mb-0">
                    ${vacancy.description!""}
                </p>
            </div>
        </div>
    </#list>
</@layout.layout>