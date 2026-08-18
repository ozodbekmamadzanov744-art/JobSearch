<#import "layout.ftlh" as layout>
<@layout.layout title="Личный кабинет">
    <div class="d-flex align-items-center gap-3 mb-4">
        <img src="${user.avatar!'/images/default-avatar.png'}" class="rounded-circle" width="80" height="80" style="object-fit: cover;">
        <div>
            <h2 class="mb-0">${user.name}</h2>
            <#if user.accountType == "APPLICANT">
                <p class="text-muted mb-0">Возраст: ${user.age!"—"}</p>
            <#else>
                <p class="text-muted mb-0">Работодатель</p>
            </#if>
        </div>
        <a href="/pages/profile/edit" class="btn btn-outline-secondary ms-auto">Редактировать профиль</a>
    </div>

    <#if user.accountType == "APPLICANT">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h3 class="mb-0">Мои резюме</h3>
            <a href="/pages/resumes/create" class="btn btn-success btn-sm">+ Создать резюме</a>
        </div>
        <#if resumes?size == 0>
            <p class="text-muted">Резюме пока не созданы.</p>
        </#if>
        <#list resumes as resume>
            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title">${resume.name}</h5>
                    <p class="card-text text-muted">Обновлено: <#if resume.updateTime??>${resume.updateTime.toString()?substring(0,10)}<#else>—</#if></p>
                    <a href="/pages/resumes/${resume.id}/edit" class="btn btn-sm btn-outline-primary">Редактировать</a>
                    <button class="btn btn-sm btn-outline-danger" onclick="deleteResume(${resume.id})">Удалить</button>
                </div>
            </div>
        </#list>
        <#if resumePage??>
            <@layout.pagination pageObj=resumePage baseUrl="/pages/cabinet" extra=""/>
        </#if>
    <#else>
        <div class="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
            <h3 class="mb-0">Мои вакансии</h3>
            <div class="d-flex align-items-center gap-2">
                <form method="get" class="d-flex align-items-center gap-2">
                    <label class="form-label mb-0 text-nowrap">Сортировка:</label>
                    <select name="sort" class="form-select form-select-sm" style="width:auto;" onchange="this.form.submit()">
                        <option value="date" <#if currentSort == "date">selected</#if>>По дате публикации</option>
                        <option value="responses" <#if currentSort == "responses">selected</#if>>По количеству откликов</option>
                    </select>
                </form>
                <a href="/pages/vacancies/create" class="btn btn-success btn-sm">+ Создать вакансию</a>
            </div>
        </div>
        <#if vacancies?size == 0>
            <p class="text-muted">Вакансии пока не созданы.</p>
        </#if>
        <#list vacancies as vacancy>
            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title">${vacancy.name}</h5>
                    <p class="card-text text-muted">Обновлено: <#if vacancy.updateTime??>${vacancy.updateTime.toString()?substring(0,10)}<#else>—</#if></p>
                    <a href="/pages/vacancies/${vacancy.id}/edit" class="btn btn-sm btn-outline-primary">Редактировать</a>
                    <a href="/pages/vacancies/${vacancy.id}/applicants" class="btn btn-sm btn-outline-secondary">Отклики</a>
                    <button class="btn btn-sm btn-outline-danger" onclick="deleteVacancy(${vacancy.id})">Удалить</button>
                </div>
            </div>
        </#list>
        <#if vacancyPage??>
            <@layout.pagination pageObj=vacancyPage baseUrl="/pages/cabinet" extra="&sort=${currentSort}"/>
        </#if>
    </#if>

    <script>
        async function deleteResume(id) {
            if (!confirm('Удалить резюме?')) return;
            const res = await fetch('/resumes/' + id, {
                method: 'DELETE'
            });
            if (res.ok) location.reload();
        }
        async function deleteVacancy(id) {
            if (!confirm('Удалить вакансию?')) return;
            const res = await fetch('/vacancies/' + id, {
                method: 'DELETE'
            });
            if (res.ok) location.reload();
        }
    </script>
</@layout.layout>
