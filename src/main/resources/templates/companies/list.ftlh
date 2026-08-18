<#import "../layout.ftlh" as layout>
<@layout.layout title="Компании">
    <h2 class="mb-4">Компании-работодатели</h2>

    <#if companies?size == 0>
        <p class="text-muted">Пока нет зарегистрированных компаний.</p>
    </#if>

    <#list companies as company>
        <div class="card mb-3">
            <div class="card-body d-flex align-items-center gap-3">
                <img src="${company.avatar!'/images/default-avatar.png'}" class="rounded-circle" width="56" height="56" style="object-fit: cover;">
                <div>
                    <h5 class="card-title mb-1">${company.name}<#if company.surname??> ${company.surname}</#if></h5>
                    <p class="card-text text-muted mb-0">${company.email}</p>
                </div>
            </div>
        </div>
    </#list>

    <#if companyPage??>
        <@layout.pagination pageObj=companyPage baseUrl="/pages/companies" extra=""/>
    </#if>
</@layout.layout>
