<#import "../layout.ftlh" as layout>
<@layout.layout title="Ошибка">
    <div class="row justify-content-center py-4">
        <div class="col-lg-7 col-md-9">
            <div class="card shadow-sm">
                <div class="card-body p-4 p-md-5 text-center">
                    <div class="display-5 fw-semibold text-danger mb-2">${status}</div>
                    <h1 class="h3 mb-3">Ошибка обработки запроса<#if reason??>: ${reason}</#if></h1>
                    <#if message??>
                        <p class="text-muted mb-4">${message}</p>
                    </#if>

                    <div class="detail-band text-start mb-4">
                        <div class="row g-3">
                            <#if details?? && details.serverName??>
                                <div class="col-md-4">
                                    <div class="muted-label">Хост</div>
                                    <div class="fw-semibold">${details.serverName}</div>
                                </div>
                            </#if>
                            <#if details?? && details.requestURL??>
                                <div class="col-md-4">
                                    <div class="muted-label">Запрошенный URI</div>
                                    <div class="fw-semibold text-break">${details.requestURL}</div>
                                </div>
                            </#if>
                            <#if details?? && details.serverPort??>
                                <div class="col-md-4">
                                    <div class="muted-label">Порт</div>
                                    <div class="fw-semibold">${details.serverPort}</div>
                                </div>
                            </#if>
                            <div class="col-12">
                                <div class="muted-label">Время</div>
                                <div class="fw-semibold">${.now}</div>
                            </div>
                        </div>
                    </div>

                    <a href="/pages/vacancies" class="btn btn-primary">На главную</a>
                </div>
            </div>
        </div>
    </div>
</@layout.layout>
