<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>
<@layout.layout title="Резюме">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card p-4">
                <h2 class="mb-4"><#if resumeId??>Редактирование резюме<#else>Новое резюме</#if></h2>

                <form action="<#if resumeId??>/pages/resumes/${resumeId}/edit<#else>/pages/resumes/create</#if>" method="post">

                    <div class="mb-3">
                        <label class="form-label">Название резюме</label>
                        <@spring.formInput "resumeDto.name" 'class="form-control"'/>
                        <@spring.showErrors "<br>" "text-danger small"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Категория</label>
                        <@spring.formSingleSelect "resumeDto.categoryId", categories, 'class="form-select"'/>
                        <@spring.showErrors "<br>" "text-danger small"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Ожидаемая зарплата</label>
                        <@spring.formInput "resumeDto.salary" 'class="form-control"'/>
                        <@spring.showErrors "<br>" "text-danger small"/>
                    </div>

                    <div class="form-check mb-4">
                        <@spring.formCheckbox "resumeDto.isActive" 'class="form-check-input"'/>
                        <label class="form-check-label">Резюме активно (видно работодателям)</label>
                    </div>

                    <hr>
                    <h5 class="mb-3">Образование <small class="text-muted">(необязательно, можно оставить пустым)</small></h5>
                    <#list resumeDto.educationList as edu>
                        <div class="row g-2 mb-3 border-bottom pb-3">
                            <div class="col-md-4">
                                <input type="text" name="educationList[${edu?index}].institution" value="${edu.institution!''}" class="form-control" placeholder="Учебное заведение">
                            </div>
                            <div class="col-md-3">
                                <input type="text" name="educationList[${edu?index}].program" value="${edu.program!''}" class="form-control" placeholder="Программа">
                            </div>
                            <div class="col-md-2">
                                <input type="date" name="educationList[${edu?index}].startDate" value="${(edu.startDate?string('yyyy-MM-dd'))!''}" class="form-control">
                            </div>
                            <div class="col-md-2">
                                <input type="date" name="educationList[${edu?index}].endDate" value="${(edu.endDate?string('yyyy-MM-dd'))!''}" class="form-control">
                            </div>
                            <div class="col-md-1">
                                <input type="text" name="educationList[${edu?index}].degree" value="${edu.degree!''}" class="form-control" placeholder="Степень">
                            </div>
                        </div>
                    </#list>

                    <h5 class="mb-3">Опыт работы <small class="text-muted">(необязательно)</small></h5>
                    <#list resumeDto.workExperienceList as we>
                        <div class="row g-2 mb-3 border-bottom pb-3">
                            <div class="col-md-2">
                                <input type="number" name="workExperienceList[${we?index}].years" value="${we.years!''}" class="form-control" placeholder="Лет">
                            </div>
                            <div class="col-md-3">
                                <input type="text" name="workExperienceList[${we?index}].companyName" value="${we.companyName!''}" class="form-control" placeholder="Компания">
                            </div>
                            <div class="col-md-3">
                                <input type="text" name="workExperienceList[${we?index}].position" value="${we.position!''}" class="form-control" placeholder="Должность">
                            </div>
                            <div class="col-md-4">
                                <input type="text" name="workExperienceList[${we?index}].responsibilities" value="${we.responsibilities!''}" class="form-control" placeholder="Обязанности">
                            </div>
                        </div>
                    </#list>

                    <h5 class="mb-3">Контакты <small class="text-muted">(необязательно)</small></h5>
                    <#list resumeDto.contactList as c>
                        <div class="row g-2 mb-3">
                            <div class="col-md-4">
                                <@spring.formSingleSelect "resumeDto.contactList[${c?index}].typeId", contactTypes, 'class="form-select"'/>
                            </div>
                            <div class="col-md-8">
                                <input type="text" name="contactList[${c?index}].value" value="${c.value!''}" class="form-control" placeholder="Значение (например, @username)">
                            </div>
                        </div>
                    </#list>

                    <button type="submit" class="btn btn-primary w-100 mt-3">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</@layout.layout>