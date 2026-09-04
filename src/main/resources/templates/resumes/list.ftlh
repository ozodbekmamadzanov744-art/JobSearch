<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<#assign pageTitle>
    <@spring.message "resumes.title"/>
</#assign>

<@layout.layout title=pageTitle>
    <div class="detail-band d-flex justify-content-between align-items-center flex-wrap gap-2">
        <div>
            <h1 class="h3 mb-1">
                <#if vacancyName??>
                    <@spring.messageArgs
                    "resumes.responsesTitle",
                    [vacancyName]/>
                <#else>
                    <@spring.message "resumes.heading"/>
                </#if>
            </h1>

            <p class="text-muted mb-0">
                <#if vacancyName??>
                    <#if resumes?size == 0>
                        <@spring.message "resumes.responsesEmpty"/>
                    </#if>
                <#else>
                    <@spring.message "resumes.subtitle"/>
                </#if>
            </p>
        </div>
    </div>

    <#if resumes?size == 0>
        <p class="text-muted mt-3">
            <#if vacancyName??>
                <@spring.message "resumes.responsesEmpty"/>
            <#else>
                <@spring.message "resumes.empty"/>
            </#if>
        </p>
    </#if>

    <#list resumes as resume>
        <a href="/pages/resumes/${resume.id}"
           class="text-decoration-none text-reset">

            <div class="card shadow-sm mb-3">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-start gap-3 flex-wrap">
                        <div>
                            <h2 class="h5 mb-1">
                                ${resume.name}
                            </h2>

                            <p class="text-muted mb-0">
                                <#if resume.updateTime??>
                                    <@spring.messageArgs
                                    "resumes.updated",
                                    [resume.updateTime.toString()?substring(0,10)]/>
                                <#else>
                                    <@spring.messageArgs
                                    "resumes.updated",
                                    ["—"]/>
                                </#if>
                            </p>
                        </div>

                        <div class="fw-semibold text-nowrap">
                            ${resume.salary!"—"}
                        </div>
                    </div>
                </div>
            </div>
        </a>
    </#list>

    <#if resumePage??>
        <@layout.pagination
        pageObj=resumePage
        baseUrl="/pages/resumes"
        extra=""/>
    </#if>
</@layout.layout>