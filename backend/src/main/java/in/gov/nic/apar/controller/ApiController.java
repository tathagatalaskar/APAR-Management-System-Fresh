package in.gov.nic.apar.controller;

import in.gov.nic.apar.service.AparService; import in.gov.nic.apar.security.JwtService; import org.springframework.beans.factory.annotation.Value; import org.springframework.http.ResponseEntity; import org.springframework.jdbc.core.JdbcTemplate; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping public class ApiController {
 private final AparService svc; private final JdbcTemplate db; private final PasswordEncoder encoder; private final JwtService jwt; private final boolean bootstrap;
 public ApiController(AparService s,JdbcTemplate d,PasswordEncoder e,JwtService j,@Value("${app.bootstrap-enabled}") boolean b){svc=s;db=d;encoder=e;jwt=j;bootstrap=b;}
 @GetMapping("/health") public Map<String,Object> health(){db.queryForObject("select 1",Integer.class);return Map.of("status","UP","database","UP");}
 @PostMapping("/auth/login") public Map<String,Object> login(@RequestBody Map<String,String>b){return svc.login(b.getOrDefault("email",""),b.getOrDefault("password",""));}
 @PostMapping("/auth/register") public Map<String,Object> register(@RequestBody Map<String,Object>b){return svc.register(b);}
 @GetMapping("/auth/bootstrap-status") public Map<String,Boolean> bootstrapStatus(){Integer count=db.queryForObject("select count(*) from tran.user_account where role='AUTHORITY'",Integer.class);return Map.of("available",bootstrap&&(count==null||count==0));}
 @PostMapping("/auth/bootstrap-authority") public ResponseEntity<?> bootstrap(@RequestBody Map<String,String>b){if(!bootstrap)return ResponseEntity.notFound().build();Integer count=db.queryForObject("select count(*) from tran.user_account where role='AUTHORITY'",Integer.class);if(count!=null&&count>0)return ResponseEntity.status(409).body(Map.of("error","An authority already exists"));String email=b.get("email");String password=b.get("password");if(email==null||password==null||password.length()<12)return ResponseEntity.badRequest().body(Map.of("error","Email and password (12+ characters) required"));long id=db.queryForObject("insert into tran.user_account(email,password_hash,role,enabled) values(?,?, 'AUTHORITY',true) returning id",Long.class,email,encoder.encode(password));return ResponseEntity.ok(Map.of("token",jwt.issue(email,"AUTHORITY",id),"role","AUTHORITY","email",email));}
 @GetMapping("/v1/meta/departments") public List<Map<String,Object>> departments(){return svc.departments();}
 @PreAuthorize("hasRole('AUTHORITY')") @GetMapping("/v1/employees") public List<Map<String,Object>> employees(@RequestParam(defaultValue="")String q,@RequestParam(defaultValue="")String department,@RequestParam(defaultValue="")String active){return svc.employees(q,department,active);}
 @PreAuthorize("hasRole('AUTHORITY')") @PostMapping("/v1/employees") public Map<String,Object> addEmployee(@RequestBody Map<String,Object>b){return svc.addEmployee(b);}
 @PreAuthorize("hasRole('AUTHORITY')") @PutMapping("/v1/employees/{id}") public ResponseEntity<?> update(@PathVariable long id,@RequestBody Map<String,Object>b){svc.updateEmployee(id,b);return ResponseEntity.noContent().build();}
 @PreAuthorize("hasRole('AUTHORITY')") @PatchMapping("/v1/employees/{id}/active") public ResponseEntity<?> active(@PathVariable long id,@RequestBody Map<String,Boolean>b){svc.active(id,Boolean.TRUE.equals(b.get("active")));return ResponseEntity.noContent().build();}
 @PreAuthorize("hasRole('AUTHORITY')") @DeleteMapping("/v1/employees/{id}") public ResponseEntity<?> delete(@PathVariable long id){svc.deleteEmployee(id);return ResponseEntity.noContent().build();}
 @PreAuthorize("hasRole('AUTHORITY')") @GetMapping("/v1/cycles/active") public Map<String,Object> cycle(){return svc.cycle();}
 @PreAuthorize("hasRole('AUTHORITY')") @PutMapping("/v1/cycles/active") public ResponseEntity<?> setCycle(@RequestBody Map<String,Object>b){svc.setCycle(b);return ResponseEntity.noContent().build();}
 @PreAuthorize("hasRole('AUTHORITY')") @PostMapping("/v1/acr/reminders") public ResponseEntity<?> request(@RequestBody Map<String,Object>b){List<Long> ids=((List<?>)b.getOrDefault("employeeIds",List.of())).stream().map(x->((Number)x).longValue()).toList();svc.requestApar(ids,String.valueOf(b.getOrDefault("message","Please submit your APAR.")));return ResponseEntity.noContent().build();}
 @GetMapping("/v1/acr/eligibility") public Map<String,Object> eligibility(){return svc.eligibility();}
 @GetMapping("/v1/acr") public List<Map<String,Object>> acrs(){return svc.acrList();}
 @PostMapping("/v1/acr/submit") public ResponseEntity<?> submit(@RequestBody Map<String,Object>b){Long employeeId=((Number)db.queryForObject("select e.id from tran.employee_details e where lower(e.email)=lower(?)",Long.class,org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName())).longValue();svc.submit(employeeId,(List<Map<String,Object>>)b.getOrDefault("entries",List.of()));return ResponseEntity.noContent().build();}
 @GetMapping("/v1/acr/{id}") public Map<String,Object> acr(@PathVariable long id){return svc.acr(id);}
 @PreAuthorize("hasRole('AUTHORITY')") @PutMapping("/v1/acr/{id}/review") public ResponseEntity<?> review(@PathVariable long id,@RequestBody Map<String,Object>b){svc.review(id,b);return ResponseEntity.noContent().build();}
 @PreAuthorize("hasRole('AUTHORITY')") @PutMapping("/v1/acr/{id}/request-resubmission") public ResponseEntity<?> resubmit(@PathVariable long id,@RequestBody Map<String,String>b){svc.returnForRevision(id,b.getOrDefault("reason",""));return ResponseEntity.noContent().build();}
 @GetMapping("/v1/acr/{id}/history") public List<Map<String,Object>> history(@PathVariable long id){return svc.history(id);}
 @GetMapping("/v1/acr/{id}/audit") public List<Map<String,Object>> audit(@PathVariable long id){return svc.audit(id);}
 @GetMapping("/v1/notifications") public List<Map<String,Object>> notifications(){return svc.notifications();}
 @GetMapping("/v1/notifications/unread-count") public Map<String,Integer> unread(){return Map.of("count",svc.unread());}
 @PatchMapping("/v1/notifications/{id}/read") public ResponseEntity<?> read(@PathVariable long id){svc.read(id);return ResponseEntity.noContent().build();}
 @PatchMapping("/v1/notifications/read-all") public ResponseEntity<?> readAll(){svc.readAll();return ResponseEntity.noContent().build();}
 @PreAuthorize("hasRole('AUTHORITY')") @PostMapping("/v1/notifications/send") public ResponseEntity<?> send(@RequestBody Map<String,Object>b){svc.sendNotification(b);return ResponseEntity.noContent().build();}
}
