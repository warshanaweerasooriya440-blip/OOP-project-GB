# G77 Admin Panel

EC2493 Object Oriented Programming Semester Project | Company B | Main Group B2 | Subgroup G77

The Admin Panel part of the Anonymous Student Feedback System. The Administrator uses it to
set up modules, lecturers (LICs), semesters, feedback sessions and the question bank, and to
generate single-use access codes for a session.

This is the **Week 2 version** (core domain and data). It runs without a database or web page.

## Team

- Nilamdeen O.N. (EN25300528)
- Kumasaru S.T. (EN25300531)

## What works now

| Feature | Where |
|---|---|
| Create, update and deactivate modules (FR7) | `ModuleService` |
| Create, update and deactivate LICs with e-mail (FR7) | `LICService` |
| Create semesters (FR7) | `SemesterService` |
| Question bank: add, edit, deactivate (FR8) | `QuestionBankService`, `QuestionFactory` |
| Sessions: create, edit while SCHEDULED, assign questions, open, close, schedule (FR7 to FR9) | `SessionService` |
| Single-use access codes, session link and QR placeholder (FR9, Section 8) | `AccessCodeService` |
| Validation that reports every mistake at once (FR4) | `RequestValidator` |

## Project structure

Base package: `com.feedback.admin`

| Package | Contents |
|---|---|
| `domain` | Module, Semester, LIC, FeedbackSession, SessionStatus, AccessCodeBatch, CodeSummary, Alert, AlertType (the Alert classes are ready; AlertService comes in Week 3) |
| `request` | Data typed in by the Administrator: ModuleRequest, LICRequest, SemesterRequest, SessionRequest, QuestionRequest, QuestionType |
| `service` | Business rules: the services above, RequestValidator, CodeGenerator, SecureCodeGenerator, QrCodeGenerator, PlaceholderQrCodeGenerator |
| `repository` | Interfaces for saving and reading data |
| `repository.memory` | In-memory versions of the repositories (no database needed) |
| `exception` | ValidationException, SessionStateException, ResourceNotFoundException |
| `util` | Rules (validation constants), IdGenerator |
| `config` | AdminConfig (settings from a properties file) |
| `stub` | Stand-ins for other subgroups' classes (see below) |

## Build and run

Requirements: Java 21 (set in `pom.xml`) and Maven. NetBeans is used for development.

1. Open the project in NetBeans (File, Open Project, select the `G77_Admin_Panel` folder).
2. Clean and Build: `Shift + F11`. The output must end with `BUILD SUCCESS`.
3. Run the demo: open `G77_Admin_Panel.java` and press `Shift + F6`.
4. Run the self-test: open `AdminSelfTest.java` and press `Shift + F6`. The last line must be
   `67 passed, 0 failed`.

## Configuration

The demo and the self-test use the built-in defaults. A properties file is read only when
`AdminConfig.load(path)` is called (the web layer will do this in a later week).

To use a file:

1. Copy `config/admin.properties.example` to `config/admin.properties`.
2. Edit the values:

| Key | Meaning | Default |
|---|---|---|
| `admin.baseUrl` | Address students open (used for the session link) | `http://localhost:8080` |
| `admin.maxCodesPerBatch` | Most access codes made in one call | `1000` |
| `admin.codeLength` | Characters in an access code (at least 6) | `10` |

`config/admin.properties` is listed in `.gitignore`. Never commit passwords or real credentials.

## Anonymity design

- Access codes are random (`SecureRandom`) and hashed with SHA-256. Only the **hash** is stored.
- The plain codes exist only in `AccessCodeBatch`, returned once when the batch is made.
  Its `toString()` never prints them, so they cannot leak into logs.
- `AccessCodeRepository` has **no method** that returns who used a code or when, so that
  information cannot be read. It only counts total and used codes.
- Hashes are shuffled before saving and kept in a plain `HashMap`, so the stored order says
  nothing about when codes were made or used.
- The session link and QR code are the same for every student and contain no code.
- The number of codes for a session can never pass its enrolment count.

## Stand-ins (temporary classes)

Some classes belong to other subgroups. Until they are ready, look-alikes in the `stub` package let
this project compile and run alone.

| Stand-in | Real owner | Note |
|---|---|---|
| `Questions`, `Rate_type_question`, `MCQ`, `Text_Question` | G8 | G8's classes must be public, one class per file, with a constructor and an `active` flag |
| `PlaceholderQrCodeGenerator` | G77 | Returns the link as text, not a real QR image. To be replaced with a QR library |
| In-memory repositories | G18 | G18 writes database versions behind the same interfaces |

When integrating, delete the stand-in and use the real class.

## Not included yet (Week 3 and 4)

- Alert, report management and e-mail delivery status services
- Web pages and the real QR image
- Database implementation of the repositories (G18)
- JUnit 5 tests and JaCoCo coverage report
