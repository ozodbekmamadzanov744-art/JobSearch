<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<#assign pageTitle>
    <@spring.message "companies.title"/>
</#assign>

<@layout.layout title=pageTitle>
    <div class="detail-band mb-4">
        <h1 class="h3 mb-1">
            <@spring.message "companies.heading"/>
        </h1>

        <p class="text-muted mb-0">
            <@spring.message "companies.subtitle"/>
        </p>
    </div>

    <#if companies?size == 0>
        <p class="text-muted">
            <@spring.message "companies.empty"/>
        </p>
    </#if>

    <#list companies as company>
        <a href="/pages/companies/${company.id}"
           class="text-decoration-none text-reset">

            <div class="card shadow-sm mb-3">
                <div class="card-body d-flex align-items-center gap-3">
                    <img src="${company.avatar!'/images/default-avatar.png'}"
                         class="rounded-circle"
                         width="56"
                         height="56"
                         style="object-fit: cover;"
                         alt="<@spring.message 'company.avatarAlt'/>">

                    <div>
                        <h5 class="card-title mb-1">
                            ${company.name}

                            <#if company.surname??>
                                ${company.surname}
                            </#if>
                        </h5>

                        <p class="card-text text-muted mb-0">
                            ${company.email}
                        </p>
                    </div>
                </div>
            </div>
        </a>
    </#list>

    <#if companyPage??>
        <@layout.pagination
        pageObj=companyPage
        baseUrl="/pages/companies"
        extra=""/>
    </#if>
</@layout.layout>