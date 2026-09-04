<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<#if vacancyId??>
    <#assign pageTitle>
        <@spring.message "vacancy.form.editTitle"/>
    </#assign>
<#else>
    <#assign pageTitle>
        <@spring.message "vacancy.form.createTitle"/>
    </#assign>
</#if>

<@layout.layout title=pageTitle>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-body p-4 p-md-5">
                    <div class="mb-4">
                        <h1 class="h3 mb-1">
                            <#if vacancyId??>
                                <@spring.message "vacancy.form.editTitle"/>
                            <#else>
                                <@spring.message "vacancy.form.createTitle"/>
                            </#if>
                        </h1>

                        <p class="text-muted mb-0">
                            <@spring.message "vacancy.form.subtitle"/>
                        </p>
                    </div>

                    <form action="<#if vacancyId??>/pages/vacancies/${vacancyId}/edit<#else>/pages/vacancies/create</#if>"
                          method="post">

                        <#if _csrf??>
                            <input type="hidden"
                                   name="${(_csrf.parameterName)!'csrf'}"
                                   value="${(_csrf.token)!'--no-token--'}"/>
                        </#if>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "vacancy.form.name"/>
                            </label>

                            <@spring.formInput
                            "vacancyDto.name"
                            'class="form-control"'/>

                            <@spring.showErrors
                            "<br>"
                            "text-danger small"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "vacancy.form.description"/>
                            </label>

                            <@spring.formTextarea
                            "vacancyDto.description"
                            'class="form-control" rows="5"'/>

                            <@spring.showErrors
                            "<br>"
                            "text-danger small"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "vacancy.form.category"/>
                            </label>

                            <@spring.formSingleSelect
                            "vacancyDto.categoryId"
                            categories
                            'class="form-select"'/>

                            <@spring.showErrors
                            "<br>"
                            "text-danger small"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "vacancy.form.salary"/>
                            </label>

                            <@spring.formInput
                            "vacancyDto.salary"
                            'class="form-control"'/>

                            <@spring.showErrors
                            "<br>"
                            "text-danger small"/>
                        </div>

                        <div class="row mb-3">
                            <div class="col">
                                <label class="form-label">
                                    <@spring.message "vacancy.form.experienceFrom"/>
                                </label>

                                <@spring.formInput
                                "vacancyDto.expFrom"
                                'class="form-control"'/>

                                <@spring.showErrors
                                "<br>"
                                "text-danger small"/>
                            </div>

                            <div class="col">
                                <label class="form-label">
                                    <@spring.message "vacancy.form.experienceTo"/>
                                </label>

                                <@spring.formInput
                                "vacancyDto.expTo"
                                'class="form-control"'/>

                                <@spring.showErrors
                                "<br>"
                                "text-danger small"/>
                            </div>
                        </div>

                        <div class="form-check mb-4">
                            <@spring.formCheckbox
                            "vacancyDto.isActive"
                            'class="form-check-input"'/>

                            <label class="form-check-label">
                                <@spring.message "vacancy.form.active"/>
                            </label>
                        </div>

                        <button type="submit"
                                class="btn btn-primary w-100">
                            <@spring.message "vacancy.form.save"/>
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@layout.layout>