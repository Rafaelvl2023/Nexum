package br.com.nexum.controller;

import br.com.nexum.model.Tarefa;
import br.com.nexum.repository.TarefaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class FluxoController {

    @Autowired
    private TarefaRepository tarefaRepository;

    @GetMapping("/fluxoTarefas")
    public String kanban(Model model) {
        List<Tarefa> tarefasBaixa = tarefaRepository.findByPrioridade("BAIXA");
        List<Tarefa> tarefasMedia = tarefaRepository.findByPrioridade("MEDIA");
        List<Tarefa> tarefasAlta = tarefaRepository.findByPrioridade("ALTA");

        model.addAttribute("tarefasBaixa", tarefasBaixa);
        model.addAttribute("tarefasMedia", tarefasMedia);
        model.addAttribute("tarefasAlta", tarefasAlta);

        return "fluxoTarefas";
    }

    public static class PrioridadeUpdateDTO {
        private String prioridade;

        public String getPrioridade() {
            return prioridade;
        }

        public void setPrioridade(String prioridade) {
            this.prioridade = prioridade;
        }
    }

    @PostMapping("/prioridade/editar/{id}")
    @ResponseBody
    public ResponseEntity<?> editarPrioridade(@PathVariable Long id, @RequestBody PrioridadeUpdateDTO dto) {
        var tarefaOpt = tarefaRepository.findById(id);
        if (tarefaOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Tarefa não encontrada");
        }

        var tarefa = tarefaOpt.get();

        String novaPrioridade = dto.getPrioridade();
        if (!novaPrioridade.equals("BAIXA") && !novaPrioridade.equals("MEDIA") && !novaPrioridade.equals("ALTA")) {
            return ResponseEntity.badRequest().body("Prioridade inválida");
        }

        tarefa.setPrioridade(novaPrioridade);
        tarefaRepository.save(tarefa);

        return ResponseEntity.ok("Prioridade atualizada com sucesso");
    }

    @PostMapping("/tarefa/fluxo/excluir/{id}")
    public String excluirTarefa(@PathVariable Long id) {
        tarefaRepository.deleteById(id);
        return "redirect:/fluxoTarefas";
    }
}
