SET search_path TO tran, public;
INSERT INTO organization(name) VALUES ('National Informatics Centre - Tripura') ON CONFLICT DO NOTHING;
INSERT INTO office(organization_id,name) SELECT id,'NIC Tripura State Centre' FROM organization WHERE name='National Informatics Centre - Tripura' ON CONFLICT DO NOTHING;
INSERT INTO department(office_id,name,code) SELECT id,v.name,v.code FROM office CROSS JOIN (VALUES ('Administration','ADM'),('Computer Science','CS'),('Information Technology','IT'),('Finance','FIN'),('Human Resources','HR'),('General','GEN')) AS v(name,code) ON CONFLICT(name) DO NOTHING;

INSERT INTO tran.acr_cycle(assessment_year,due_date,is_open) VALUES ('2026-2027', CURRENT_DATE + 30, TRUE) ON CONFLICT (assessment_year) DO NOTHING;
