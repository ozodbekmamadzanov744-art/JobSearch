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

    <#if categories??>
        <form method="get" class="card card-body mb-4">
            <div class="row g-2 align-items-end">
                <div class="col-md-8">
                    <label class="form-label" for="resume-category-filter">
                        <@spring.message "resumes.filter.category"/>
                    </label>
                    <select id="resume-category-filter" name="categoryId" class="form-select">
                        <option value=""><@spring.message "resumes.filter.allCategories"/></option>
                        <#list categories?keys as key>
                            <option value="${key}" <#if currentCategoryId?? && currentCategoryId?string == key>selected</#if>>
                                ${categories[key]}
                            </option>
                        </#list>
                    </select>
                </div>
                <div class="col-md-4">
                    <button type="submit" class="btn btn-primary">
                        <@spring.message "resumes.filter.apply"/>
                    </button>
                </div>
            </div>
        </form>
    </#if>

    <#if resumes?size == 0>
        <p class="text-muted mt-3">
            <#if vacancyName??>
                <@spring.message "resumes.responsesEmpty"/>
            <#else>
                <@spring.message "resumes.empty"/>
            </#if>
        </p>
    </#if>

    <#if responses??>
        <#list responses as response>
            <div class="card shadow-sm mb-3">
                <div class="card-body d-flex justify-content-between align-items-center gap-3 flex-wrap">
                    <div>
                        <h2 class="h5 mb-1">
                            ${response.resume.name!"—"}
                        </h2>
                        <p class="text-muted mb-0">
                            <#if response.resume.applicant??>
                                ${response.resume.applicant.name}
                            </#if>
                        </p>
                    </div>

                    <div class="d-flex gap-2">
                        <a href="/pages/resumes/${response.resume.id}" class="btn btn-outline-primary btn-sm">
                            <@spring.message "resume.title"/>
                        </a>
                        <a href="/pages/chats/${response.id}" class="btn btn-outline-secondary btn-sm">
                            <@spring.message "chat.open"/>
                        </a>
                    </div>
                </div>
            </div>
        </#list>
    <#else>
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
    </#if>

    <#assign resumePaginationExtra="">
    <#if currentCategoryId??>
        <#assign resumePaginationExtra="&categoryId=${currentCategoryId}">
    </#if>

    <#if resumePage??>
        <@layout.pagination
        pageObj=resumePage
        baseUrl="/pages/resumes"
        extra=resumePaginationExtra/>
    </#if>
</@layout.layout>
