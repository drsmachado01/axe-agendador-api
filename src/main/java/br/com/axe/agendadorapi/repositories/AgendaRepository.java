package br.com.axe.agendadorapi.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.axe.agendadorapi.domain.model.Agenda;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
	public List<Agenda> findByTheDate(LocalDate theDate);
	
	public Optional<Agenda> findByTheDateAndTheTime(LocalDate theDate, LocalTime theTime);
}
