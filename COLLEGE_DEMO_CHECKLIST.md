# College demo checklist

1. Start with `docker compose up --build`; verify frontend, API health and PostgreSQL.
2. Sign in as `authority@nic.in` / `Demo@12345`.
3. Open Employees: search by name/email/ID; add an employee; edit; deactivate/reactivate.
4. Open Assessment Cycle: set assessment year, deadline and open state.
5. Send an APAR request to the demo employee.
6. Send a notification to one employee, a department and all employees.
7. Sign out; sign in as `employee@nic.in` / `Demo@12345`.
8. Verify APAR request and notifications; mark one read and then mark all read.
9. Submit targets and achievements. Confirm a second initial submission is blocked.
10. Sign in as authority; review, grade, remark, flag and approve.
11. For a second test record, request resubmission with a reason; sign in as employee and resubmit.
12. Show revision history and audit trail.
13. Test employee cannot open authority pages/API; test inactive account cannot log in.

Demo data is fictional and local-only. Do not use sample credentials in production.
