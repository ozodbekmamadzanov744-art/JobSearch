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
            </div>
        </div>
    </#list>
</@layout.layout>