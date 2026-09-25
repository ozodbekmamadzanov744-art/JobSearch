<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<#assign pageTitle><@spring.message "responses.title"/></#assign>

<@layout.layout title=pageTitle>
    <div class="detail-band mb-4">
        <h1 class="h3 mb-1"><@spring.message "responses.title"/></h1>
        <p class="text-muted mb-0"><@spring.message "responses.subtitle"/></p>
    </div>

    <#if responses?size == 0>
        <p class="text-muted"><@spring.message "responses.empty"/></p>
    </#if>

    <#list responses as response>
        <div class="card shadow-sm mb-3">
            <div class="card-body d-flex justify-content-between align-items-center gap-3 flex-wrap">
                <div>
                    <h2 class="h5 mb-1">
                        ${response.vacancy.name!"—"}
                    </h2>
                    <p class="text-muted mb-0">
                        ${response.resume.name!"—"}
                    </p>
                </div>

                <a href="/pages/chats/${response.id}" class="btn btn-outline-primary btn-sm">
                    <@spring.message "chat.open"/>
                </a>
            </div>
        </div>
    </#list>
</@layout.layout>
