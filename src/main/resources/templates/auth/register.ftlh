<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>
<@layout.layout title="Регистрация">
    <div class="row justify-content-center">
        <div class="col-lg-6 col-md-8">
            <div class="card shadow-sm">
                <div class="card-body p-4 p-md-5">
                    <div class="mb-4">
                        <h1 class="h3 mb-1">Регистрация</h1>
                        <p class="text-muted mb-0">Выберите тип аккаунта и заполните базовые данные.</p>
                    </div>

                    <#if generalError??>
                        <div class="alert alert-danger">${generalError}</div>
                    </#if>

                    <form action="/pages/auth/register" method="post" enctype="multipart/form-data">
                        <#if _csrf??>
                            <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                        </#if>
                        <div class="mb-3">
                            <label class="form-label d-block">Тип аккаунта</label>
                            <div class="btn-group w-100" role="group">
                                <input class="btn-check" type="radio" name="accountType" id="roleApplicant" value="APPLICANT" checked>
                                <label class="btn btn-outline-primary" for="roleApplicant">Соискатель</label>
                                <input class="btn-check" type="radio" name="accountType" id="roleEmployer" value="EMPLOYER">
                                <label class="btn btn-outline-primary" for="roleEmployer">Работодатель</label>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Имя / Название компании</label>
                            <@spring.formInput "registrationDto.name" 'class="form-control"'/>
                            <@spring.showErrors "<br>" "text-danger small"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <@spring.formInput "registrationDto.email" 'class="form-control"'/>
                            <@spring.showErrors "<br>" "text-danger small"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Пароль</label>
                            <@spring.formInput "registrationDto.password" 'class="form-control"' 'password'/>
                            <@spring.showErrors "<br>" "text-danger small"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Номер телефона</label>
                            <@spring.formInput "registrationDto.phoneNumber" 'class="form-control"'/>
                            <@spring.showErrors "<br>" "text-danger small"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Фото профиля (необязательно)</label>
                            <input type="file" name="avatarFile" class="form-control" accept="image/*">
                        </div>

                        <button type="submit" class="btn btn-primary w-100">Зарегистрироваться</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@layout.layout>
