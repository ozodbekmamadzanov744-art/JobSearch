<#import "/spring.ftl" as spring>
<#import "../layout.ftlh" as layout>

<#assign pageTitle>
    <@spring.message "vacancies.title"/>
</#assign>

<#assign english = springMacroRequestContext.locale.language == "en">

<@layout.layout title=pageTitle>
    <div class="detail-band mb-4">
        <h1 class="h3 mb-1">
            <@spring.message "vacancies.heading"/>
        </h1>

        <p class="text-muted mb-0">
            <@spring.message "vacancies.subtitle"/>
        </p>
    </div>

    <form id="vacancy-filter-form" class="card card-body mb-4">
        <div class="row g-3">
            <div class="col-md-4">
                <label for="filter-name" class="form-label">
                    <@spring.message "vacancies.filter.name"/>
                </label>

                <input type="text"
                       id="filter-name"
                       name="name"
                       class="form-control">
            </div>

            <div class="col-md-2">
                <label for="filter-salary" class="form-label">
                    <@spring.message "vacancies.filter.salary"/>
                </label>

                <input type="number"
                       id="filter-salary"
                       name="salary"
                       class="form-control"
                       min="0"
                       step="any">
            </div>

            <div class="col-md-3">
                <label for="filter-category" class="form-label">
                    <@spring.message "vacancies.filter.category"/>
                </label>

                <select id="filter-category" name="categoryId" class="form-select">
                    <option value=""><@spring.message "vacancies.filter.allCategories"/></option>
                    <#list categories?keys as key>
                        <option value="${key}">${categories[key]}</option>
                    </#list>
                </select>
            </div>

            <div class="col-md-3">
                <label for="filter-sort" class="form-label">
                    <@spring.message "vacancies.sort.label"/>
                </label>

                <select id="filter-sort" name="sort" class="form-select">
                    <option value="date_desc">
                        <@spring.message "vacancies.sort.dateDesc"/>
                    </option>

                    <option value="date_asc">
                        <@spring.message "vacancies.sort.dateAsc"/>
                    </option>

                    <option value="responses_desc">
                        <@spring.message "vacancies.sort.responsesDesc"/>
                    </option>

                    <option value="responses_asc">
                        <@spring.message "vacancies.sort.responsesAsc"/>
                    </option>

                    <option value="salary_desc">
                        <@spring.message "vacancies.sort.salaryDesc"/>
                    </option>

                    <option value="salary_asc">
                        <@spring.message "vacancies.sort.salaryAsc"/>
                    </option>
                </select>
            </div>
        </div>

        <div class="d-flex gap-2 mt-3">
            <button type="submit"
                    id="search-button"
                    class="btn btn-primary">
                <@spring.message "vacancies.filter.search"/>
            </button>

            <button type="button"
                    id="reset-filter"
                    class="btn btn-outline-secondary">
                <@spring.message "vacancies.filter.reset"/>
            </button>
        </div>
    </form>

    <p id="loading-message" class="text-muted" hidden>
        <@spring.message "vacancies.filter.loading"/>
    </p>

    <p id="error-message" class="text-danger" hidden>
        <@spring.message "vacancies.filter.error"/>
    </p>

    <p id="storage-message" class="text-warning" hidden>
        <@spring.message "vacancies.filter.storageError"/>
    </p>

    <p id="empty-message" class="text-muted" hidden>
        <@spring.message "vacancies.filter.empty"/>
    </p>

    <div id="vacancy-list"
         data-experience-label="<@spring.message 'vacancies.filter.experience'/>"
         data-company-label="<@spring.message 'vacancies.filter.company'/>"
         data-updated-label="<@spring.message 'vacancies.filter.updated'/>">
    </div>

    <div id="vacancy-pagination" class="text-center mt-4" hidden>
        <button type="button"
                id="previous-page"
                class="btn btn-outline-primary btn-sm">
            <@spring.message "pagination.previous"/>
        </button>

        <span id="page-number" class="mx-3"></span>

        <button type="button"
                id="next-page"
                class="btn btn-outline-primary btn-sm">
            <@spring.message "pagination.next"/>
        </button>
    </div>

    <script src="/js/vacancy-filter.js"></script>
</@layout.layout>
