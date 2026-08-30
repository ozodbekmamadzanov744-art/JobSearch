<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.layout title="Вакансия">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-body p-4 p-md-5">
                <div class="mb-4">
                    <h1 class="h3 mb-1">
                    <#if vacancyId??>
                        Редактирование вакансии
                    <#else>
                        Новая вакансия
                    </#if>
                    </h1>
                    <p class="text-muted mb-0">Сделайте вакансию понятной с первого взгляда.</p>
                </div>

                <form action="<#if vacancyId??>/pages/vacancies/${vacancyId}/edit<#else>/pages/vacancies/create</#if>" method="post">

                    <#if _csrf??>
                        <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                    </#if>

                    <div class="mb-3">
                        <label class="form-label">Название вакансии</label>
                        <@spring.formInput "vacancyDto.name" 'class="form-control"'/>
                        <@spring.showErrors "<br>" "text-danger small"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Описание</label>

                        <@spring.formTextarea
                        "vacancyDto.description"
                        'class="form-control" rows="5"'/>

                        <@spring.showErrors
                        "<br>"
                        "text-danger small"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Категория</label>

                        <@spring.formSingleSelect
                        "vacancyDto.categoryId"
                        categories
                        'class="form-select"'/>

                        <@spring.showErrors
                        "<br>"
                        "text-danger small"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Зарплата</label>

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
                                Опыт от (лет)
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
                                Опыт до (лет)
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
                            Вакансия активна (видна соискателям)
                        </label>

                    </div>

                    <button type="submit" class="btn btn-primary w-100">Сохранить</button>
                </form>
                </div>
            </div>
        </div>
    </div>
</@layout.layout>
