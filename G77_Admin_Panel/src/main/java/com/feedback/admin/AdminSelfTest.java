/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin;


import com.feedback.admin.config.AdminConfig;
import com.feedback.admin.domain.AccessCodeBatch;
import com.feedback.admin.domain.FeedbackSession;
import com.feedback.admin.domain.LIC;
import com.feedback.admin.domain.SessionStatus;
import com.feedback.admin.exception.ResourceNotFoundException;
import com.feedback.admin.exception.SessionStateException;
import com.feedback.admin.exception.ValidationException;
import com.feedback.admin.repository.memory.*;
import com.feedback.admin.request.*;
import com.feedback.admin.service.*;
import com.feedback.admin.stub.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Tests that need no extra library. Run this class (Shift + F6 in NetBeans).
 * It prints PASS or FAIL for each check and ends with the totals.
 * Later these checks can be moved into JUnit 5 tests.
 */
public class AdminSelfTest {
    private static int passed;
    private static int failed;
    private static final LocalDate TODAY = LocalDate.of(2026, 11, 22);

    private static void check(boolean ok, String name) {
        if (ok) {
            passed++;
            System.out.println("PASS  " + name);
        } else {
            failed++;
            System.out.println("FAIL  " + name);
        }
    }

    /** Passes when the action throws an exception of the given type. */
    private static void expect(Class<? extends RuntimeException> type, Runnable action, String name) {
        try {
            action.run();
            check(false, name + " (no exception was thrown)");
        } catch (RuntimeException e) {
            check(type.isInstance(e), name + " -> " + e.getClass().getSimpleName());
        }
    }

    /** Everything the tests need, wired the same way as the demo. */
    private static class Setup {
        final AdminConfig config = AdminConfig.defaults();
        final RequestValidator validator = new RequestValidator();
        final InMemoryAccessCodeRepository codeRepo = new InMemoryAccessCodeRepository();
        final SecureCodeGenerator generator = new SecureCodeGenerator(10);
        final ModuleService modules = new ModuleService(new InMemoryModuleRepository(), validator);
        final LICService lics = new LICService(new InMemoryLICRepository(), validator);
        final SemesterService semesters =
                new SemesterService(new InMemorySemesterRepository(), validator);
        final QuestionBankService questions =
                new QuestionBankService(new InMemoryQuestionRepository(), validator);
        final SessionService sessions = new SessionService(validator, new InMemorySessionRepository(),
                modules, lics, semesters, questions);
        final AccessCodeService codes = new AccessCodeService(codeRepo, sessions, generator,
                new PlaceholderQrCodeGenerator(), config);
        LIC lic;
        String questionId;

        Setup() {
            modules.createModule(new ModuleRequest("EC1022", "Electrical Systems", 1));
            lic = lics.createLIC(new LICRequest("Prof. A", "a@example.com"));
            semesters.createSemester(new SemesterRequest(2026, 1));
            questionId = questions.addQuestion(new QuestionRequest(QuestionType.RATING,
                    "Q?", true, "LECTURER", null, null)).getId();
        }

        FeedbackSession newSession(int enrolment) {
            FeedbackSession s = sessions.createSession(new SessionRequest("EC1022", 2026, 1,
                    lic.getUserId(), TODAY, TODAY.plusDays(5), enrolment, 2));
            sessions.assignQuestions(s.getSessionId(), List.of(questionId));
            return s;
        }

        SessionRequest request(int year, int sem, String licId, int threshold) {
            return new SessionRequest("EC1022", year, sem, licId, TODAY, TODAY.plusDays(5), 40, threshold);
        }
    }

    public static void main(String[] args) {
        modules();
        lics();
        semesters();
        sessions();
        questions();
        accessCodes();
        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void modules() {
        Setup s = new Setup();
        expect(ValidationException.class,
                () -> s.modules.createModule(new ModuleRequest("EC1022", "Again", 1)),
                "duplicate module code rejected");
        try {
            s.modules.createModule(new ModuleRequest("bad", "", 9));
            check(false, "invalid module rejected");
        } catch (ValidationException e) {
            check(e.getErrors().size() == 3, "all 3 module errors reported together");
        }
        s.modules.updateModule("EC1022", new ModuleRequest("EC1022", "New name", 2));
        check(s.modules.find("EC1022").getName().equals("New name"), "module name updated");
        expect(ValidationException.class,
                () -> s.modules.updateModule("EC1022", new ModuleRequest("EC1099", "X", 1)),
                "module code cannot change");
        s.modules.deactivateModule("EC1022");
        check(!s.modules.find("EC1022").isActive(), "module is deactivated, not deleted");
        check(s.modules.listModules().size() == 1, "deactivated module is still listed");
        expect(ResourceNotFoundException.class, () -> s.modules.find("XX9999"),
                "unknown module -> not found");
    }

    private static void lics() {
        Setup s = new Setup();
        expect(ValidationException.class, () -> s.lics.createLIC(new LICRequest("B", "not-an-email")),
                "bad e-mail rejected");
        expect(ValidationException.class, () -> s.lics.createLIC(new LICRequest("B", "A@EXAMPLE.com")),
                "duplicate e-mail rejected (ignores upper/lower case)");
        LIC b = s.lics.createLIC(new LICRequest("Prof. B", "b@example.com"));
        expect(ValidationException.class,
                () -> s.lics.updateLIC(b.getUserId(), new LICRequest("Prof. B", "a@example.com")),
                "cannot take another LIC's e-mail");
        s.lics.updateLIC(b.getUserId(), new LICRequest("Prof. B2", "b2@example.com"));
        check(s.lics.find(b.getUserId()).getEmail().equals("b2@example.com"), "LIC e-mail updated");
        s.lics.deactivateLIC(b.getUserId());
        check(!s.lics.find(b.getUserId()).isActive(), "LIC deactivated");
        expect(ResourceNotFoundException.class, () -> s.lics.find("LIC-9999"), "unknown LIC -> not found");
    }

    private static void semesters() {
        Setup s = new Setup();
        expect(ValidationException.class, () -> s.semesters.createSemester(new SemesterRequest(2026, 1)),
                "duplicate semester rejected");
        expect(ValidationException.class, () -> s.semesters.createSemester(new SemesterRequest(1999, 5)),
                "invalid semester rejected");
        check(s.semesters.find(2026, 1).getLabel().equals("2026 Semester 1"), "semester label");
        expect(ResourceNotFoundException.class, () -> s.semesters.find(2026, 2),
                "semester that was never created -> not found");
    }

    private static void sessions() {
        Setup s = new Setup();
        FeedbackSession a = s.newSession(40);
        check(a.getStatus() == SessionStatus.SCHEDULED, "new session is SCHEDULED");
        expect(ValidationException.class,
                () -> s.sessions.createSession(s.request(2026, 1, s.lic.getUserId(), 5)),
                "second live session for the same module, LIC and semester rejected");
        expect(ValidationException.class,
                () -> s.sessions.createSession(s.request(2026, 1, s.lic.getUserId(), 0)),
                "threshold 0 rejected");
        expect(ValidationException.class,
                () -> s.sessions.createSession(new SessionRequest("EC1022", 2026, 1, s.lic.getUserId(),
                        TODAY.plusDays(5), TODAY, 40, 5)), "close date before open date rejected");
        expect(ResourceNotFoundException.class,
                () -> s.sessions.createSession(s.request(2026, 1, "NOBODY", 5)), "unknown LIC rejected");
        expect(ResourceNotFoundException.class,
                () -> s.sessions.createSession(s.request(2026, 2, s.lic.getUserId(), 5)),
                "semester that does not exist rejected");

        s.sessions.updateSession(a.getSessionId(),
                new SessionRequest("EC1022", 2026, 1, s.lic.getUserId(), TODAY, TODAY.plusDays(9), 50, 3));
        check(s.sessions.find(a.getSessionId()).getEnrolmentCount() == 50, "SCHEDULED session can be edited");
        expect(ValidationException.class,
                () -> s.sessions.updateSession(a.getSessionId(),
                        new SessionRequest("EC1099", 2026, 1, s.lic.getUserId(), TODAY, TODAY.plusDays(9), 50, 3)),
                "module of a session cannot be changed");

        check(s.sessions.applySchedule(TODAY) == 1, "schedule opens the session on its open date");
        check(a.getStatus() == SessionStatus.OPEN, "session is OPEN");
        expect(SessionStateException.class,
                () -> s.sessions.updateSession(a.getSessionId(),
                        new SessionRequest("EC1022", 2026, 1, s.lic.getUserId(), TODAY, TODAY.plusDays(9), 50, 3)),
                "an OPEN session cannot be edited");
        expect(SessionStateException.class,
                () -> s.sessions.assignQuestions(a.getSessionId(), List.of(s.questionId)),
                "an OPEN session cannot change its questions");
        check(s.sessions.applySchedule(TODAY.plusDays(20)) == 1, "schedule closes the session after its close date");
        expect(SessionStateException.class, () -> s.sessions.closeSession(a.getSessionId()),
                "closing a closed session rejected");
        expect(SessionStateException.class, () -> s.sessions.openSession(a.getSessionId()),
                "a closed session cannot be opened");

        FeedbackSession noQuestions = s.sessions.createSession(s.request(2026, 1, s.lic.getUserId(), 5));
        check(s.sessions.applySchedule(TODAY) == 0, "a session without questions is not opened");
        expect(SessionStateException.class, () -> s.sessions.openSession(noQuestions.getSessionId()),
                "cannot open a session that has no questions");
        s.sessions.closeSession(noQuestions.getSessionId());
        check(noQuestions.getStatus() == SessionStatus.CLOSED, "a SCHEDULED session can be closed (FR9)");
        expect(ValidationException.class, () -> s.sessions.assignQuestions("SES-0001", List.of()),
                "empty question list rejected");
        expect(ResourceNotFoundException.class, () -> s.sessions.find("SES-9999"),
                "unknown session -> not found");
    }

    private static void questions() {
        Setup s = new Setup();
        Questions mcq = s.questions.addQuestion(new QuestionRequest(QuestionType.MULTIPLE_CHOICE,
                "Workload?", true, "MODULE", List.of("Too light", "Appropriate", "Too heavy"), null));
        check(mcq instanceof MCQ, "factory builds an MCQ");
        check(mcq.Validate_Ans("b") && !mcq.Validate_Ans("d"), "MCQ accepts only the listed letters");
        Questions rate = s.questions.find(s.questionId);
        check(rate instanceof Rate_type_question && rate.Validate_Ans("5") && !rate.Validate_Ans("6"),
                "rating accepts 1 to 5 only");
        Questions text = s.questions.addQuestion(new QuestionRequest(QuestionType.TEXT,
                "Comments?", false, "MODULE", null, 10));
        check(text.Validate_Ans("short") && !text.Validate_Ans("this is far too long"),
                "text question limits the length");
        expect(ValidationException.class,
                () -> s.questions.addQuestion(new QuestionRequest(QuestionType.MULTIPLE_CHOICE,
                        "X", true, "M", List.of("only"), null)), "MCQ needs at least 2 options");
        expect(ValidationException.class,
                () -> s.questions.addQuestion(new QuestionRequest(QuestionType.MULTIPLE_CHOICE,
                        "X", true, "M", List.of("a", "A"), null)), "repeated options rejected");
        expect(ValidationException.class,
                () -> s.questions.editQuestion(mcq.getId(),
                        new QuestionRequest(QuestionType.RATING, "X", true, "M", null, null)),
                "the type of a question cannot change");
        Questions edited = s.questions.editQuestion(mcq.getId(), new QuestionRequest(
                QuestionType.MULTIPLE_CHOICE, "New text", true, "MODULE", List.of("x", "y"), null));
        check(edited.getId().equals(mcq.getId()) && edited.getQuestion().equals("New text"),
                "edit keeps the same id");
        s.questions.deactivateQuestion(mcq.getId());
        check(s.questions.listActive().stream().noneMatch(q -> q.getId().equals(mcq.getId())),
                "a deactivated question leaves the active list");
        check(s.questions.listAll().stream().anyMatch(q -> q.getId().equals(mcq.getId())),
                "a deactivated question is kept");
        FeedbackSession session = s.newSession(10);
        expect(ValidationException.class,
                () -> s.sessions.assignQuestions(session.getSessionId(), List.of(mcq.getId())),
                "a deactivated question cannot be assigned");
        expect(ResourceNotFoundException.class,
                () -> s.sessions.assignQuestions(session.getSessionId(), List.of("Q-9999")),
                "an unknown question cannot be assigned");
    }

    private static void accessCodes() {
        Setup s = new Setup();
        FeedbackSession session = s.newSession(30);
        String id = session.getSessionId();
        AccessCodeBatch batch = s.codes.generateBatch(id, 20);
        check(batch.getTotalCodes() == 20 && batch.getPlainCodes().stream().distinct().count() == 20,
                "20 unique codes made");
        check(s.codes.getSummary(id).getTotal() == 20 && s.codes.getSummary(id).getUnused() == 20,
                "summary shows counts");
        String plain = batch.getPlainCodes().get(0);
        check(!s.codeRepo.markUsedByHash(plain), "the plain code is NOT stored (anonymity)");
        check(s.codeRepo.markUsedByHash(s.generator.hash(plain)), "the hash is stored and works once");
        check(!s.codeRepo.markUsedByHash(s.generator.hash(plain)), "a code cannot be used twice");
        check(s.generator.hash(plain).equals(s.generator.hash(plain.toLowerCase().replace("-", " "))),
                "lower case or spaces give the same hash");
        check(s.codes.getSummary(id).getUsed() == 1, "summary counts one used code");
        check(!batch.toString().contains(plain), "toString does not leak the codes");
        expect(ValidationException.class, () -> s.codes.generateBatch(id, 11),
                "codes cannot pass the enrolment count");
        expect(ValidationException.class, () -> s.codes.generateBatch(id, 0), "zero codes rejected");
        check(s.codes.generateBatch(id).getTotalCodes() == 10, "generateBatch(id) makes the rest");
        expect(ValidationException.class, () -> s.codes.generateBatch(id), "no codes left to make");
        check(s.codes.getSessionLink(id).endsWith("session=" + id), "session link uses the configuration");
        check(s.codes.getQr(id).length > 0, "QR bytes returned");
        s.sessions.closeSession(id);
        expect(SessionStateException.class, () -> s.codes.generateBatch(id, 1),
                "no codes for a closed session");
        expect(ResourceNotFoundException.class, () -> s.codes.getSummary("SES-9999"),
                "unknown session -> not found");
        check(s.generator.newCode().matches("[A-Z2-9]{5}-[A-Z2-9]{5}"), "code format is XXXXX-XXXXX");
        expect(IllegalArgumentException.class, () -> new SecureCodeGenerator(3),
                "too short a code length rejected");
    }
}
