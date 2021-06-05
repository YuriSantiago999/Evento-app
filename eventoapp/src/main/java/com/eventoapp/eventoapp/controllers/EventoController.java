package com.eventoapp.eventoapp.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoapp.eventoapp.models.Convidado;
import com.eventoapp.eventoapp.models.Evento;
import com.eventoapp.repository.ConvidadoRepository;
import com.eventoapp.repository.EventoRepository;


@Controller
public class EventoController {
	
	@Autowired
	private EventoRepository repository;
	
	@Autowired
	private ConvidadoRepository cr;
	
	@RequestMapping(value="/cadastrarEvento",method=RequestMethod.GET)
	public String form() {
		return "evento/formEvento";
	}
		
	@RequestMapping(value="/cadastrarEvento",method=RequestMethod.POST)
	public String form( @Validated Evento evento, BindingResult result, RedirectAttributes attributes) {
		
		if (result.hasErrors()) {
			attributes.addFlashAttribute("flashMessage", "Verifique os campos!");
			attributes.addFlashAttribute("flashType", "danger");
			return "redirect:/cadastrarEvento";
		}
		attributes.addFlashAttribute("flashMessage", "Evento adicionado com sucesso!");
		attributes.addFlashAttribute("flashType", "success");
		repository.save(evento);
		return "redirect:/cadastrarEvento";
		
		
	}
	@RequestMapping("/eventos")
	public ModelAndView listaEventos() {
		ModelAndView mv = new ModelAndView("index");
		Iterable<Evento>eventos= repository.findAll();
		mv.addObject("eventos", eventos);
		return mv;
		
		
	}
	@RequestMapping(value="/{codigo}",method=RequestMethod.GET)
	public ModelAndView detalhesEvento(@PathVariable("codigo")long codigo) {
		Evento evento= repository.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("evento/detalhesEvento");
		mv.addObject("evento", evento);
		Iterable<Convidado>convidados=cr.findByEvento(evento);
		mv.addObject("convidados", convidados);
		return mv;
		
	}
	
	@RequestMapping(value="/{codigo}",method=RequestMethod.POST)
	public String detalhesEventoPost(@PathVariable("codigo")long codigo , @Validated Convidado convidado, BindingResult result, RedirectAttributes attributes) {
		Evento evento=repository.findByCodigo(codigo);
		if (result.hasErrors()) {
			attributes.addFlashAttribute("flashMessage", "Verifique os campos!");
			attributes.addFlashAttribute("flashType", "danger");			
			return "redirect:/{codigo}";
		}
		attributes.addFlashAttribute("flashMessage", "Convidado adicionado com sucesso!");
		attributes.addFlashAttribute("flashType", "success");
		convidado.setEvento(evento);
		cr.save(convidado);
		return "redirect:/{codigo}";
		
	}
	@RequestMapping("/deletarEvento")
	public String deletarEvento(long codigo,@Validated Evento event, BindingResult result, RedirectAttributes attributes){
		Evento evento= repository.findByCodigo(codigo);
		attributes.addFlashAttribute("flashMessage", "Evento deletado com sucesso!");
		attributes.addFlashAttribute("flashType", "success");
		
		repository.delete(evento);
		return "redirect:/eventos";
		
	}
	@RequestMapping("/deletarConvidado")
	public String deletarConvidado(String cpf) {
		Convidado convidado=cr.findByCpf(cpf);
		cr.delete(convidado);
		Evento evento=convidado.getEvento();
		long codLong= evento.getCodigo();
		String cod=" "+codLong;
		return "redirect:/"+cod;
		
	}
		
		
				
		
		
		
	
	
	

}
