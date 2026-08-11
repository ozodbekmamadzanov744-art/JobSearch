<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>
<@layout.layout title="Редактирование профиля">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card p-4">
                <h2 class="mb-4">Редактирование профиля</h2>

                <div class="text-center mb-4">
                    <img src="${user.avatar!'/images/default-avatar.png'}" class="rounded-circle mb-2" width="100" height="100" style="object-fit: cover;">
                    <form action="/pages/profile/avatar" method="post" enctype="multipart/form-data">
                        <input type="file" name="avatar" accept="image/*" class="form-control mb-2">
                        <button type="submit" class="btn btn-outline-secondary btn-sm">Обновить фото</button>
                    </form>
                </div>
                <hr class="mb-4">

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