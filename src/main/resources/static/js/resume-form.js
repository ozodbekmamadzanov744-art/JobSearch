'use strict';

window.addEventListener('load', function () {
    const form = document.getElementById('resume-form');
    const educationList = document.getElementById('education-list');
    const workExperienceList =
        document.getElementById('work-experience-list');
    const contactList = document.getElementById('contact-list');
    const contactTypeOptions =
        document.getElementById('contact-type-options').innerHTML.trim();
    const labels = form.dataset;
    const submitButton = form.querySelector('[type="submit"]');

    const now = new Date();
    const today = [
        now.getFullYear(),
        String(now.getMonth() + 1).padStart(2, '0'),
        String(now.getDate()).padStart(2, '0')
    ].join('-');

    document.querySelectorAll('input[type="date"]').forEach(function (input) {
        input.max = today;
    });

    function setDateLimits(item) {
        item.querySelectorAll('input[type="date"]').forEach(function (input) {
            input.max = today;
        });
    }

    function clearDateValidity(item) {
        item.querySelectorAll('input[type="date"]').forEach(function (input) {
            input.setCustomValidity('');
        });
    }

    function clearServerErrors() {
        form.querySelectorAll('.ajax-error').forEach(function (error) {
            error.remove();
        });

        form.querySelectorAll('.is-invalid').forEach(function (field) {
            field.classList.remove('is-invalid');
        });
    }

    function findField(name) {
        return Array.from(form.querySelectorAll('[name]'))
            .find(function (field) {
                return field.name === name;
            });
    }

    function showFieldError(field, message) {
        field.classList.add('is-invalid');

        const error = document.createElement('div');
        error.className = 'invalid-feedback d-block ajax-error';
        error.textContent = message;

        field.insertAdjacentElement('afterend', error);
    }

    function showFormError(message) {
        const error = document.createElement('div');
        error.className = 'alert alert-danger ajax-error';
        error.textContent = message;

        form.prepend(error);
    }

    function showServerErrors(errorBody) {
        clearServerErrors();

        if (!errorBody || !errorBody.fieldErrors) {
            showFormError(errorBody && errorBody.message
                ? errorBody.message
                : labels.requestErrorMessage);
            return;
        }

        Object.entries(errorBody.fieldErrors).forEach(function ([fieldName, message]) {
            const field = findField(fieldName);

            if (field) {
                showFieldError(field, message);
            } else {
                showFormError(message);
            }
        });
    }

    function setSubmitting(isSubmitting) {
        if (submitButton) {
            submitButton.disabled = isSubmitting;
        }
    }

    function validateEducationDates() {
        let valid = true;

        document.querySelectorAll('.education-item').forEach(function (item) {
            clearDateValidity(item);

            const startDate = item.querySelector('[data-field="startDate"]');
            const endDate = item.querySelector('[data-field="endDate"]');

            if (!startDate || !endDate) {
                return;
            }

            if (startDate.value && startDate.value > today) {
                startDate.setCustomValidity(
                    labels.startDateFutureMessage
                );
                valid = false;
            }

            if (endDate.value && endDate.value > today) {
                endDate.setCustomValidity(
                    labels.endDateFutureMessage
                );
                valid = false;
            }

            if (startDate.value && endDate.value
                && endDate.value < startDate.value) {
                endDate.setCustomValidity(
                    labels.dateRangeMessage
                );
                valid = false;
            }
        });

        return valid;
    }

    function reindexItems(selector, listName) {
        document.querySelectorAll(selector).forEach(function (item, index) {
            item.querySelectorAll('[name]').forEach(function (field) {
                field.name = field.name.replace(
                    new RegExp(listName + '\\[\\d+\\]'),
                    listName + '[' + index + ']'
                );
            });
        });
    }

    function reindexAll() {
        reindexItems('.education-item', 'educationList');
        reindexItems('.work-experience-item', 'workExperienceList');
        reindexItems('.contact-item', 'contactList');
    }

    function trimValue(item, fieldName) {
        const field = item.querySelector('[name$=".' + fieldName + '"]');
        return field ? field.value.trim() : '';
    }

    function fieldValue(item, fieldName) {
        const field = item.querySelector('[name$=".' + fieldName + '"]');
        return field ? field.value : '';
    }

    function hasEducationValue(item) {
        return trimValue(item, 'institution') !== ''
            || trimValue(item, 'program') !== ''
            || fieldValue(item, 'startDate') !== ''
            || fieldValue(item, 'endDate') !== ''
            || trimValue(item, 'degree') !== '';
    }

    function hasWorkExperienceValue(item) {
        return fieldValue(item, 'years') !== ''
            || trimValue(item, 'companyName') !== ''
            || trimValue(item, 'position') !== ''
            || trimValue(item, 'responsibilities') !== '';
    }

    function hasContactValue(item) {
        return trimValue(item, 'value') !== '';
    }

    function removeEmptyBlocks() {
        document.querySelectorAll('.education-item').forEach(function (item) {
            if (!hasEducationValue(item)) {
                item.remove();
            }
        });

        document.querySelectorAll('.work-experience-item').forEach(function (item) {
            if (!hasWorkExperienceValue(item)) {
                item.remove();
            }
        });

        document.querySelectorAll('.contact-item').forEach(function (item) {
            if (!hasContactValue(item)) {
                item.remove();
            }
        });
    }

    function getEducationList() {
        return Array.from(document.querySelectorAll('.education-item'))
            .filter(hasEducationValue)
            .map(function (item) {
                return {
                    institution: trimValue(item, 'institution'),
                    program: trimValue(item, 'program'),
                    startDate: fieldValue(item, 'startDate') || null,
                    endDate: fieldValue(item, 'endDate') || null,
                    degree: trimValue(item, 'degree')
                };
            });
    }

    function getWorkExperienceList() {
        return Array.from(document.querySelectorAll('.work-experience-item'))
            .filter(hasWorkExperienceValue)
            .map(function (item) {
                const years = fieldValue(item, 'years');

                return {
                    years: years === '' ? null : Number(years),
                    companyName: trimValue(item, 'companyName'),
                    position: trimValue(item, 'position'),
                    responsibilities: trimValue(item, 'responsibilities')
                };
            });
    }

    function getContactList() {
        return Array.from(document.querySelectorAll('.contact-item'))
            .filter(hasContactValue)
            .map(function (item) {
                const typeId = fieldValue(item, 'typeId');

                return {
                    typeId: typeId === '' ? null : Number(typeId),
                    value: trimValue(item, 'value')
                };
            });
    }

    function getNumber(formData, fieldName) {
        const value = formData.get(fieldName);
        return value === null || value === '' ? null : Number(value);
    }

    function getResumePayload() {
        const formData = new FormData(form);

        return {
            name: formData.get('name'),
            categoryId: getNumber(formData, 'categoryId'),
            salary: getNumber(formData, 'salary'),
            isActive: formData.get('isActive') !== null,
            educationList: getEducationList(),
            workExperienceList: getWorkExperienceList(),
            contactList: getContactList()
        };
    }

    function bindRemoveButtons(container) {
        container.addEventListener('click', function (event) {
            const button = event.target.closest('.remove-block');

            if (!button) {
                return;
            }

            button.closest('.education-item, .work-experience-item, .contact-item')
                .remove();
            reindexAll();
        });
    }

    function addEducationBlock() {
        const educationBlock = document.createElement('div');

        educationBlock.className =
            'education-item row g-2 mb-3 border-bottom pb-3';

        educationBlock.innerHTML = `
            <div class="col-md-4">
                <input type="text"
                       name="educationList[0].institution"
                       data-field="institution"
                       class="form-control"
                       placeholder="${labels.institutionPlaceholder}">
            </div>

            <div class="col-md-3">
                <input type="text"
                       name="educationList[0].program"
                       data-field="program"
                       class="form-control"
                       placeholder="${labels.programPlaceholder}">
            </div>

            <div class="col-md-2">
                <input type="date"
                       name="educationList[0].startDate"
                       data-field="startDate"
                       class="form-control">
            </div>

            <div class="col-md-2">
                <input type="date"
                       name="educationList[0].endDate"
                       data-field="endDate"
                       class="form-control">
            </div>

            <div class="col-md-1">
                <input type="text"
                       name="educationList[0].degree"
                       data-field="degree"
                       class="form-control"
                       placeholder="${labels.degreePlaceholder}">
            </div>

            <div class="col-md-12">
                <button type="button"
                        class="btn btn-outline-danger btn-sm remove-block">
                    ${labels.removeLabel}
                </button>
            </div>
        `;

        setDateLimits(educationBlock);
        educationList.append(educationBlock);
        reindexAll();
    }

    function addWorkExperienceBlock() {
        const workExperienceBlock = document.createElement('div');

        workExperienceBlock.className =
            'work-experience-item row g-2 mb-3 border-bottom pb-3';

        workExperienceBlock.innerHTML = `
            <div class="col-md-2">
                <input type="number"
                       min="0"
                       name="workExperienceList[0].years"
                       data-field="years"
                       class="form-control"
                       placeholder="${labels.yearsPlaceholder}">
            </div>

            <div class="col-md-3">
                <input type="text"
                       name="workExperienceList[0].companyName"
                       data-field="companyName"
                       class="form-control"
                       placeholder="${labels.companyPlaceholder}">
            </div>

            <div class="col-md-3">
                <input type="text"
                       name="workExperienceList[0].position"
                       data-field="position"
                       class="form-control"
                       placeholder="${labels.positionPlaceholder}">
            </div>

            <div class="col-md-4">
                <input type="text"
                       name="workExperienceList[0].responsibilities"
                       data-field="responsibilities"
                       class="form-control"
                       placeholder="${labels.responsibilitiesPlaceholder}">
            </div>

            <div class="col-md-12">
                <button type="button"
                        class="btn btn-outline-danger btn-sm remove-block">
                    ${labels.removeLabel}
                </button>
            </div>
        `;

        workExperienceList.append(workExperienceBlock);
        reindexAll();
    }

    function addContactBlock() {
        const contactBlock = document.createElement('div');

        contactBlock.className = 'contact-item row g-2 mb-3';

        contactBlock.innerHTML = `
            <div class="col-md-4">
                <select name="contactList[0].typeId"
                        class="form-select">
                    ${contactTypeOptions}
                </select>
            </div>

            <div class="col-md-7">
                <input type="text"
                       name="contactList[0].value"
                       class="form-control"
                       placeholder="${labels.contactValuePlaceholder}">
            </div>

            <div class="col-md-1">
                <button type="button"
                        class="btn btn-outline-danger btn-sm remove-block">
                    ${labels.removeLabel}
                </button>
            </div>
        `;

        contactList.append(contactBlock);
        reindexAll();
    }

    bindRemoveButtons(educationList);
    bindRemoveButtons(workExperienceList);
    bindRemoveButtons(contactList);

    document.getElementById('add-education')
        .addEventListener('click', addEducationBlock);

    document.getElementById('add-work-experience')
        .addEventListener('click', addWorkExperienceBlock);

    document.getElementById('add-contact')
        .addEventListener('click', addContactBlock);

    form.addEventListener('input', validateEducationDates);
    form.addEventListener('change', validateEducationDates);

    form.addEventListener('submit', async function (event) {
        event.preventDefault();
        removeEmptyBlocks();
        reindexAll();
        clearServerErrors();

        if (!validateEducationDates()) {
            form.reportValidity();
            return;
        }

        setSubmitting(true);

        const headers = {
            'Content-Type': 'application/json;charset=utf-8'
        };

        if (labels.csrfHeader && labels.csrfToken) {
            headers[labels.csrfHeader] = labels.csrfToken;
        }

        let shouldUnlock = true;

        try {
            const response = await fetch(labels.apiUrl, {
                method: labels.apiMethod,
                headers: headers,
                body: JSON.stringify(getResumePayload())
            });

            if (response.ok) {
                shouldUnlock = false;
                window.location.href = labels.successUrl;
                return;
            }

            let errorBody = null;

            try {
                errorBody = await response.json();
            } catch (error) {
                errorBody = null;
            }

            showServerErrors(errorBody);

            const firstInvalid = form.querySelector('.is-invalid');

            if (firstInvalid) {
                firstInvalid.focus();
            }
        } catch (error) {
            clearServerErrors();
            showFormError(labels.requestErrorMessage);
        } finally {
            if (shouldUnlock) {
                setSubmitting(false);
            }
        }
    });

    reindexAll();
});
