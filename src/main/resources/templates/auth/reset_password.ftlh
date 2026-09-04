<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<#assign pageTitle>
    <@spring.message "reset.title"/>
</#assign>

<@layout.layout title=pageTitle>
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card p-4">
                <h2 class="mb-4 text-center">
                    <@spring.message "reset.title"/>
                </h2>

                <#if error??>
                    <div class="alert alert-danger">${error}</div>

                    <div class="text-center mt-3">
                        <a href="/pages/auth/forgot-password">
                            <@spring.message "reset.newLink"/>
                        </a>
                    </div>
                <#else>
                    <form action="/pages/auth/reset-password" method="post">
                        <#if _csrf??>
                            <input type="hidden"
                                   name="${(_csrf.parameterName)!'csrf'}"
                                   value="${(_csrf.token)!'--no-token--'}"/>
                        </#if>

                        <input type="hidden"
                               name="token"
                               value="${token!}">

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "reset.password"/>
                            </label>

                            <input type="password"
                                   name="password"
                                   class="form-control"
                                   minlength="6"
                                   required
                                   autofocus>
                        </div>

                        <button type="submit"
                                class="btn btn-primary w-100">
                            <@spring.message "reset.submit"/>
                        </button>
                    </form>
                </#if>
            </div>
        </div>
    </div>
</@layout.layout>