package br.com.axe.agendadorapi.services;

import java.time.LocalDate;
import java.util.List;

import br.com.axe.agendadorapi.domain.model.Agenda;

public interface AgendaService {
	List<Agenda> list();
	
	List<Agenda> findByTheDate(LocalDate theDate);
	
	Agenda findById(Long id);

	Agenda save(Agenda agenda);
	
	Agenda update(Long id, Agenda agenda);
	
	void delete(Long id);

	Agenda findByTheDateAndTheTime(Agenda agenda);
}
