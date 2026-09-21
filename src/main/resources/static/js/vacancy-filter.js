document.addEventListener('DOMContentLoaded', function () {
    const filterForm = document.getElementById('vacancy-filter-form');
    const nameInput = document.getElementById('filter-name');
    const salaryInput = document.getElementById('filter-salary');
    const sortSelect = document.getElementById('filter-sort');

    const searchButton = document.getElementById('search-button');
    const resetButton = document.getElementById('reset-filter');

    const vacancyList = document.getElementById('vacancy-list');
    const loadingMessage = document.getElementById('loading-message');
    const errorMessage = document.getElementById('error-message');
    const storageMessage = document.getElementById('storage-message');
    const emptyMessage = document.getElementById('empty-message');

    const pagination = document.getElementById('vacancy-pagination');
    const previousButton = document.getElementById('previous-page');
    const nextButton = document.getElementById('next-page');
    const pageNumber = document.getElementById('page-number');

    const storageKey = 'vacancyFilter';
    const pageSize = 5;

    let filteredVacancies = [];
    let currentPage = 0;

    restoreFilter();
    loadVacancies();

    filterForm.addEventListener('submit', function (event) {
        event.preventDefault();

        saveFilter();
        loadVacancies();
    });

    sortSelect.addEventListener('change', function () {
        if (filterForm.reportValidity()) {
            saveFilter();
            loadVacancies();
        }
    });

    resetButton.addEventListener('click', function () {
        filterForm.reset();

        storageMessage.hidden = true;

        try {
            localStorage.removeItem(storageKey);
        } catch (error) {
            storageMessage.hidden = false;
        }

        loadVacancies();
    });

    previousButton.addEventListener('click', function () {
        if (currentPage > 0) {
            currentPage--;
            renderVacancies();
        }
    });

    nextButton.addEventListener('click', function () {
        const totalPages = Math.ceil(filteredVacancies.length / pageSize);

        if (currentPage < totalPages - 1) {
            currentPage++;
            renderVacancies();
        }
    });

    function getFilter() {
        const formData = new FormData(filterForm);
        const filter = Object.fromEntries(formData);

        filter.name = filter.name.trim();

        return filter;
    }

    function saveFilter() {
        storageMessage.hidden = true;

        try {
            localStorage.setItem(storageKey, JSON.stringify(getFilter()));
        } catch (error) {
            storageMessage.hidden = false;
        }
    }

    function restoreFilter() {
        try {
            const savedFilter = localStorage.getItem(storageKey);

            if (!savedFilter) {
                return;
            }

            const filter = JSON.parse(savedFilter);

            if (!filter || typeof filter !== 'object') {
                return;
            }

            if (typeof filter.name === 'string') {
                nameInput.value = filter.name;
            }

            if (typeof filter.salary === 'string'
                && filter.salary.trim() !== ''
                && Number.isFinite(Number(filter.salary))
                && Number(filter.salary) >= 0) {
                salaryInput.value = filter.salary;
            }

            const allowedSorts = [
                'date_desc',
                'date_asc',
                'responses_desc',
                'responses_asc'
            ];

            if (allowedSorts.includes(filter.sort)) {
                sortSelect.value = filter.sort;
            }
        } catch (error) {
            storageMessage.hidden = false;
        }
    }

    async function loadVacancies() {
        const filter = getFilter();

        setLoading(true);

        errorMessage.hidden = true;
        emptyMessage.hidden = true;
        pagination.hidden = true;
        vacancyList.innerHTML = '';

        try {
            const response = await fetch('/vacancies?sort=' + filter.sort);

            if (!response.ok) {
                throw new Error(response.statusText);
            }

            const vacancies = await response.json();
            const searchName = filter.name.toLowerCase();

            filteredVacancies = vacancies.filter(function (vacancy) {
                const vacancyName = (vacancy.name || '').toLowerCase();

                const matchesName = vacancyName.includes(searchName);

                const matchesSalary = filter.salary === ''
                    || (vacancy.salary != null
                        && vacancy.salary >= Number(filter.salary));

                return matchesName && matchesSalary;
            });

            currentPage = 0;
            renderVacancies();
        } catch (error) {
            filteredVacancies = [];
            vacancyList.innerHTML = '';
            pagination.hidden = true;
            emptyMessage.hidden = true;
            errorMessage.hidden = false;
        } finally {
            setLoading(false);
        }
    }

    function setLoading(loading) {
        loadingMessage.hidden = !loading;

        searchButton.disabled = loading;
        resetButton.disabled = loading;
        nameInput.disabled = loading;
        salaryInput.disabled = loading;
        sortSelect.disabled = loading;
    }

    function renderVacancies() {
        vacancyList.innerHTML = '';

        emptyMessage.hidden = filteredVacancies.length !== 0;

        const start = currentPage * pageSize;
        const pageVacancies = filteredVacancies.slice(start, start + pageSize);

        pageVacancies.forEach(function (vacancy) {
            const link = createElement(
                'a',
                'text-decoration-none text-reset'
            );
            link.href = '/pages/vacancies/' + vacancy.id;

            const card = createElement('div', 'card shadow-sm mb-3');
            const body = createElement('div', 'card-body');

            const heading = createElement(
                'div',
                'd-flex justify-content-between align-items-start gap-3 flex-wrap'
            );

            const information = createElement('div', '');

            const title = createElement(
                'h2',
                'h5 mb-1',
                vacancy.name
            );

            const expFrom = vacancy.expFrom == null ? '—' : vacancy.expFrom;
            const expTo = vacancy.expTo == null ? '—' : vacancy.expTo;

            const experience = createElement(
                'p',
                'text-muted mb-0',
                vacancyList.dataset.experienceLabel + ': ' + expFrom + '–' + expTo
            );

            const salary = createElement(
                'div',
                'fw-semibold text-nowrap',
                vacancy.salary == null ? '—' : vacancy.salary
            );

            const description = createElement(
                'p',
                'mb-0 mt-3 text-body-secondary',
                vacancy.description
            );

            information.appendChild(title);
            information.appendChild(experience);

            heading.appendChild(information);
            heading.appendChild(salary);

            body.appendChild(heading);
            body.appendChild(description);

            card.appendChild(body);
            link.appendChild(card);
            vacancyList.appendChild(link);
        });

        const totalPages = Math.ceil(filteredVacancies.length / pageSize);

        pagination.hidden = totalPages <= 1;
        pageNumber.textContent = (currentPage + 1) + ' / ' + totalPages;

        previousButton.disabled = currentPage === 0;
        nextButton.disabled = currentPage >= totalPages - 1;
    }

    function createElement(tag, className, text) {
        const element = document.createElement(tag);
        element.className = className;

        if (text != null) {
            element.textContent = text;
        }

        return element;
    }
});