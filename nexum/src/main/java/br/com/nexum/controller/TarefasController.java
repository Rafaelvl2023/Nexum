package br.com.nexum.controller;

import br.com.nexum.model.Tarefa;
import br.com.nexum.repository.TarefaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Controller
public class TarefasController {

    @Autowired
    public TarefaRepository tarefaRepository;

    @GetMapping("/")
    public String listarTarefas(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String prioridade,
            @RequestParam(required = false) Boolean lembrete) {

        List<Tarefa> lembretes = tarefaRepository.buscarLembretesMaisProximos();
        model.addAttribute("lembretes", lembretes);

        int pageSize = 9;
        Pageable pageable = PageRequest.of(page, pageSize);

        if (prioridade != null && prioridade.trim().isEmpty()) {
            prioridade = null;
        }

        Page<Tarefa> tarefasPage = tarefaRepository.filtrarTarefas(prioridade, lembrete, pageable);

        model.addAttribute("tarefas", tarefasPage.getContent());
        model.addAttribute("currentPage", tarefasPage.getNumber());
        model.addAttribute("totalPages", tarefasPage.getTotalPages());

        model.addAttribute("filtroPrioridade", prioridade);
        model.addAttribute("filtroLembrete", lembrete);

        return "tarefas";
    }

    @PostMapping("/cadastrarTarefa")
    public String cadastrarTarefa(@ModelAttribute Tarefa tarefa) {
        if (tarefa.getLembreteAtivado() == null) {
            tarefa.setLembreteAtivado(false);
        }

        tarefaRepository.save(tarefa);
        return "redirect:/";
    }

    @PostMapping("/tarefas/editar/{id}")
    public String editarTarefa(@PathVariable Long id, @ModelAttribute Tarefa tarefaAtualizada,
            RedirectAttributes redirectAttributes) {
        Tarefa tarefaExistente = tarefaRepository.findById(id).orElse(null);

        if (tarefaExistente != null) {
            tarefaExistente.setTitulo(tarefaAtualizada.getTitulo());
            tarefaExistente.setDescricao(tarefaAtualizada.getDescricao());
            tarefaExistente.setDataConclusao(tarefaAtualizada.getDataConclusao());
            tarefaExistente.setPrioridade(tarefaAtualizada.getPrioridade());
            tarefaExistente.setUsuarioId(tarefaAtualizada.getUsuarioId());
            tarefaExistente.setLembreteAtivado(tarefaAtualizada.getLembreteAtivado());

            tarefaRepository.save(tarefaExistente);

            redirectAttributes.addFlashAttribute("mensagem", "Tarefa atualizada com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("erro", "Tarefa não encontrada.");
        }

        return "redirect:/";
    }

    @PostMapping("/tarefas/excluir/{id}")
    public String excluirTarefa(@PathVariable Long id) {
        tarefaRepository.deleteById(id);
        return "redirect:/";
    }
}
