<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<#assign pageTitle>
    <@spring.message "register.title"/>
</#assign>

<@layout.layout title=pageTitle>
    <div class="row justify-content-center">
        <div class="col-lg-6 col-md-8">
            <div class="card shadow-sm">
                <div class="card-body p-4 p-md-5">
                    <div class="mb-4">
                        <h1 class="h3 mb-1">
                            <@spring.message "register.title"/>
                        </h1>

                        <p class="text-muted mb-0">
                            <@spring.message "register.subtitle"/>
                        </p>
                    </div>

                    <#if generalError??>
                        <div class="alert alert-danger">${generalError}</div>
                    </#if>

                    <form action="/pages/auth/register"
                          method="post"
                          enctype="multipart/form-data">

                        <#if _csrf??>
                            <input type="hidden"
                                   name="${(_csrf.parameterName)!'csrf'}"
                                   value="${(_csrf.token)!'--no-token--'}"/>
                        </#if>

                        <div class="mb-3">
                            <label class="form-label d-block">
                                <@spring.message "register.accountType"/>
                            </label>

                            <div class="btn-group w-100" role="group">
                                <input class="btn-check"
                                       type="radio"
                                       name="accountType"
                                       id="roleApplicant"
                                       value="APPLICANT"
                                       checked>

                                <label class="btn btn-outline-primary"
                                       for="roleApplicant">
                                    <@spring.message "register.applicant"/>
                                </label>

                                <input class="btn-check"
                                       type="radio"
                                       name="accountType"
                                       id="roleEmployer"
                                       value="EMPLOYER">

                                <label class="btn btn-outline-primary"
                                       for="roleEmployer">
                                    <@spring.message "register.employer"/>
                                </label>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "register.name"/>
                            </label>

                            <@spring.formInput
                            "registrationDto.name"
                            'class="form-control"'/>

                            <@spring.showErrors
                            "<br>"
                            "text-danger small"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "register.email"/>
                            </label>

                            <@spring.formInput
                            "registrationDto.email"
                            'class="form-control"'/>

                            <@spring.showErrors
                            "<br>"
                            "text-danger small"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "register.password"/>
                            </label>

                            <@spring.formInput
                            "registrationDto.password"
                            'class="form-control"'
                            'password'/>

                            <@spring.showErrors
                            "<br>"
                            "text-danger small"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "register.phone"/>
                            </label>

                            <@spring.formInput
                            "registrationDto.phoneNumber"
                            'class="form-control"'/>

                            <@spring.showErrors
                            "<br>"
                            "text-danger small"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "register.avatar"/>
                            </label>

                            <input type="file"
                                   name="avatarFile"
                                   class="form-control"
                                   accept="image/*">
                        </div>

                        <button type="submit"
                                class="btn btn-primary w-100">
                            <@spring.message "register.submit"/>
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@layout.layout>