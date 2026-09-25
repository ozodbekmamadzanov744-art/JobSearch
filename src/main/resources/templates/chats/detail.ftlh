<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<#assign pageTitle><@spring.message "chat.title"/></#assign>

<@layout.layout title=pageTitle>
    <div class="detail-band mb-4">
        <h1 class="h3 mb-1"><@spring.message "chat.title"/></h1>
        <p class="text-muted mb-0">
            ${response.vacancy.name!"—"} · ${response.resume.name!"—"}
        </p>
    </div>

    <div class="mb-4">
        <#if messages?size == 0>
            <p class="text-muted"><@spring.message "chat.empty"/></p>
        </#if>

        <#list messages as message>
            <div class="card shadow-sm mb-2">
                <div class="card-body py-2">
                    <div class="d-flex justify-content-between gap-3 flex-wrap">
                        <strong>
                            <#if message.sender??>
                                ${message.sender.name}
                            <#else>
                                <@spring.message "chat.senderUnknown"/>
                            </#if>
                        </strong>

                        <span class="text-muted small">
                            <#if message.timestamp??>
                                ${message.timestamp.toString()?replace("T", " ")?substring(0,16)}
                            <#else>
                                —
                            </#if>
                        </span>
                    </div>

                    <p class="mb-0 mt-2">${message.content!""}</p>
                </div>
            </div>
        </#list>
    </div>

    <#if messageError??>
        <div class="alert alert-danger">
            <@spring.message messageError/>
        </div>
    </#if>

    <form action="/pages/chats/${response.id}/messages" method="post" class="card card-body shadow-sm">
        <#if _csrf??>
            <input type="hidden"
                   name="${(_csrf.parameterName)!'csrf'}"
                   value="${(_csrf.token)!'--no-token--'}"/>
        </#if>

        <label for="content" class="form-label"><@spring.message "chat.message"/></label>
        <textarea id="content" name="content" class="form-control mb-3" rows="3" required></textarea>

        <button type="submit" class="btn btn-primary align-self-start">
            <@spring.message "chat.send"/>
        </button>
    </form>
</@layout.layout>
