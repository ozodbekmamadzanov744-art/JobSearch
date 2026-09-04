<#import "/spring.ftl" as spring>
<#import "layout.ftlh" as layout>
<#assign pageTitle><@spring.message "nav.cabinet"/></#assign>
<@layout.layout title=pageTitle>
    <div class="detail-band d-flex align-items-center gap-3 flex-wrap">
        <img src="${user.avatar!'/images/default-avatar.png'}" class="rounded-circle profile-avatar" alt="<@spring.message 'company.avatarAlt'/>">
        <div class="me-auto">
            <h1 class="h3 mb-1">${user.name}</h1>
            <#if user.accountType == "APPLICANT">
                <p class="text-muted mb-0"><@spring.messageArgs "common.age", [user.age!"—"]/></p>
            <#else>
                <p class="text-muted mb-0"><@spring.message "common.employer"/></p>
            </#if>
        </div>
        <a href="/pages/profile/edit" class="btn btn-outline-secondary"><@spring.message "common.edit"/></a>
    </div>

    <#if user.accountType == "APPLICANT">
        <div class="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
            <h2 class="h4 mb-0"><@spring.message "resumes.title"/></h2>
            <a href="/pages/resumes/create" class="btn btn-primary btn-sm"><@spring.message "common.create"/></a>
        </div>
        <#if resumes?size == 0>
            <p class="text-muted"><@spring.message "resumes.empty"/></p>
        </#if>
        <#list resumes as resume>
            <div class="card shadow-sm mb-3">
                <div class="card-body d-flex justify-content-between align-items-center flex-wrap gap-3">
                    <div>
                        <h3 class="h5 mb-1"><a href="/pages/resumes/${resume.id}" class="text-decoration-none text-body">${resume.name}</a></h3>
                        <p class="text-muted mb-0">
                            <#if resume.updateTime??>
                                <@spring.messageArgs "resumes.updated", [resume.updateTime.toString()?substring(0,10)]/>
                            <#else>
                                <@spring.messageArgs "resumes.updated", ["—"]/>
                            </#if>
                        </p>
                    </div>
                    <div class="d-flex gap-2">
                        <a href="/pages/resumes/${resume.id}/edit" class="btn btn-outline-primary btn-sm"><@spring.message "common.edit"/></a>
                        <button class="btn btn-outline-danger btn-sm" onclick="deleteResume(${resume.id})"><@spring.message "common.delete"/></button>
                    </div>
                </div>
            </div>
        </#list>
        <#if resumePage??>
            <@layout.pagination pageObj=resumePage baseUrl="/pages/cabinet" extra=""/>
        </#if>
    <#else>
        <div class="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
            <h2 class="h4 mb-0"><@spring.message "vacancies.title"/></h2>
            <div class="d-flex align-items-center gap-2 flex-wrap">
                <form method="get" class="d-flex align-items-center gap-2">
                    <label class="form-label mb-0 text-nowrap"><@spring.message "vacancies.sort.label"/></label>
                    <select name="sort" class="form-select form-select-sm" style="width:auto;" onchange="this.form.submit()">
                        <option value="date_desc" <#if currentSort == "date_desc" || currentSort == "date">selected</#if>><@spring.message "vacancies.sort.dateDesc"/></option>
                        <option value="date_asc" <#if currentSort == "date_asc">selected</#if>><@spring.message "vacancies.sort.dateAsc"/></option>
                        <option value="responses_desc" <#if currentSort == "responses_desc" || currentSort == "responses">selected</#if>><@spring.message "vacancies.sort.responsesDesc"/></option>
                        <option value="responses_asc" <#if currentSort == "responses_asc">selected</#if>><@spring.message "vacancies.sort.responsesAsc"/></option>
                    </select>
                </form>
                <a href="/pages/vacancies/create" class="btn btn-primary btn-sm"><@spring.message "common.create"/></a>
            </div>
        </div>
        <#if vacancies?size == 0>
            <p class="text-muted"><@spring.message "vacancies.empty"/></p>
        </#if>
        <#list vacancies as vacancy>
            <div class="card shadow-sm mb-3">
                <div class="card-body d-flex justify-content-between align-items-center flex-wrap gap-3">
                    <div>
                        <h3 class="h5 mb-1"><a href="/pages/vacancies/${vacancy.id}" class="text-decoration-none text-body">${vacancy.name}</a></h3>
                        <p class="text-muted mb-0">
                            <#if vacancy.updateTime??>
                                <@spring.messageArgs "vacancy.updated", [vacancy.updateTime.toString()?substring(0,10)]/>
                            <#else>
                                <@spring.messageArgs "vacancy.updated", ["—"]/>
                            </#if>
                        </p>
                    </div>
                    <div class="d-flex gap-2">
                        <a href="/pages/vacancies/${vacancy.id}/edit" class="btn btn-outline-primary btn-sm"><@spring.message "common.edit"/></a>
                        <a href="/pages/vacancies/${vacancy.id}/applicants" class="btn btn-outline-secondary btn-sm"><@spring.message "vacancy.responses"/></a>
                        <button class="btn btn-outline-danger btn-sm" onclick="deleteVacancy(${vacancy.id})"><@spring.message "common.delete"/></button>
                    </div>
                </div>
            </div>
        </#list>
        <#if vacancyPage??>
            <@layout.pagination pageObj=vacancyPage baseUrl="/pages/cabinet" extra="&sort=${currentSort}"/>
        </#if>
    </#if>

    <script>
        async function deleteResume(id) {
            if (!confirm('<@spring.message "common.deleteResumeConfirm"/>')) return;
            const res = await fetch('/resumes/' + id, {
                method: 'DELETE'
            });
            if (res.ok) location.reload();
        }
        async function deleteVacancy(id) {
            if (!confirm('<@spring.message "common.deleteVacancyConfirm"/>')) return;
            const res = await fetch('/vacancies/' + id, {
                method: 'DELETE'
            });
            if (res.ok) location.reload();
        }
    </script>
</@layout.layout>
