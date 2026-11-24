/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */


package com.feedback.admin;




import com.feedback.admin.config.AdminConfig;
import com.feedback.admin.domain.AccessCodeBatch;
import com.feedback.admin.domain.CodeSummary;
import com.feedback.admin.domain.FeedbackSession;
import com.feedback.admin.domain.LIC;
import com.feedback.admin.exception.SessionStateException;
import com.feedback.admin.exception.ValidationException;
import com.feedback.admin.repository.memory.InMemoryAccessCodeRepository;
import com.feedback.admin.repository.memory.InMemoryLICRepository;
import com.feedback.admin.repository.memory.InMemoryModuleRepository;
import com.feedback.admin.repository.memory.InMemoryQuestionRepository;
import com.feedback.admin.repository.memory.InMemorySemesterRepository;
import com.feedback.admin.repository.memory.InMemorySessionRepository;
import com.feedback.admin.request.LICRequest;
import com.feedback.admin.request.ModuleRequest;
import com.feedback.admin.request.QuestionRequest;
import com.feedback.admin.request.QuestionType;
import com.feedback.admin.request.SemesterRequest;
import com.feedback.admin.request.SessionRequest;
import com.feedback.admin.service.AccessCodeService;
import com.feedback.admin.service.LICService;
import com.feedback.admin.service.ModuleService;
import com.feedback.admin.service.PlaceholderQrCodeGenerator;
import com.feedback.admin.service.QuestionBankService;
import com.feedback.admin.service.RequestValidator;
import com.feedback.admin.service.SecureCodeGenerator;
import com.feedback.admin.service.SemesterService;
import com.feedback.admin.service.SessionService;
import com.feedback.admin.stub.Questions;

import java.time.LocalDate;
import java.util.List;

public class G77_Admin_Panel {

    public static void main(String[] args) {

        AdminConfig config = AdminConfig.defaults();
        RequestValidator validator = new RequestValidator();

        // Repositories
        InMemoryAccessCodeRepository codeRepo =
                new InMemoryAccessCodeRepository();

        // Services
        ModuleService moduleService =
                new ModuleService(new InMemoryModuleRepository(), validator);

        LICService licService =
                new LICService(new InMemoryLICRepository(), validator);

        SemesterService semesterService =
                new SemesterService(new InMemorySemesterRepository(), validator);

        QuestionBankService questionService =
                new QuestionBankService(new InMemoryQuestionRepository(), validator);

        SessionService sessionService = new SessionService(
                validator,
                new InMemorySessionRepository(),
                moduleService,
                licService,
                semesterService,
                questionService
        );

        AccessCodeService codeService = new AccessCodeService(
                codeRepo,
                sessionService,
                new SecureCodeGenerator(config.getCodeLength()),
                new PlaceholderQrCodeGenerator(),
                config
        );

        // STEP 1
        heading("1. Set up a module, an LIC and a semester");

        moduleService.createModule(
                new ModuleRequest("EC1022", "Electrical Systems", 1));

        LIC lic = licService.createLIC(
                new LICRequest(
                        "Prof. Chulantha Kulasekere",
                        "chulantha@example.com"
                ));

        semesterService.createSemester(new SemesterRequest(2026, 1));

        System.out.println("Module : " +
                moduleService.find("EC1022").getName());

        System.out.println("LIC    : " +
                lic.getName() + " <" + lic.getEmail() + ">");

        System.out.println("Sem    : " +
                semesterService.listSemesters().get(0).getLabel());


        // STEP 2
        heading("2. Validation shows every mistake at once");

        try {
            moduleService.createModule(
                    new ModuleRequest("bad", "", 9));
        } catch (ValidationException e) {
            for (String error : e.getErrors()) {
                System.out.println(" - " + error);
            }
        }


        // STEP 3
        heading("3. Build the question bank");

        Questions q1 = questionService.addQuestion(
                new QuestionRequest(
                        QuestionType.RATING,
                        "Explanations given by the lecturer",
                        true,
                        "LECTURER",
                        null,
                        null
                ));

        Questions q2 = questionService.addQuestion(
                new QuestionRequest(
                        QuestionType.MULTIPLE_CHOICE,
                        "The workload of the subject was",
                        true,
                        "MODULE",
                        List.of("Too light", "Appropriate", "Too heavy"),
                        null
                ));

        Questions q3 = questionService.addQuestion(
                new QuestionRequest(
                        QuestionType.TEXT,
                        "Additional comments about the lecturer",
                        false,
                        "LECTURER",
                        null,
                        null
                ));

        System.out.println("Active questions: " +
                questionService.listActive().size());


        // STEP 4
        heading("4. Create a session and assign questions");

        LocalDate today = LocalDate.now();

        FeedbackSession session = sessionService.createSession(
                new SessionRequest(
                        "EC1022",
                        2026,
                        1,
                        lic.getUserId(),
                        today,
                        today.plusDays(7),
                        40,
                        5
                ));

        sessionService.assignQuestions(
                session.getSessionId(),
                List.of(q1.getId(), q2.getId(), q3.getId())
        );

        System.out.println("Session " + session.getSessionId() +
                " is " + session.getStatus() +
                ", threshold " + session.getMinResponseThreshold());


        // STEP 5
        heading("5. Access codes (shown once, only hashes are stored)");

        AccessCodeBatch batch =
                codeService.generateBatch(session.getSessionId(), 40);

        System.out.println("Generated " + batch.getTotalCodes() +
                " codes. First three: " +
                batch.getPlainCodes().subList(0, 3));

        CodeSummary summary =
                codeService.getSummary(session.getSessionId());

        System.out.println("Summary: total " + summary.getTotal() +
                ", used " + summary.getUsed() +
                ", unused " + summary.getUnused());

        System.out.println("Link   : " +
                codeService.getSessionLink(session.getSessionId()));


        // STEP 6
        heading("6. The schedule opens the session, and an open session cannot be edited");

        System.out.println("Sessions changed by the schedule: " +
                sessionService.applySchedule(today));

        System.out.println("Status: " +
                sessionService.find(session.getSessionId()).getStatus());

        try {
            sessionService.updateSession(
                    session.getSessionId(),
                    new SessionRequest(
                            "EC1022",
                            2026,
                            1,
                            lic.getUserId(),
                            today,
                            today.plusDays(10),
                            40,
                            5
                    ));
        } catch (SessionStateException e) {
            System.out.println("Blocked: " + e.getMessage());
        }


        // STEP 7
        heading("7. Close the session manually");

        sessionService.closeSession(session.getSessionId());

        System.out.println("Status: " +
                sessionService.find(session.getSessionId()).getStatus());

    }

    private static void heading(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }
}
