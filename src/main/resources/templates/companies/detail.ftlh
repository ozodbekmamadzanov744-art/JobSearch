<#import "../layout.ftlh" as layout>
<@layout.layout title="Компания">
    <div class="detail-band d-flex align-items-center gap-3 flex-wrap">
        <img src="${company.avatar!'/images/default-avatar.png'}" class="rounded-circle profile-avatar" alt="Аватар">
        <div>
            <h1 class="h3 mb-1">${company.name}<#if company.surname??> ${company.surname}</#if></h1>
            <p class="text-muted mb-0">${company.email}</p>
            <#if company.phoneNumber??>
                <p class="text-muted mb-0">${company.phoneNumber}</p>
            </#if>
        </div>
    </div>

    <h2 class="h4 mb-3">Вакансии компании</h2>
    <#if vacancies?size == 0>
        <p class="text-muted">У компании пока нет опубликованных вакансий.</p>
    </#if>

    <#list vacancies as vacancy>
        <div class="card shadow-sm mb-3">
            <div class="card-body">
                <h3 class="h5 mb-1">${vacancy.name}</h3>
                <p class="text-muted mb-2">
                    Зарплата: ${vacancy.salary!"—"} · Обновлено: <#if vacancy.updateTime??>${vacancy.updateTime.toString()?substring(0,10)}<#else>—</#if>
                </p>
                <p class="mb-0">${vacancy.description!""}</p>
            </div>
        </div>
    </#list>
</@layout.layout>
