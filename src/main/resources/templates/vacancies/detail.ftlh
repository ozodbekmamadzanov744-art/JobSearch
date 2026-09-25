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
            <#if applicantResumes?? && applicantResumes?size gt 0>
                <button type="button"
                        class="btn btn-success"
                        data-bs-toggle="modal"
                        data-bs-target="#respondModal">
                    <@spring.message "vacancy.respond.submit"/>
                </button>
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

    <#if SPRING_SECURITY_CONTEXT??
    && SPRING_SECURITY_CONTEXT.authentication.principal.user.accountType == "APPLICANT"
    && applicantResumes?? && applicantResumes?size gt 0>
        <div class="modal fade" id="respondModal" tabindex="-1" aria-labelledby="respondModalTitle" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2 class="modal-title h5" id="respondModalTitle">
                            <@spring.message "vacancy.respond.title"/>
                        </h2>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="<@spring.message 'common.close'/>"></button>
                    </div>

                    <form action="/pages/vacancies/${vacancy.id}/respond" method="post">
                        <div class="modal-body">
                            <#if _csrf??>
                                <input type="hidden"
                                       name="${(_csrf.parameterName)!'csrf'}"
                                       value="${(_csrf.token)!'--no-token--'}"/>
                            </#if>

                            <label class="form-label" for="resumeId">
                                <@spring.message "vacancy.respond.resume"/>
                            </label>
                            <select id="resumeId" name="resumeId" class="form-select">
                                <#list applicantResumes as resume>
                                    <option value="${resume.id}">
                                        ${resume.name}
                                    </option>
                                </#list>
                            </select>
                        </div>

                        <div class="modal-footer">
                            <button type="submit" class="btn btn-success">
                                <@spring.message "vacancy.respond.submit"/>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </#if>
</@layout.layout>
