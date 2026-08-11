<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>
<@layout.layout title="Редактирование профиля">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card p-4">
                <h2 class="mb-4">Редактирование профиля</h2>

                <form action="/pages/profile/edit" method="post">
                    <div class="mb-3">
                        <label class="form-label">Имя</label>
                        <@spring.formInput "profileDto.name" 'class="form-control"'/>
                        <@spring.showErrors "<br>" "text-danger small"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Фамилия</label>
                        <@spring.formInput "profileDto.surname" 'class="form-control"'/>
                    </div>

                    <#if accountType == "APPLICANT">
                        <div class="mb-3">
                            <label class="form-label">Возраст</label>
                            <@spring.formInput "profileDto.age" 'class="form-control"'/>
                            <@spring.showErrors "<br>" "text-danger small"/>
                        </div>
                    </#if>

                    <div class="mb-3">
                        <label class="form-label">Номер телефона</label>
                        <@spring.formInput "profileDto.phoneNumber" 'class="form-control"'/>
                        <@spring.showErrors "<br>" "text-danger small"/>
                    </div>

                    <button type="submit" class="btn btn-primary w-100">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</@layout.layout>