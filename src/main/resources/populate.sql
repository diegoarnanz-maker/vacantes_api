USE vacantes_BBDD_2025_RETO;

-- CATEGORÍAS
INSERT INTO Categorias (nombre, descripcion) VALUES
('Informática', 'Ofertas relacionadas con el sector tecnológico'),
('Marketing', 'Ofertas en el área de comunicación y marketing'),
('Educación', 'Ofertas para docentes y formadores');

-- USUARIOS
INSERT INTO Usuarios (email, nombre, apellidos, password, enabled, fecha_Registro, rol) VALUES
('admin@email.com', 'Ana', 'Administradora', '$2a$10$Dow1bYupDArN8XwTfAMK9.FZlDdkXvnTPh9n9I9RIwOY4tvRqt7CW', 1, CURDATE(), 'ADMON'),
('empresa@email.com', 'Carlos', 'Empresa S.L.', '$2a$10$Dow1bYupDArN8XwTfAMK9.FZlDdkXvnTPh9n9I9RIwOY4tvRqt7CW', 1, CURDATE(), 'EMPRESA'),
('cliente@email.com', 'Laura', 'Gómez', '$2a$10$Dow1bYupDArN8XwTfAMK9.FZlDdkXvnTPh9n9I9RIwOY4tvRqt7CW', 1, CURDATE(), 'CLIENTE');

-- EMPRESA (vinculada al usuario "empresa@email.com")
INSERT INTO Empresas (cif, nombre_empresa, direccion_fiscal, pais, email) VALUES
('B12345678', 'Empresa Tech S.L.', 'Calle Falsa 123', 'España', 'empresa@email.com');

-- VACANTES
INSERT INTO Vacantes (nombre, descripcion, fecha, salario, estatus, destacado, imagen, detalles, id_Categoria, id_empresa) VALUES
('Desarrollador Java', 'Backend con Spring Boot', CURDATE(), 28000, 'CREADA', 1, 'java.jpg', 'Buscamos perfil junior con ganas de crecer', 1, 1),
('Marketing Digital', 'SEO, SEM y RRSS', CURDATE(), 25000, 'CREADA', 0, 'marketing.jpg', 'Empresa joven busca creativo/a', 2, 1);

-- SOLICITUDES (del usuario cliente a la primera vacante)
INSERT INTO Solicitudes (fecha, archivo, comentarios, estado, curriculum, id_Vacante, email) VALUES
(CURDATE(), 'cv_laura.pdf', 'Estoy muy interesada en la vacante', 0, 'CV Laura Gómez', 1, 'cliente@email.com');
