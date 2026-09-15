'use strict';

window.addEventListener('load', function () {
    const form = document.getElementById('resume-form');

    const educationList = document.getElementById('education-list');
    const workExperienceList =
        document.getElementById('work-experience-list');

    const addEducationButton =
        document.getElementById('add-education');

    const addWorkExperienceButton =
        document.getElementById('add-work-experience');

    let educationIndex = 0;
    let workExperienceIndex = 0;

    addEducationButton.addEventListener('click', function () {
        const educationBlock = document.createElement('div');

        educationBlock.className =
            'education-item border rounded p-3 mb-3';

        educationBlock.innerHTML = `
            <div class="row g-2">
                <div class="col-md-4">
                    <input type="text"
                           name="educationList[${educationIndex}].institution"
                           data-field="institution"
                           class="form-control"
                           placeholder="Учебное заведение">
                </div>

                <div class="col-md-3">
                    <input type="text"
                           name="educationList[${educationIndex}].program"
                           data-field="program"
                           class="form-control"
                           placeholder="Программа">
                </div>

                <div class="col-md-2">
                    <input type="date"
                           name="educationList[${educationIndex}].startDate"
                           data-field="startDate"
                           class="form-control">
                </div>

                <div class="col-md-2">
                    <input type="date"
                           name="educationList[${educationIndex}].endDate"
                           data-field="endDate"
                           class="form-control">
                </div>

                <div class="col-md-12">
                    <input type="text"
                           name="educationList[${educationIndex}].degree"
                           data-field="degree"
                           class="form-control"
                           placeholder="Степень">
                </div>

                <div class="col-md-12">
                    <button type="button"
                            class="btn btn-outline-danger btn-sm remove-block">
                        Удалить
                    </button>
                </div>
            </div>
        `;

        const removeButton =
            educationBlock.querySelector('.remove-block');

        removeButton.addEventListener('click', function () {
            educationBlock.remove();
        });

        educationList.append(educationBlock);
        educationIndex++;
    });

    addWorkExperienceButton.addEventListener('click', function () {
        const workExperienceBlock = document.createElement('div');

        workExperienceBlock.className =
            'work-experience-item border rounded p-3 mb-3';

        workExperienceBlock.innerHTML = `
            <div class="row g-2">
                <div class="col-md-2">
                    <input type="number"
                           min="0"
                           name="workExperienceList[${workExperienceIndex}].years"
                           data-field="years"
                           class="form-control"
                           placeholder="Лет">
                </div>

                <div class="col-md-3">
                    <input type="text"
                           name="workExperienceList[${workExperienceIndex}].companyName"
                           data-field="companyName"
                           class="form-control"
                           placeholder="Компания">
                </div>

                <div class="col-md-3">
                    <input type="text"
                           name="workExperienceList[${workExperienceIndex}].position"
                           data-field="position"
                           class="form-control"
                           placeholder="Должность">
                </div>

                <div class="col-md-4">
                    <input type="text"
                           name="workExperienceList[${workExperienceIndex}].responsibilities"
                           data-field="responsibilities"
                           class="form-control"
                           placeholder="Обязанности">
                </div>

                <div class="col-md-12">
                    <button type="button"
                            class="btn btn-outline-danger btn-sm remove-block">
                        Удалить
                    </button>
                </div>
            </div>
        `;

        const removeButton =
            workExperienceBlock.querySelector('.remove-block');

        removeButton.addEventListener('click', function () {
            workExperienceBlock.remove();
        });

        workExperienceList.append(workExperienceBlock);
        workExperienceIndex++;
    });

    function getEducationList() {
        const educationItems =
            document.querySelectorAll('.education-item');

        const result = [];

        educationItems.forEach(function (item) {
            result.push({
                institution:
                item.querySelector('[data-field="institution"]').value,
                program:
                item.querySelector('[data-field="program"]').value,
                startDate:
                item.querySelector('[data-field="startDate"]').value,
                endDate:
                    item.querySelector('[data-field="endDate"]').value || null,
                degree:
                item.querySelector('[data-field="degree"]').value
            });
        });

        return result;
    }

    function getWorkExperienceList() {
        const workExperienceItems =
            document.querySelectorAll('.work-experience-item');

        const result = [];

        workExperienceItems.forEach(function (item) {
            const years =
                item.querySelector('[data-field="years"]').value;

            result.push({
                years: years === '' ? null : Number(years),
                companyName:
                item.querySelector('[data-field="companyName"]').value,
                position:
                item.querySelector('[data-field="position"]').value,
                responsibilities:
                item.querySelector('[data-field="responsibilities"]').value
            });
        });

        return result;
    }

    function getContactList(formData) {
        const result = [];

        for (let i = 0; i < 3; i++) {
            const typeId =
                formData.get(`contactList[${i}].typeId`);

            const value =
                formData.get(`contactList[${i}].value`);

            if (typeId && value && value.trim() !== '') {
                result.push({
                    typeId: Number(typeId),
                    value: value
                });
            }
        }

        return result;
    }

    form.addEventListener('submit', async function (event) {
        event.preventDefault();

        const formData = new FormData(form);

        const resume = {
            name: formData.get('name'),
            categoryId: Number(formData.get('categoryId')),
            salary: Number(formData.get('salary')),
            isActive: formData.get('isActive') !== null,
            educationList: getEducationList(),
            workExperienceList: getWorkExperienceList(),
            contactList: getContactList(formData)
        };

        try {
            const response = await fetch('/resumes', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json;charset=utf-8'
                },
                body: JSON.stringify(resume)
            });

            if (!response.ok) {
                throw new Error('Не удалось сохранить резюме');
            }

            await response.json();

            window.location.href = '/pages/cabinet';
        } catch (error) {
            console.log(error);
            alert(error.message);
        }
    });
});