package br.com.axe.agendadorapi.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.axe.agendadorapi.domain.model.Agenda;
import br.com.axe.agendadorapi.repositories.AgendaRepository;
import br.com.axe.agendadorapi.services.AgendaService;
import br.com.axe.agendadorapi.services.exceptions.DataIntegrityViolationException;
import br.com.axe.agendadorapi.services.exceptions.ObjectNotFoundException;

@Service
public class AgendaServiceImpl implements AgendaService {
	@Autowired
	private AgendaRepository repo;

	@Override
	public List<Agenda> list() {
		return repo.findAll();
	}

	@Override
	public List<Agenda> findByTheDate(Agenda agenda) {
		return repo.findByTheDateOrderByTheTimeAsc(agenda.getTheDate());
	}

	@Override
	public Agenda findById(Long id) {
		Optional<Agenda> oAgenda = repo.findById(id);
		return oAgenda.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado!"));
	}

	@Override
	public Agenda save(Agenda agenda) {
		findByTheDateAndTheTime(agenda);
		return repo.save(agenda);
	}

	@Override
	public Agenda update(Long id, Agenda agenda) {
		findById(id);
		agenda.setIdAgenda(id);
		findByTheDateAndTheTime(agenda);
		return repo.save(agenda);
	}

	@Override
	public void delete(Long id) {
		repo.delete(findById(id));
	}

	@Override
	public Agenda findByTheDateAndTheTime(Agenda agenda) {
		Optional<Agenda> oAgenda = repo.findByTheDateAndTheTime(agenda.getTheDate(), agenda.getTheTime());
		if(oAgenda.isPresent() && !oAgenda.get().getIdAgenda().equals(agenda.getIdAgenda())) {
            throw new DataIntegrityViolationException("Já existe agenda para a data e hora informados!");
		} else if(oAgenda.isPresent() && !oAgenda.get().getIdAgenda().equals(agenda.getIdAgenda()) 
				&& oAgenda.get().getTheClientName().equals(agenda.getTheClientName())) {
			throw new DataIntegrityViolationException(agenda.getTheClientName() + " já agendado(a) para esta data e hora!");
		}
		return oAgenda.orElse(null);
	}
}
