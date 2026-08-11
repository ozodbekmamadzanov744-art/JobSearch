<#import "../layout.ftlh" as layout>
<@layout.layout title="Вход">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card p-4">
                <h2 class="mb-4 text-center">Вход</h2>

                <#if RequestParameters.error??>
                    <div class="alert alert-danger">Неверный email или пароль</div>
                </#if>

                <form action="/pages/auth/login" method="post">
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
            </div>
        </div>
    </div>
</@layout.layout>