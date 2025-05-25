package br.com.nexum.repository;

import br.com.nexum.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    @Query(value = "SELECT * FROM tarefas " +
            "WHERE lembrete_ativado = 1 " +
            "AND horario_lembrete >= NOW() " +
            "ORDER BY horario_lembrete ASC " +
            "LIMIT 10", nativeQuery = true)
    List<Tarefa> buscarLembretesMaisProximos();

    @Query("SELECT t FROM Tarefa t WHERE " +
        "(:prioridade IS NULL OR t.prioridade = :prioridade) AND " +
        "(:lembrete IS NULL OR t.lembreteAtivado = :lembrete)")
    Page<Tarefa> filtrarTarefas(
        @Param("prioridade") String prioridade,
        @Param("lembrete") Boolean lembrete,
        Pageable pageable
    );

    List<Tarefa> findByPrioridade(String prioridade);
}
