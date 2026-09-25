package kg.attractor.jobsearch.util;

import kg.attractor.jobsearch.dto.EducationInfoDto;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.List;

public final class ResumeValidationUtils {

    private ResumeValidationUtils() {
    }

    public static void validateEducationDates(List<EducationInfoDto> educationList,
                                              BindingResult bindingResult) {
        if (educationList == null) {
            return;
        }

        LocalDate today = LocalDate.now();

        for (int i = 0; i < educationList.size(); i++) {
            EducationInfoDto education = educationList.get(i);

            if (education == null) {
                continue;
            }

            if (education.getStartDate() != null
                    && education.getStartDate().isAfter(today)) {
                bindingResult.rejectValue(
                        "educationList[" + i + "].startDate",
                        "validation.education.startDate.future"
                );
            }

            if (education.getEndDate() == null) {
                continue;
            }

            if (education.getEndDate().isAfter(today)) {
                bindingResult.rejectValue(
                        "educationList[" + i + "].endDate",
                        "validation.education.endDate.future"
                );
            }

            if (education.getStartDate() != null
                    && education.getEndDate().isBefore(education.getStartDate())) {
                bindingResult.rejectValue(
                        "educationList[" + i + "].endDate",
                        "validation.education.dateRange"
                );
            }
        }
    }
}
