<#import "../layout.ftlh" as layout>
<@layout.layout title="Вакансии">
    <h2 class="mb-4">Актуальные вакансии</h2>
    <#if vacancies?size == 0>
        <p class="text-muted">Вакансий пока нет.</p>
    </#if>
    <#list vacancies as vacancy>
        <div class="card mb-3">
            <div class="card-body">
                <h5 class="card-title">${vacancy.name}</h5>
                <p class="card-text text-muted">
                    Зарплата: ${vacancy.salary} · Опыт: от ${vacancy.expFrom} до ${vacancy.expTo} лет
                </p>
                <p class="card-text">${vacancy.description}</p>
                <#if currentUser?? && currentUser.accountType == "APPLICANT">
                    <#if applicantResumes?? && applicantResumes?size gt 0>
                        <form action="/pages/vacancies/${vacancy.id}/respond" method="post" class="d-flex gap-2 align-items-center">
                            <select name="resumeId" class="form-select form-select-sm" style="max-width: 320px;">
                                <#list applicantResumes as resume>
                                    <option value="${resume.id}">${resume.name}</option>
                                </#list>
                            </select>
                            <button type="submit" class="btn btn-success btn-sm">Откликнуться</button>
                        </form>
                    <#else>
                        <a href="/pages/resumes/create" class="btn btn-outline-success btn-sm">Создать резюме для отклика</a>
                    </#if>
                </#if>
            </div>
        </div>
    </#list>
</@layout.layout>
