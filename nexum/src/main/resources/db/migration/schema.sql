CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE tarefas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT,
    data_conclusao DATE,
    prioridade ENUM('BAIXA', 'MEDIA', 'ALTA') DEFAULT 'MEDIA',
    usuario_id INT NOT NULL,
    lembrete_ativado BOOLEAN DEFAULT FALSE,
    horario_lembrete DATETIME,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
