<#import "../layout.ftlh" as layout>
<@layout.layout title="Вакансии">
    <div class="detail-band d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
        <div>
            <h1 class="h3 mb-1">Актуальные вакансии</h1>
            <p class="text-muted mb-0">Список доступных предложений по работе.</p>
        </div>
        <form method="get" class="d-flex align-items-center gap-2">
            <label class="form-label mb-0 text-nowrap">Сортировка:</label>
            <select name="sort" class="form-select form-select-sm" style="width:auto;" onchange="this.form.submit()">
                <option value="date_desc" <#if currentSort == "date_desc" || currentSort == "date">selected</#if>>По дате публикации: сначала новые</option>
                <option value="date_asc" <#if currentSort == "date_asc">selected</#if>>По дате публикации: сначала старые</option>
                <option value="responses_desc" <#if currentSort == "responses_desc" || currentSort == "responses">selected</#if>>По количеству откликов: больше сначала</option>
                <option value="responses_asc" <#if currentSort == "responses_asc">selected</#if>>По количеству откликов: меньше сначала</option>
            </select>
        </form>
    </div>

    <#if vacancies?size == 0>
        <p class="text-muted">Вакансий пока нет.</p>
    </#if>

    <#list vacancies as vacancy>
        <a href="/pages/vacancies/${vacancy.id}" class="text-decoration-none text-reset">
            <div class="card shadow-sm mb-3">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-start gap-3 flex-wrap">
                        <div>
                            <h2 class="h5 mb-1">${vacancy.name}</h2>
                            <p class="text-muted mb-0">Опыт: от ${vacancy.expFrom!"—"} до ${vacancy.expTo!"—"} лет</p>
                        </div>
                        <div class="fw-semibold text-nowrap">${vacancy.salary!"—"}</div>
                    </div>
                    <p class="mb-0 mt-3 text-body-secondary">${vacancy.description}</p>
                </div>
            </div>
        </a>
    </#list>

    <#if vacancyPage??>
        <@layout.pagination pageObj=vacancyPage baseUrl="/pages/vacancies" extra="&sort=${currentSort}"/>
    </#if>
</@layout.layout>
