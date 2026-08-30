<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<@layout.layout title="Резюме">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-body p-4 p-md-5">
                <div class="mb-4">
                    <h1 class="h3 mb-1">
                    <#if resumeId??>
                        Редактирование резюме
                    <#else>
                        Новое резюме
                    </#if>
                    </h1>
                    <p class="text-muted mb-0">Заполните основные данные, затем добавьте детали при необходимости.</p>
                </div>

                <form action="<#if resumeId??>/pages/resumes/${resumeId}/edit<#else>/pages/resumes/create</#if>" method="post">

                    <#if _csrf??>
                        <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                    </#if>


                    <div class="mb-3">
                        <label class="form-label">Название резюме</label>
                        <@spring.formInput "resumeDto.name" 'class="form-control"'/>
                        <@spring.showErrors "<br>" "text-danger small"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Категория</label>

                        <@spring.formSingleSelect
                        "resumeDto.categoryId"
                        categories
                        'class="form-select"'/>

                        <@spring.showErrors
                        "<br>"
                        "text-danger small"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Ожидаемая зарплата</label>

                        <@spring.formInput
                        "resumeDto.salary"
                        'class="form-control"'/>

                        <@spring.showErrors
                        "<br>"
                        "text-danger small"/>
                    </div>

                    <div class="form-check mb-4">
                        <@spring.formCheckbox
                        "resumeDto.isActive"
                        'class="form-check-input"'/>

                        <label class="form-check-label">
                            Резюме активно (видно работодателям)
                        </label>
                    </div>

                    <hr>


                    <h5 class="mb-3">
                        Образование
                        <small class="text-muted">
                            (необязательно, можно оставить пустым)
                        </small>
                    </h5>

                    <#list resumeDto.educationList as edu>
                        <div class="row g-2 mb-3 border-bottom pb-3">

                            <div class="col-md-4">
                                <@spring.bind "resumeDto.educationList[${edu?index}].institution"/>
                                <input
                                        type="text"
                                        name="educationList[${edu?index}].institution"
                                        value="${edu.institution!''}"
                                        class="form-control"
                                        placeholder="Учебное заведение">
                                <@spring.showErrors "<br>" "text-danger small"/>
                            </div>

                            <div class="col-md-3">
                                <@spring.bind "resumeDto.educationList[${edu?index}].program"/>
                                <input
                                        type="text"
                                        name="educationList[${edu?index}].program"
                                        value="${edu.program!''}"
                                        class="form-control"
                                        placeholder="Программа">
                                <@spring.showErrors "<br>" "text-danger small"/>
                            </div>

                            <div class="col-md-2">
                                <@spring.bind "resumeDto.educationList[${edu?index}].startDate"/>
                                <input
                                        type="date"
                                        name="educationList[${edu?index}].startDate"
                                        value="<#if edu.startDate??>${edu.startDate.toString()}</#if>"
                                        class="form-control">
                                <@spring.showErrors "<br>" "text-danger small"/>
                            </div>

                            <div class="col-md-2">
                                <input
                                        type="date"
                                        name="educationList[${edu?index}].endDate"
                                        value="<#if edu.endDate??>${edu.endDate.toString()}</#if>"
                                        class="form-control">
                            </div>

                            <div class="col-md-1">
                                <@spring.bind "resumeDto.educationList[${edu?index}].degree"/>
                                <input
                                        type="text"
                                        name="educationList[${edu?index}].degree"
                                        value="${edu.degree!''}"
                                        class="form-control"
                                        placeholder="Степень">
                                <@spring.showErrors "<br>" "text-danger small"/>
                            </div>

                        </div>
                    </#list>


                    <h5 class="mb-3">
                        Опыт работы
                        <small class="text-muted">
                            (необязательно)
                        </small>
                    </h5>

                    <#list resumeDto.workExperienceList as we>
                        <div class="row g-2 mb-3 border-bottom pb-3">

                            <div class="col-md-2">
                                <@spring.bind "resumeDto.workExperienceList[${we?index}].years"/>
                                <input
                                        type="number"
                                        name="workExperienceList[${we?index}].years"
                                        value="${we.years!''}"
                                        class="form-control"
                                        placeholder="Лет">
                                <@spring.showErrors "<br>" "text-danger small"/>
                            </div>

                            <div class="col-md-3">
                                <@spring.bind "resumeDto.workExperienceList[${we?index}].companyName"/>
                                <input
                                        type="text"
                                        name="workExperienceList[${we?index}].companyName"
                                        value="${we.companyName!''}"
                                        class="form-control"
                                        placeholder="Компания">
                                <@spring.showErrors "<br>" "text-danger small"/>
                            </div>

                            <div class="col-md-3">
                                <@spring.bind "resumeDto.workExperienceList[${we?index}].position"/>
                                <input
                                        type="text"
                                        name="workExperienceList[${we?index}].position"
                                        value="${we.position!''}"
                                        class="form-control"
                                        placeholder="Должность">
                                <@spring.showErrors "<br>" "text-danger small"/>
                            </div>

                            <div class="col-md-4">
                                <@spring.bind "resumeDto.workExperienceList[${we?index}].responsibilities"/>
                                <input
                                        type="text"
                                        name="workExperienceList[${we?index}].responsibilities"
                                        value="${we.responsibilities!''}"
                                        class="form-control"
                                        placeholder="Обязанности">
                                <@spring.showErrors "<br>" "text-danger small"/>
                            </div>

                        </div>
                    </#list>


                    <h5 class="mb-3">
                        Контакты
                        <small class="text-muted">
                            (необязательно)
                        </small>
                    </h5>

                    <#list resumeDto.contactList as c>
                        <div class="row g-2 mb-3">

                            <div class="col-md-4">
                                <@spring.formSingleSelect
                                "resumeDto.contactList[${c?index}].typeId"
                                contactTypes
                                'class="form-select"'/>
                                <@spring.showErrors "<br>" "text-danger small"/>
                            </div>

                            <div class="col-md-8">
                                <@spring.bind "resumeDto.contactList[${c?index}].value"/>
                                <input
                                        type="text"
                                        name="contactList[${c?index}].value"
                                        value="${c.value!''}"
                                        class="form-control"
                                        placeholder="Значение (например, @username)">
                                <@spring.showErrors "<br>" "text-danger small"/>
                            </div>

                        </div>
                    </#list>

                    <button type="submit" class="btn btn-primary w-100 mt-3">Сохранить</button>
                </form>
                </div>
            </div>
        </div>
    </div>
</@layout.layout>
