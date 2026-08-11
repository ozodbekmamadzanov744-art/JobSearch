<#import "../layout.ftlh" as layout>
<@layout.layout title="Ошибка">
    <div class="d-flex flex-column align-items-center text-center py-5">
        <h1 class="display-1 text-danger">${status}</h1>
        <h2 class="mb-4">Упс! <#if reason??>${reason}</#if></h2>

        <#if message??>
            <p class="lead mb-4">${message}</p>
        </#if>

        <ul class="list-group mb-4 text-start" style="max-width: 500px; width: 100%;">
            <#if details??>
                <#if details.serverName??>
                    <li class="list-group-item"><strong>Хост:</strong> <code>${details.serverName}</code></li>
                </#if>
                <#if details.requestURL??>
                    <li class="list-group-item"><strong>Запрошенный URI:</strong> <code>${details.requestURL}</code></li>
                </#if>
                <#if details.serverPort??>
                    <li class="list-group-item"><strong>Порт:</strong> <code>${details.serverPort}</code></li>
                </#if>
            </#if>
            <li class="list-group-item"><strong>Время:</strong> <code>${.now}</code></li>
        </ul>

        <a href="/pages/vacancies" class="btn btn-primary">На главную</a>
    </div>
</@layout.layout>