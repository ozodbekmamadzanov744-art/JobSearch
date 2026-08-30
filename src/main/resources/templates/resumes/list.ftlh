<#import "../layout.ftlh" as layout>
<@layout.layout title="Резюме">
    <div class="detail-band d-flex justify-content-between align-items-center flex-wrap gap-2">
        <div>
            <h1 class="h3 mb-1">${pageTitle!"Резюме соискателей"}</h1>
            <p class="text-muted mb-0">${emptyMessage!"Резюме пока не опубликованы."}</p>
        </div>
    </div>
    <#if resumes?size == 0>
        <p class="text-muted mt-3">${emptyMessage!"Резюме пока не опубликованы."}</p>
    </#if>
    <#list resumes as resume>
        <a href="/pages/resumes/${resume.id}" class="text-decoration-none text-reset">
            <div class="card shadow-sm mb-3">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-start gap-3 flex-wrap">
                        <div>
                            <h2 class="h5 mb-1">${resume.name}</h2>
                            <p class="text-muted mb-0">Обновлено: <#if resume.updateTime??>${resume.updateTime.toString()?substring(0,10)}<#else>—</#if></p>
                        </div>
                        <div class="fw-semibold text-nowrap">${resume.salary!"—"}</div>
                    </div>
                </div>
            </div>
        </a>
    </#list>

    <#if resumePage??>
        <@layout.pagination pageObj=resumePage baseUrl="/pages/resumes" extra=""/>
    </#if>
</@layout.layout>
