<#import "../layout.ftlh" as layout>
<@layout.layout title="Резюме">
    <h2 class="mb-4">${pageTitle!"Резюме соискателей"}</h2>
    <#if resumes?size == 0>
        <p class="text-muted">${emptyMessage!"Резюме пока не опубликованы."}</p>
    </#if>
    <#list resumes as resume>
        <div class="card mb-3">
            <div class="card-body">
                <h5 class="card-title">${resume.name}</h5>
                <p class="card-text text-muted">
                    Ожидаемая зарплата: ${resume.salary} · Обновлено: <#if resume.updateTime??>${resume.updateTime.toString()?substring(0,10)}<#else>—</#if>
                </p>
            </div>
        </div>
    </#list>
</@layout.layout>