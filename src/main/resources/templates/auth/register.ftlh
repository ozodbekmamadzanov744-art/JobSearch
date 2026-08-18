<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>
<@layout.layout title="Регистрация">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card p-4">
                <h2 class="mb-4 text-center">Регистрация</h2>

                <#if generalError??>
                    <div class="alert alert-danger">${generalError}</div>
                </#if>

                <form action="/pages/auth/register" method="post" enctype="multipart/form-data">
                    <div class="mb-3">
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="accountType" id="roleApplicant" value="APPLICANT" checked>
                            <label class="form-check-label" for="roleApplicant">Я соискатель</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="accountType" id="roleEmployer" value="EMPLOYER">
                            <label class="form-check-label" for="roleEmployer">Я работодатель</label>
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

                    <button type="submit" class="btn btn-success w-100">Зарегистрироваться</button>
                </form>
            </div>
        </div>
    </div>

</@layout.layout>