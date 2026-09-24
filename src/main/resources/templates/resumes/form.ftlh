<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<#if resumeId??>
    <#assign pageTitle>
        <@spring.message "resume.form.editTitle"/>
    </#assign>
<#else>
    <#assign pageTitle>
        <@spring.message "resume.form.createTitle"/>
    </#assign>
</#if>

<@layout.layout title=pageTitle>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-body p-4 p-md-5">
                    <div class="mb-4">
                        <h1 class="h3 mb-1">
                            <#if resumeId??>
                                <@spring.message "resume.form.editTitle"/>
                            <#else>
                                <@spring.message "resume.form.createTitle"/>
                            </#if>
                        </h1>

                        <p class="text-muted mb-0">
                            <@spring.message "resume.form.subtitle"/>
                        </p>
                    </div>

                    <form id="resume-form"
                          action="<#if resumeId??>/pages/resumes/${resumeId}/edit<#else>/pages/resumes/create</#if>"
                          method="post"
                          data-institution-placeholder="<@spring.message 'resume.form.institution'/>"
                          data-program-placeholder="<@spring.message 'resume.form.program'/>"
                          data-degree-placeholder="<@spring.message 'resume.form.degree'/>"
                          data-years-placeholder="<@spring.message 'resume.form.years'/>"
                          data-company-placeholder="<@spring.message 'resume.form.company'/>"
                          data-position-placeholder="<@spring.message 'resume.form.position'/>"
                          data-responsibilities-placeholder="<@spring.message 'resume.form.responsibilities'/>"
                          data-contact-value-placeholder="<@spring.message 'resume.form.contactValue'/>"
                          data-remove-label="<@spring.message 'resume.form.remove'/>"
                          data-start-date-future-message="<@spring.message 'validation.education.startDate.future'/>"
                          data-end-date-future-message="<@spring.message 'validation.education.endDate.future'/>"
                          data-date-range-message="<@spring.message 'validation.education.dateRange'/>"
                          data-request-error-message="<@spring.message 'common.requestError'/>"
                          data-api-url="<#if resumeId??>/resumes/${resumeId}<#else>/resumes</#if>"
                          data-api-method="<#if resumeId??>PUT<#else>POST</#if>"
                          data-success-url="/pages/cabinet"
                          <#if _csrf??>
                              data-csrf-header="${(_csrf.headerName)!'X-CSRF-TOKEN'}"
                              data-csrf-token="${(_csrf.token)!'--no-token--'}"
                          </#if>>

                        <#if _csrf??>
                            <input type="hidden"
                                   name="${(_csrf.parameterName)!'csrf'}"
                                   value="${(_csrf.token)!'--no-token--'}"/>
                        </#if>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "resume.form.name"/>
                            </label>

                            <@spring.formInput
                            "resumeDto.name"
                            'class="form-control"'/>

                            <@spring.showErrors
                            "<br>"
                            "text-danger small"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "resume.form.category"/>
                            </label>

                            <@spring.formSingleSelect
                            "resumeDto.categoryId"
                            categories
                            'class="form-select"'/>

                            <@spring.showErrors
                            "<br>"
                            "text-danger small"/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "resume.form.salary"/>
                            </label>

                            <@spring.formInput
                            "resumeDto.salary"
                            'class="form-control"'/>

                            <@spring.showErrors
                            "<br>"
                            "text-danger small"/>
                        </div>

                        <div class="form-check mb-4">
                            <@spring.formCheckbox
                            "resumeDto.isActive"
                            'class="form-check-input"'/>

                            <label class="form-check-label">
                                <@spring.message "resume.form.active"/>
                            </label>
                        </div>

                        <hr>

                        <h5 class="mb-3">
                            <@spring.message "resume.form.education"/>

                            <small class="text-muted">
                                <@spring.message "resume.form.educationOptional"/>
                            </small>
                        </h5>

                        <div id="education-list">
                            <#list resumeDto.educationList as edu>
                                <div class="education-item row g-2 mb-3 border-bottom pb-3">
                                    <div class="col-md-4">
                                        <@spring.bind "resumeDto.educationList[${edu?index}].institution"/>

                                        <input type="text"
                                               name="educationList[${edu?index}].institution"
                                               value="${edu.institution!''}"
                                               class="form-control"
                                               placeholder="<@spring.message 'resume.form.institution'/>">

                                        <@spring.showErrors
                                        "<br>"
                                        "text-danger small"/>
                                    </div>

                                    <div class="col-md-3">
                                        <@spring.bind "resumeDto.educationList[${edu?index}].program"/>

                                        <input type="text"
                                               name="educationList[${edu?index}].program"
                                               value="${edu.program!''}"
                                               class="form-control"
                                               placeholder="<@spring.message 'resume.form.program'/>">

                                        <@spring.showErrors
                                        "<br>"
                                        "text-danger small"/>
                                    </div>

                                    <div class="col-md-2">
                                        <@spring.bind "resumeDto.educationList[${edu?index}].startDate"/>

                                        <input type="date"
                                               name="educationList[${edu?index}].startDate"
                                               value="<#if edu.startDate??>${edu.startDate.toString()}</#if>"
                                               data-field="startDate"
                                               class="form-control">

                                        <@spring.showErrors
                                        "<br>"
                                        "text-danger small"/>
                                    </div>

                                    <div class="col-md-2">
                                        <@spring.bind "resumeDto.educationList[${edu?index}].endDate"/>

                                        <input type="date"
                                               name="educationList[${edu?index}].endDate"
                                               value="<#if edu.endDate??>${edu.endDate.toString()}</#if>"
                                               data-field="endDate"
                                               class="form-control">

                                        <@spring.showErrors
                                        "<br>"
                                        "text-danger small"/>
                                    </div>

                                    <div class="col-md-1">
                                        <@spring.bind "resumeDto.educationList[${edu?index}].degree"/>

                                        <input type="text"
                                               name="educationList[${edu?index}].degree"
                                               value="${edu.degree!''}"
                                               class="form-control"
                                               placeholder="<@spring.message 'resume.form.degree'/>">

                                        <@spring.showErrors
                                        "<br>"
                                        "text-danger small"/>
                                    </div>

                                    <div class="col-md-12">
                                        <button type="button"
                                                class="btn btn-outline-danger btn-sm remove-block">
                                            <@spring.message "resume.form.remove"/>
                                        </button>
                                    </div>
                                </div>
                            </#list>
                        </div>

                        <button type="button"
                                id="add-education"
                                class="btn btn-outline-primary btn-sm mb-4">
                            <@spring.message "resume.form.addEducation"/>
                        </button>

                        <h5 class="mb-3">
                            <@spring.message "resume.form.workExperience"/>

                            <small class="text-muted">
                                <@spring.message "resume.form.optional"/>
                            </small>
                        </h5>

                        <div id="work-experience-list">
                            <#list resumeDto.workExperienceList as we>
                                <div class="work-experience-item row g-2 mb-3 border-bottom pb-3">
                                    <div class="col-md-2">
                                        <@spring.bind "resumeDto.workExperienceList[${we?index}].years"/>

                                        <input type="number"
                                               name="workExperienceList[${we?index}].years"
                                               value="${we.years!''}"
                                               class="form-control"
                                               placeholder="<@spring.message 'resume.form.years'/>">

                                        <@spring.showErrors
                                        "<br>"
                                        "text-danger small"/>
                                    </div>

                                    <div class="col-md-3">
                                        <@spring.bind "resumeDto.workExperienceList[${we?index}].companyName"/>

                                        <input type="text"
                                               name="workExperienceList[${we?index}].companyName"
                                               value="${we.companyName!''}"
                                               class="form-control"
                                               placeholder="<@spring.message 'resume.form.company'/>">

                                        <@spring.showErrors
                                        "<br>"
                                        "text-danger small"/>
                                    </div>

                                    <div class="col-md-3">
                                        <@spring.bind "resumeDto.workExperienceList[${we?index}].position"/>

                                        <input type="text"
                                               name="workExperienceList[${we?index}].position"
                                               value="${we.position!''}"
                                               class="form-control"
                                               placeholder="<@spring.message 'resume.form.position'/>">

                                        <@spring.showErrors
                                        "<br>"
                                        "text-danger small"/>
                                    </div>

                                    <div class="col-md-4">
                                        <@spring.bind "resumeDto.workExperienceList[${we?index}].responsibilities"/>

                                        <input type="text"
                                               name="workExperienceList[${we?index}].responsibilities"
                                               value="${we.responsibilities!''}"
                                               class="form-control"
                                               placeholder="<@spring.message 'resume.form.responsibilities'/>">

                                        <@spring.showErrors
                                        "<br>"
                                        "text-danger small"/>
                                    </div>

                                    <div class="col-md-12">
                                        <button type="button"
                                                class="btn btn-outline-danger btn-sm remove-block">
                                            <@spring.message "resume.form.remove"/>
                                        </button>
                                    </div>
                                </div>
                            </#list>
                        </div>

                        <button type="button"
                                id="add-work-experience"
                                class="btn btn-outline-primary btn-sm mb-4">
                            <@spring.message "resume.form.addWorkExperience"/>
                        </button>

                        <h5 class="mb-3">
                            <@spring.message "resume.form.contacts"/>

                            <small class="text-muted">
                                <@spring.message "resume.form.optional"/>
                            </small>
                        </h5>

                        <div id="contact-list">
                            <#list resumeDto.contactList as contact>
                                <div class="contact-item row g-2 mb-3">
                                    <div class="col-md-4">
                                        <@spring.formSingleSelect
                                        "resumeDto.contactList[${contact?index}].typeId"
                                        contactTypes
                                        'class="form-select"'/>

                                        <@spring.showErrors
                                        "<br>"
                                        "text-danger small"/>
                                    </div>

                                    <div class="col-md-7">
                                        <@spring.bind "resumeDto.contactList[${contact?index}].value"/>

                                        <input type="text"
                                               name="contactList[${contact?index}].value"
                                               value="${contact.value!''}"
                                               class="form-control"
                                               placeholder="<@spring.message 'resume.form.contactValue'/>">

                                        <@spring.showErrors
                                        "<br>"
                                        "text-danger small"/>
                                    </div>

                                    <div class="col-md-1">
                                        <button type="button"
                                                class="btn btn-outline-danger btn-sm remove-block">
                                            <@spring.message "resume.form.remove"/>
                                        </button>
                                    </div>
                                </div>
                            </#list>
                        </div>

                        <button type="button"
                                id="add-contact"
                                class="btn btn-outline-primary btn-sm mb-4">
                            <@spring.message "resume.form.addContact"/>
                        </button>

                        <template id="contact-type-options">
                            <#list contactTypes?keys as key>
                                <option value="${key}">${contactTypes[key]}</option>
                            </#list>
                        </template>

                        <button type="submit"
                                class="btn btn-primary w-100 mt-3">
                            <@spring.message "resume.form.save"/>
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <script src="/js/resume-form.js?v=2"></script>
</@layout.layout>
