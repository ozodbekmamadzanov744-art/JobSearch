<#import "../layout.ftlh" as layout>
<@layout.layout title="Восстановление пароля">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card p-4">
                <h2 class="mb-4 text-center">Восстановление пароля</h2>

                <#if error??>
                    <div class="alert alert-danger">${error}</div>
                </#if>
                <#if message??>
                    <div class="alert alert-success">${message}</div>
                </#if>

                <p class="text-muted">Введите email, указанный при регистрации — мы отправим на него ссылку для сброса пароля.</p>

                <form action="/pages/auth/forgot-password" method="post">
                    <#if _csrf??>
                        <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                    </#if>
                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="email" name="email" class="form-control" required autofocus>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Отправить ссылку</button>
                </form>

                <div class="text-center mt-3">
                    <a href="/pages/auth/login">Вернуться ко входу</a>
                </div>
            </div>
        </div>
    </div>
</@layout.layout>
