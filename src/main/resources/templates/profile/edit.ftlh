<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>
<#assign pageTitle><@spring.message "common.edit"/></#assign>
<@layout.layout title=pageTitle>
    <div class="row justify-content-center">
        <div class="col-lg-6 col-md-8">
            <div class="card shadow-sm">
                <div class="card-body p-4 p-md-5">
                    <div class="mb-4">
                        <h1 class="h3 mb-1"><@spring.message 'common.edit'/> <@spring.message 'nav.cabinet'/></h1>
                        <p class="text-muted mb-0"><@spring.message 'common.update'/> <@spring.message 'nav.cabinet'/></p>
                    </div>

                    <div class="profile-avatar-block mb-4">
                        <img src="${user.avatar!'/images/default-avatar.png'}" class="rounded-circle profile-avatar" alt="<@spring.message 'company.avatarAlt'/>">
                        <form action="/pages/profile/avatar" method="post" enctype="multipart/form-data" class="mt-3">
                            <#if _csrf??>
                                <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                            </#if>
                            <input type="file" name="avatar" accept="image/*" class="form-control mb-2">
                            <button type="submit" class="btn btn-outline-secondary btn-sm"><@spring.message 'common.update'/> <@spring.message 'company.avatarAlt'/></button>
                        </form>
                    </div>

                    <form action="/pages/profile/edit" method="post">
                        <#if _csrf??>
                            <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
                        </#if>
                        <div class="mb-3">
                            <label class="form-label"><@spring.message 'common.name'/></label>
                            <@spring.formInput "profileDto.name" 'class="form-control"'/>
                            <@spring.showErrors "<br>" "text-danger small"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label"><@spring.message 'common.surname'/></label>
                            <@spring.formInput "profileDto.surname" 'class="form-control"'/>
                            <@spring.showErrors "<br>" "text-danger small"/>
                        </div>

                        <#if accountType == "APPLICANT">
                            <div class="mb-3">
                                <label class="form-label"><@spring.message 'common.age'/></label>
                                <@spring.formInput "profileDto.age" 'class="form-control"'/>
                                <@spring.showErrors "<br>" "text-danger small"/>
                            </div>
                        </#if>

                        <div class="mb-3">
                            <label class="form-label"><@spring.message 'common.phone'/></label>
                            <@spring.formInput "profileDto.phoneNumber" 'class="form-control"'/>
                            <@spring.showErrors "<br>" "text-danger small"/>
                        </div>

                        <button type="submit" class="btn btn-primary w-100"><@spring.message 'common.save'/></button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@layout.layout>
