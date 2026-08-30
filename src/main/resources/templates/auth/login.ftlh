<#import "../layout.ftlh" as layout>
<@layout.layout title="Вход">
    <div class="row justify-content-center">
        <div class="col-lg-4 col-md-6">
            <div class="card shadow-sm">
                <div class="card-body p-4 p-md-5">
                    <div class="mb-4">
                        <h1 class="h3 mb-1">Вход</h1>
                        <p class="text-muted mb-0">Используйте email и пароль для доступа к кабинету.</p>
                    </div>

                    <#if RequestParameters.error??>
                        <div class="alert alert-danger">Неверный email или пароль</div>
                    </#if>
                    <#if RequestParameters.reset?? && RequestParameters.reset == "success">
                        <div class="alert alert-success">Пароль успешно изменён. Теперь вы можете войти.</div>
                    </#if>

                    <form action="/pages/auth/login" method="post">
                        <#if _csrf??>
                            <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                        </#if>
                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" name="username" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Пароль</label>
                            <input type="password" name="password" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Войти</button>
                    </form>

                    <div class="text-center mt-3">
                        <a href="/pages/auth/forgot-password">Забыли пароль?</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@layout.layout>
