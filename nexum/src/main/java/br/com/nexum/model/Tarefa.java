package br.com.nexum.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "tarefas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_conclusao")
    private LocalDate dataConclusao;

    private String prioridade;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "lembrete_ativado")
    private Boolean lembreteAtivado;

    @Column(name = "horario_lembrete")
    private LocalDateTime horarioLembrete;
}
