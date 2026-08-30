<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>
<@layout.layout title="Редактирование профиля">
    <div class="row justify-content-center">
        <div class="col-lg-6 col-md-8">
            <div class="card shadow-sm">
                <div class="card-body p-4 p-md-5">
                    <div class="mb-4">
                        <h1 class="h3 mb-1">Редактирование профиля</h1>
                        <p class="text-muted mb-0">Обновите имя, контакты и аватар.</p>
                    </div>

                    <div class="profile-avatar-block mb-4">
                        <img src="${user.avatar!'/images/default-avatar.png'}" class="rounded-circle profile-avatar" alt="Аватар">
                        <form action="/pages/profile/avatar" method="post" enctype="multipart/form-data" class="mt-3">
                            <#if _csrf??>
                                <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                            </#if>
                            <input type="file" name="avatar" accept="image/*" class="form-control mb-2">
                            <button type="submit" class="btn btn-outline-secondary btn-sm">Обновить фото</button>
                        </form>
                    </div>

                    <form action="/pages/profile/edit" method="post">
                        <#if _csrf??>
                            <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                        </#if>
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
    </div>
</@layout.layout>
