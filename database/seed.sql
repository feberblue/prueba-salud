-- Seed Data for medicamentos_db
-- Note: Scripts are executed in the context of the database specified in MYSQL_DATABASE

-- Insert sample medicamentos (POS)
INSERT INTO medicamentos (nombre, descripcion, es_no_pos) VALUES
-- Analgésicos y Antipiréticos
('Acetaminofén 500mg', 'Analgésico y antipirético para dolor leve a moderado', FALSE),
('Acetaminofén 1g', 'Analgésico y antipirético de mayor concentración', FALSE),
('Dipirona 500mg', 'Analgésico y antipirético para dolor y fiebre', FALSE),

-- Antiinflamatorios
('Ibuprofeno 400mg', 'Antiinflamatorio no esteroideo para dolor e inflamación', FALSE),
('Ibuprofeno 600mg', 'Antiinflamatorio de mayor concentración', FALSE),
('Diclofenaco 50mg', 'Antiinflamatorio para dolor articular', FALSE),
('Naproxeno 500mg', 'Antiinflamatorio para dolor crónico', FALSE),
('Meloxicam 15mg', 'Antiinflamatorio para artritis', FALSE),

-- Antibióticos
('Amoxicilina 500mg', 'Antibiótico de amplio espectro', FALSE),
('Amoxicilina + Ácido Clavulánico 875mg', 'Antibiótico de amplio espectro reforzado', FALSE),
('Azitromicina 500mg', 'Antibiótico macrólido para infecciones respiratorias', FALSE),
('Cefalexina 500mg', 'Antibiótico cefalosporina de primera generación', FALSE),
('Ciprofloxacina 500mg', 'Antibiótico fluoroquinolona', FALSE),

-- Antihipertensivos
('Losartán 50mg', 'Antihipertensivo para control de presión arterial', FALSE),
('Enalapril 10mg', 'Inhibidor de la ECA para hipertensión', FALSE),
('Amlodipino 5mg', 'Bloqueador de canales de calcio', FALSE),
('Hidroclorotiazida 25mg', 'Diurético para hipertensión', FALSE),

-- Antidiabéticos
('Metformina 850mg', 'Antidiabético oral para diabetes tipo 2', FALSE),
('Metformina 500mg', 'Antidiabético oral de menor concentración', FALSE),
('Glibenclamida 5mg', 'Antidiabético oral sulfonilurea', FALSE),

-- Gastrointestinales
('Omeprazol 20mg', 'Inhibidor de bomba de protones para acidez estomacal', FALSE),
('Ranitidina 150mg', 'Antagonista H2 para acidez', FALSE),
('Metoclopramida 10mg', 'Antiemético y procinético', FALSE),
('Loperamida 2mg', 'Antidiarreico', FALSE),

-- Cardiovasculares
('Atorvastatina 20mg', 'Estatina para control de colesterol', FALSE),
('Simvastatina 40mg', 'Estatina para dislipidemia', FALSE),
('Ácido Acetilsalicílico 100mg', 'Antiagregante plaquetario', FALSE),

-- Respiratorios
('Salbutamol Inhalador', 'Broncodilatador para asma y EPOC', FALSE),
('Salbutamol Jarabe', 'Broncodilatador en presentación oral', FALSE),
('Loratadina 10mg', 'Antihistamínico para alergias', FALSE),
('Cetirizina 10mg', 'Antihistamínico de segunda generación', FALSE),
('Dextrometorfano 15mg', 'Antitusivo para tos seca', FALSE),

-- Vitaminas y Suplementos
('Ácido Fólico 1mg', 'Suplemento vitamínico', FALSE),
('Complejo B', 'Vitaminas del complejo B', FALSE),
('Vitamina C 500mg', 'Suplemento de ácido ascórbico', FALSE),
('Calcio + Vitamina D', 'Suplemento para salud ósea', FALSE),

-- Otros
('Clonazepam 2mg', 'Ansiolítico y anticonvulsivante', FALSE),
('Fluoxetina 20mg', 'Antidepresivo inhibidor selectivo de recaptación de serotonina', FALSE),
('Levotiroxina 100mcg', 'Hormona tiroidea para hipotiroidismo', FALSE);

-- Insert sample medicamentos (NO POS)
INSERT INTO medicamentos (nombre, descripcion, es_no_pos) VALUES
-- Medicamentos Biológicos para Enfermedades Autoinmunes
('Adalimumab 40mg', 'Medicamento biológico para artritis reumatoide', TRUE),
('Etanercept 50mg', 'Inhibidor del TNF para artritis reumatoide y psoriasis', TRUE),
('Infliximab 100mg', 'Anticuerpo monoclonal para enfermedad de Crohn', TRUE),
('Tocilizumab 200mg', 'Inhibidor de IL-6 para artritis reumatoide', TRUE),
('Ustekinumab 45mg', 'Anticuerpo monoclonal para psoriasis', TRUE),

-- Oncológicos
('Rituximab 500mg', 'Anticuerpo monoclonal para linfoma', TRUE),
('Trastuzumab 440mg', 'Terapia dirigida para cáncer de mama HER2+', TRUE),
('Pembrolizumab 100mg', 'Inmunoterapia para diversos tipos de cáncer', TRUE),
('Nivolumab 100mg', 'Inmunoterapia anti-PD-1 para melanoma', TRUE),
('Bevacizumab 400mg', 'Anticuerpo anti-VEGF para cáncer colorrectal', TRUE),
('Imatinib 400mg', 'Inhibidor de tirosina quinasa para leucemia mieloide crónica', TRUE),
('Erlotinib 150mg', 'Inhibidor de EGFR para cáncer de pulmón', TRUE),

-- Antivirales de Alto Costo
('Sofosbuvir 400mg', 'Antiviral para hepatitis C crónica', TRUE),
('Ledipasvir/Sofosbuvir 90/400mg', 'Combinación para hepatitis C', TRUE),
('Daclatasvir 60mg', 'Antiviral de acción directa para hepatitis C', TRUE),

-- Enfermedades Raras
('Eculizumab 300mg', 'Tratamiento para hemoglobinuria paroxística nocturna', TRUE),
('Nusinersen 12mg', 'Tratamiento para atrofia muscular espinal', TRUE),
('Alglucosidasa alfa 50mg', 'Terapia de reemplazo enzimático para enfermedad de Pompe', TRUE),
('Idursulfasa 2mg', 'Terapia enzimática para síndrome de Hunter', TRUE),

-- Esclerosis Múltiple
('Ocrelizumab 300mg', 'Tratamiento para esclerosis múltiple', TRUE),
('Natalizumab 300mg', 'Anticuerpo monoclonal para esclerosis múltiple', TRUE),
('Fingolimod 0.5mg', 'Modulador de receptores de esfingosina-1-fosfato', TRUE),
('Dimetilfumarato 240mg', 'Inmunomodulador para esclerosis múltiple', TRUE),

-- Hemofilia
('Factor VIII Recombinante 1000UI', 'Factor de coagulación para hemofilia A', TRUE),
('Factor IX Recombinante 1000UI', 'Factor de coagulación para hemofilia B', TRUE),

-- Hormonas de Crecimiento
('Somatropina 5mg', 'Hormona de crecimiento humana recombinante', TRUE),

-- Inmunosupresores de Alto Costo
('Tacrolimus 5mg', 'Inmunosupresor para trasplante de órganos', TRUE),
('Micofenolato de Mofetilo 500mg', 'Inmunosupresor para trasplante renal', TRUE),
('Everolimus 10mg', 'Inmunosupresor para trasplante de órganos', TRUE),

-- Oftalmológicos de Alto Costo
('Ranibizumab 10mg/ml', 'Tratamiento intravítreo para degeneración macular', TRUE),
('Aflibercept 40mg/ml', 'Anti-VEGF para edema macular diabético', TRUE);

-- Insert sample user (password is 'password123' encrypted with BCrypt)
-- Note: In production, passwords should be created through the registration endpoint
INSERT INTO usuarios (username, email, password) VALUES
('admin', 'admin@medicamentos.com', '$2a$10$UzUvp0SJ0nu6.KiIeRF6Ru8kNGcYlqV58n9JzbrvZUvq1rjbA26lC'),
('usuario1', 'usuario1@example.com', '$2a$10$UzUvp0SJ0nu6.KiIeRF6Ru8kNGcYlqV58n9JzbrvZUvq1rjbA26lC');

-- Insert sample solicitudes
INSERT INTO solicitudes (usuario_id, medicamento_id, estado) VALUES
(1, 1, 'APROBADA'),
(1, 3, 'PENDIENTE'),
(2, 2, 'APROBADA');

-- Insert sample solicitud for NO POS medication
INSERT INTO solicitudes (usuario_id, medicamento_id, numero_orden, direccion, telefono, correo, estado) VALUES
(1, 11, 'ORD-2024-001', 'Calle 123 #45-67, Bogotá', '3001234567', 'admin@medicamentos.com', 'PENDIENTE'),
(2, 13, 'ORD-2024-002', 'Carrera 45 #12-34, Medellín', '3009876543', 'usuario1@example.com', 'APROBADA');
