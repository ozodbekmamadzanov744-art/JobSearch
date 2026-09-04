<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<#assign pageTitle>
    <@spring.message "resume.title"/>
</#assign>

<@layout.layout title=pageTitle>
    <div class="detail-band d-flex justify-content-between align-items-start mb-4 flex-wrap gap-3">
        <div>
            <h1 class="h3 mb-1">
                ${resume.name}
            </h1>

            <p class="text-muted mb-0">
                <@spring.messageArgs
                "resume.expectedSalary",
                [resume.salary!"—"]/>

                ·

                <#if resume.updateTime??>
                    <@spring.messageArgs
                    "resume.updated",
                    [resume.updateTime.toString()?substring(0,10)]/>
                <#else>
                    <@spring.messageArgs
                    "resume.updated",
                    ["—"]/>
                </#if>
            </p>
        </div>

        <#if SPRING_SECURITY_CONTEXT??
        && SPRING_SECURITY_CONTEXT.authentication.principal.user.id == resume.applicant.id>

            <a href="/pages/resumes/${resume.id}/edit"
               class="btn btn-outline-primary">
                <@spring.message "resume.edit"/>
            </a>
        </#if>
    </div>

    <div class="mb-4">
        <h2 class="h4 mb-3">
            <@spring.message "resume.contacts"/>
        </h2>

        <#if contactList?size == 0>
            <p class="text-muted">
                <@spring.message "resume.contactsEmpty"/>
            </p>
        </#if>

        <#list contactList as contact>
            <div class="card shadow-sm mb-2">
                <div class="card-body py-2">
                    <strong>
                        <#if contact.contactType??>
                            ${contact.contactType.type}
                        <#else>
                            <@spring.message "resume.contact"/>
                        </#if>
                    </strong>

                    <span class="text-muted">
                        : ${contact.value!""}
                    </span>
                </div>
            </div>
        </#list>
    </div>

    <div class="mb-4">
        <h2 class="h4 mb-3">
            <@spring.message "resume.education"/>
        </h2>

        <#if educationList?size == 0>
            <p class="text-muted">
                <@spring.message "resume.educationEmpty"/>
            </p>
        </#if>

        <#list educationList as education>
            <div class="card shadow-sm mb-2">
                <div class="card-body py-2">
                    <div class="fw-semibold">
                        ${education.institution!""}
                    </div>

                    <div class="text-muted">
                        ${education.program!""} · ${education.degree!""}
                    </div>
                </div>
            </div>
        </#list>
    </div>

    <div>
        <h2 class="h4 mb-3">
            <@spring.message "resume.workExperience"/>
        </h2>

        <#if workExperienceList?size == 0>
            <p class="text-muted">
                <@spring.message "resume.workExperienceEmpty"/>
            </p>
        </#if>

        <#list workExperienceList as work>
            <div class="card shadow-sm mb-2">
                <div class="card-body py-2">
                    <div class="fw-semibold">
                        ${work.companyName!""}
                    </div>

                    <div class="text-muted">
                        ${work.position!""}
                        ·
                        <@spring.messageArgs
                        "resume.workYears",
                        [work.years!"—"]/>
                    </div>
                </div>
            </div>
        </#list>
    </div>
</@layout.layout>