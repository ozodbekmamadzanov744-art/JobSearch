<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<#assign pageTitle>
    <@spring.message "forgot.title"/>
</#assign>

<@layout.layout title=pageTitle>
    <div class="row justify-content-center">
        <div class="col-lg-5 col-md-7">
            <div class="card shadow-sm">
                <div class="card-body p-4 p-md-5">
                    <h2 class="mb-4 text-center">
                        <@spring.message "forgot.title"/>
                    </h2>

                    <#if message??>
                        <div class="alert alert-success">${message}</div>
                    </#if>

                    <#if error??>
                        <div class="alert alert-danger">${error}</div>
                    </#if>

                    <p class="text-muted">
                        <@spring.message "forgot.description"/>
                    </p>

                    <form action="/pages/auth/forgot-password" method="post">
                        <#if _csrf??>
                            <input type="hidden"
                                   name="${(_csrf.parameterName)!'csrf'}"
                                   value="${(_csrf.token)!'--no-token--'}"/>
                        </#if>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "forgot.email"/>
                            </label>

                            <input type="email"
                                   name="email"
                                   class="form-control"
                                   required>
                        </div>

                        <button type="submit"
                                class="btn btn-primary w-100">
                            <@spring.message "forgot.submit"/>
                        </button>
                    </form>

                    <div class="text-center mt-3">
                        <a href="/pages/auth/login">
                            <@spring.message "forgot.backToLogin"/>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@layout.layout>