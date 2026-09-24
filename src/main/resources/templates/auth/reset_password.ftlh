<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<#assign pageTitle>
    <@spring.message "reset.title"/>
</#assign>

<@layout.layout title=pageTitle>
    <div class="row justify-content-center">
        <div class="col-lg-5 col-md-7">
            <div class="card shadow-sm">
                <div class="card-body p-4 p-md-5">
                    <h2 class="mb-4 text-center">
                        <@spring.message "reset.title"/>
                    </h2>

                    <div class="alert alert-info">
                        <p class="mb-2">
                            <@spring.message "reset.demoDescription"/>
                        </p>

                        <strong class="text-break">
                            ${displayToken}
                        </strong>
                    </div>

                    <form action="/pages/auth/reset-password"
                          method="post">

                        <#if _csrf??>
                            <input type="hidden"
                                   name="${_csrf.parameterName}"
                                   value="${_csrf.token}">
                        </#if>

                        <div class="mb-3">
                            <label for="token" class="form-label">
                                <@spring.message "reset.token"/>
                            </label>

                            <@spring.formInput
                            "resetDto.token"
                            'class="form-control" required autocomplete="off"'
                            />

                            <div class="text-danger small">
                                <@spring.showErrors "<br>"/>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="password" class="form-label">
                                <@spring.message "reset.password"/>
                            </label>

                            <@spring.formPasswordInput
                            "resetDto.password"
                            'class="form-control" minlength="6" required autocomplete="new-password"'
                            />

                            <div class="text-danger small">
                                <@spring.showErrors "<br>"/>
                            </div>
                        </div>

                        <button type="submit"
                                class="btn btn-primary w-100">
                            <@spring.message "reset.submit"/>
                        </button>
                    </form>

                    <div class="text-center mt-3">
                        <a href="/pages/auth/forgot-password">
                            <@spring.message "reset.newLink"/>
                        </a>
                    </div>

                    <div class="text-center mt-2">
                        <a href="/pages/auth/login">
                            <@spring.message "forgot.backToLogin"/>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@layout.layout>