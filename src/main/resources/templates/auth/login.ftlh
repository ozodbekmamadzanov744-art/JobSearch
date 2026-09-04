<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<#assign pageTitle>
    <@spring.message "login.title"/>
</#assign>

<@layout.layout title=pageTitle>
    <div class="row justify-content-center">
        <div class="col-lg-4 col-md-6">
            <div class="card shadow-sm">
                <div class="card-body p-4 p-md-5">
                    <div class="mb-4">
                        <h1 class="h3 mb-1">
                            <@spring.message "login.title"/>
                        </h1>

                        <p class="text-muted mb-0">
                            <@spring.message "login.subtitle"/>
                        </p>
                    </div>

                    <#if RequestParameters.error??>
                        <div class="alert alert-danger">
                            <@spring.message "login.error"/>
                        </div>
                    </#if>

                    <#if RequestParameters.reset?? && RequestParameters.reset == "success">
                        <div class="alert alert-success">
                            <@spring.message "login.resetSuccess"/>
                        </div>
                    </#if>

                    <form action="/pages/auth/login" method="post">
                        <#if _csrf??>
                            <input type="hidden"
                                   name="${(_csrf.parameterName)!'csrf'}"
                                   value="${(_csrf.token)!'--no-token--'}"/>
                        </#if>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "login.email"/>
                            </label>

                            <input type="email"
                                   name="username"
                                   class="form-control"
                                   required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "login.password"/>
                            </label>

                            <input type="password"
                                   name="password"
                                   class="form-control"
                                   required>
                        </div>

                        <button type="submit" class="btn btn-primary w-100">
                            <@spring.message "login.submit"/>
                        </button>
                    </form>

                    <div class="text-center mt-3">
                        <a href="/pages/auth/forgot-password">
                            <@spring.message "login.forgotPassword"/>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@layout.layout>