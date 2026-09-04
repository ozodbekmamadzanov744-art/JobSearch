<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<#assign pageTitle>
    <@spring.message "vacancy.title"/>
</#assign>

<@layout.layout title=pageTitle>
    <div class="detail-band d-flex justify-content-between align-items-start mb-4 flex-wrap gap-3">
        <div>
            <h1 class="h3 mb-1">
                ${vacancy.name}
            </h1>

            <p class="text-muted mb-0">
                <@spring.messageArgs
                "vacancy.salary",
                [vacancy.salary!"—"]/>

                ·

                <@spring.messageArgs
                "vacancy.experience",
                [vacancy.expFrom!"—", vacancy.expTo!"—"]/>

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
        </div>
    </div>

    <div class="detail-band mb-4">
        <p class="mb-0">
            ${vacancy.description!""}
        </p>
    </div>

    <#if SPRING_SECURITY_CONTEXT??
    && SPRING_SECURITY_CONTEXT.authentication.principal.user.accountType == "APPLICANT">

        <div class="detail-band mb-4">
            <h2 class="h4 mb-3">
                <@spring.message "vacancy.respond.title"/>
            </h2>

            <#if applicantResumes?? && applicantResumes?size gt 0>
                <form action="/pages/vacancies/${vacancy.id}/respond"
                      method="post"
                      class="d-flex gap-2 align-items-center flex-wrap">

                    <#if _csrf??>
                        <input type="hidden"
                               name="${(_csrf.parameterName)!'csrf'}"
                               value="${(_csrf.token)!'--no-token--'}"/>
                    </#if>

                    <select name="resumeId"
                            class="form-select"
                            style="max-width: 360px;">

                        <#list applicantResumes as resume>
                            <option value="${resume.id}">
                                ${resume.name}
                            </option>
                        </#list>
                    </select>

                    <button type="submit" class="btn btn-success">
                        <@spring.message "vacancy.respond.submit"/>
                    </button>
                </form>
            <#else>
                <p class="text-muted mb-0">
                    <@spring.message "vacancy.respond.noResume"/>
                </p>
            </#if>
        </div>
    </#if>

    <div class="detail-band">
        <h2 class="h4 mb-3">
            <@spring.message "vacancy.author"/>
        </h2>

        <#if vacancy.author??>
            <p class="mb-0">
                ${vacancy.author.name}
                <#if vacancy.author.surname??>
                    ${vacancy.author.surname}
                </#if>
            </p>
        <#else>
            <p class="text-muted mb-0">
                <@spring.message "vacancy.authorMissing"/>
            </p>
        </#if>
    </div>
</@layout.layout>