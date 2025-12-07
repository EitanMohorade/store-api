-- Script de datos de prueba para H2 (opcional)
-- Este archivo se ejecuta automáticamente durante los tests si está en src/test/resources

-- Insertar categorías de ejemplo
INSERT INTO categoria (nombre, descripcion) VALUES 
('Electrónica', 'Productos electrónicos'),
('Ropa', 'Prendas de vestir'),
('Alimentos', 'Productos alimenticios');

-- Insertar compañías de ejemplo
INSERT INTO compania (nombre) VALUES 
('CompañiaA'),
('CompañiaB');
