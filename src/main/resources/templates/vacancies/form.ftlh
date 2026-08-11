<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>
<@layout.layout title="Вакансия">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card p-4">
                <h2 class="mb-4"><#if vacancyId??>Редактирование вакансии<#else>Новая вакансия</#if></h2>

                <form action="<#if vacancyId??>/pages/vacancies/${vacancyId}/edit<#else>/pages/vacancies/create</#if>" method="post">

                    <div class="mb-3">
                        <label class="form-label">Название вакансии</label>
                        <@spring.formInput "vacancyDto.name" 'class="form-control"'/>
                        <@spring.showErrors "<br>" "text-danger small"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Описание</label>
                        <@spring.formTextarea "vacancyDto.description" 'class="form-control" rows="5"'/>
                        <@spring.showErrors "<br>" "text-danger small"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Категория</label>
                        <@spring.formSingleSelect "vacancyDto.categoryId", categories, 'class="form-select"'/>
                        <@spring.showErrors "<br>" "text-danger small"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Зарплата</label>
                        <@spring.formInput "vacancyDto.salary" 'class="form-control"'/>
                        <@spring.showErrors "<br>" "text-danger small"/>
                    </div>

                    <div class="row mb-3">
                        <div class="col">
                            <label class="form-label">Опыт от (лет)</label>
                            <@spring.formInput "vacancyDto.expFrom" 'class="form-control"'/>
                            <@spring.showErrors "<br>" "text-danger small"/>
                        </div>
                        <div class="col">
                            <label class="form-label">Опыт до (лет)</label>
                            <@spring.formInput "vacancyDto.expTo" 'class="form-control"'/>
                            <@spring.showErrors "<br>" "text-danger small"/>
                        </div>
                    </div>

                    <div class="form-check mb-4">
                        <@spring.formCheckbox "vacancyDto.isActive" 'class="form-check-input"'/>
                        <label class="form-check-label">Вакансия активна (видна соискателям)</label>
                    </div>

                    <button type="submit" class="btn btn-primary w-100">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</@layout.layout>