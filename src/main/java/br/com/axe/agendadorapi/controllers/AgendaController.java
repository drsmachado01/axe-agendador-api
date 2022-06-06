package br.com.axe.agendadorapi.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.axe.agendadorapi.domain.dto.AgendaDTO;
import br.com.axe.agendadorapi.domain.model.Agenda;
import br.com.axe.agendadorapi.services.AgendaService;

@RestController
@RequestMapping("/agenda")
public class AgendaController {
	private static final String ID = "/{id}";
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private AgendaService service;

	@GetMapping
	public ResponseEntity<List<AgendaDTO>> list() {
		return ResponseEntity.ok().body(service.list().stream().map(a -> mapper.map(a, AgendaDTO.class)).collect(Collectors.toList()));
	}
	
	@GetMapping(ID)
	public ResponseEntity<AgendaDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(mapper.map(service.findById(id), AgendaDTO.class));
	}
	
	@GetMapping("/date/{theDate}")
	public ResponseEntity<List<AgendaDTO>> listAgendaOfSpecificDate(@PathVariable LocalDate theDate) {
		Agenda agenda = Agenda.builder().theDate(theDate).build();
		return ResponseEntity.ok().body(service.findByTheDate(agenda).stream().map(a -> mapper.map(a, AgendaDTO.class)).collect(Collectors.toList()));
	}
	
	@GetMapping("/{theDate}/{theTime}")
	public ResponseEntity<AgendaDTO> findByDateAndTime(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate theDate, 
			@PathVariable @DateTimeFormat(pattern = "HH:mm:ss") LocalTime theTime) {
		Agenda agenda = Agenda.builder().theDate(theDate).theTime(theTime).build();
		return ResponseEntity.ok().body(mapper.map(service.findByTheDateAndTheTime(agenda), AgendaDTO.class));
	}
	
	@PostMapping
	public ResponseEntity<AgendaDTO> save(@RequestBody AgendaDTO agendaDTO) {
		Agenda agenda = mapper.map(agendaDTO, Agenda.class);
		agendaDTO = mapper.map(service.save(agenda), AgendaDTO.class);
		return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri().path(ID)
                .buildAndExpand(agendaDTO.getIdAgenda()).toUri()).build();
	}
	
	@PutMapping(ID)
	public ResponseEntity<AgendaDTO> update(@PathVariable Long id, @RequestBody AgendaDTO agendaDTO) {
		Agenda agenda = mapper.map(agendaDTO, Agenda.class);
		agendaDTO = mapper.map(service.update(id, agenda), AgendaDTO.class);
		return ResponseEntity.ok().body(agendaDTO);
	}
	
	@DeleteMapping(ID)
	public ResponseEntity<AgendaDTO> delete(@PathVariable Long id) {
		service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
