# Report requirements matrix

| ID | Requirement | Implementation |
|---|---|---|
| FR-1 | Employee registration/login with hashed credentials | `AuthController`, BCrypt, JWT filter |
| FR-2 | Authority employee add/edit/activate/deactivate/delete | `EmployeeController` |
| FR-3 | Search/filter employee directory | `EmployeeController` query filters + UI |
| FR-4 | Notifications to one/multiple/department/all | `NotificationController` |
| FR-5 | APAR only after authority request | `AcrController` request/submit gate |
| FR-6 | Full APAR lifecycle status | `employee_acr_status.status` |
| FR-7 | Grade/remark/flag/resubmission reason | review and return endpoints |
| FR-8 | One authorised APAR per year | unique employee/year constraint |
| FR-9 | Audit and revision-wise history | `acr_audit`, `acr_submission_history`, immutable triggers |
| FR-10 | Recipient notifications/read/unread count | recipient-scoped endpoints |
| NFR-1 | BCrypt, stateless JWT, role guards | Spring Security configuration |
| NFR-2 | Relational integrity | PostgreSQL PK/FK/check/unique constraints |
| NFR-3 | Responsive government interface | maroon/white responsive UI |
| NFR-4 | Reproducible Docker | Compose + Dockerfiles |
| NFR-5 | Layered architecture | controller/service/repository packages |
| NFR-6 | Polling | 10-second UI refresh; scheduled reminders |

## Validation note
A source package cannot prove live hosted operation. Run Docker Compose and the demo checklist, then validate against the actual hosted PostgreSQL instance.
