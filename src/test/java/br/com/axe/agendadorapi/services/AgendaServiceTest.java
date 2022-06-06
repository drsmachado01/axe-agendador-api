package br.com.axe.agendadorapi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import br.com.axe.agendadorapi.domain.model.Agenda;
import br.com.axe.agendadorapi.repositories.AgendaRepository;
import br.com.axe.agendadorapi.services.exceptions.DataIntegrityViolationException;
import br.com.axe.agendadorapi.services.exceptions.ObjectNotFoundException;
import br.com.axe.agendadorapi.services.impl.AgendaServiceImpl;

@SpringBootTest
@TestPropertySource(locations="classpath:application.properties")
class AgendaServiceTest {
    public static final LocalDate THE_DATE = LocalDate.of(2022, 06, 06);
    public static final LocalTime THE_TIME = LocalTime.of(18, 0);
    public static final boolean RETURNING = false;
    public static final String THE_CLIENT_NAME = "Astolpho Pamphilo";
    public static final long ID_AGENDA = 1L;
	private static final String JA_EXISTE_AGENDAMENTO_PARA_DATA_HORA = "Já existe agenda para a data e hora informados!";
	private static final String AGENDA_NAO_LOCALIZADA = "Objeto não encontrado!";
	private static final String JA_AGENDADO_PARA_ESTA_DATA_HORA = " já agendado(a) para esta data e hora!";
    @InjectMocks
    private AgendaServiceImpl service;

    @Mock
    private AgendaRepository repo;

    private Agenda agenda;

    private Optional<Agenda> optAgenda;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        init();
    }

    @Test
    void testListWhenHasNoAgenda() {
        when(repo.findAll()).thenReturn(List.of());
        List<Agenda> response = service.list();

        assertNotNull(response);
        assertEquals(0, response.size());
    }

    @Test
    void testListWhenHasAgenda() {
        when(repo.findAll()).thenReturn(List.of(agenda));
        List<Agenda> response = service.list();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(Agenda.class, response.get(0).getClass());
        Agenda expectedAgenda = response.get(0);
        assertingAgendaData(expectedAgenda);
    }
    
    @Test
    void testGetById() {
    	when(repo.findById(anyLong())).thenReturn(optAgenda);
    	Agenda response = service.findById(ID_AGENDA);

        assertNotNull(response);
        assertingAgendaData(response);
    }
    
    @Test
    void testFindByTheDate() {
    	when(repo.findByTheDate(THE_DATE)).thenReturn(List.of(agenda));

    	Agenda agenda = Agenda.builder().theDate(THE_DATE).build();
    	List<Agenda> response = service.findByTheDate(agenda);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(Agenda.class, response.get(0).getClass());
        Agenda expectedAgenda = response.get(0);
        assertingAgendaData(expectedAgenda);
    }

    @Test
    void testFindByTheDateWhenHasNoAgenda() {
    	when(repo.findByTheDate(THE_DATE)).thenReturn(List.of());
    	Agenda agenda = Agenda.builder().theDate(THE_DATE).build();
    	List<Agenda> response = service.findByTheDate(agenda);

        assertNotNull(response);
        assertEquals(0, response.size());
    }

    @Test
    void testSave() {
    	when(repo.findByTheDateAndTheTime(any(), any())).thenReturn(Optional.empty());
        when(repo.save(any())).thenReturn(agenda);
        Agenda response = service.save(agenda);
        assertNotNull(response);
        assertEquals(agenda, response);
        assertingAgendaData(response);
    }

    @Test
    void testSaveWhenTheDateAndTheTimeAreAlreadyScheduled() {
    	when(repo.findByTheDateAndTheTime(any(), any())).thenReturn(optAgenda);
    	try {
    		agenda.setIdAgenda(null);
    		service.save(agenda);
    	} catch (Exception ex) {
    		assertDataIntegrityViolationException(ex);
    	}
    }
    
    @Test
    void testeUpdate() {
    	agenda.setTheTime(LocalTime.of(18, 30));
    	when(repo.findById(anyLong())).thenReturn(optAgenda);
    	when(repo.findByTheDateAndTheTime(any(), any())).thenReturn(optAgenda);
        when(repo.save(any())).thenReturn(agenda);
        Agenda response = service.save(agenda);
        assertNotNull(response);
        assertEquals(agenda, response);
        assertingAgendaData(response);
    }
    
    @Test
    void testeUpdateWhenAgendaIdDoesNotExist() {
    	when(repo.findById(anyLong())).thenThrow(new ObjectNotFoundException(AGENDA_NAO_LOCALIZADA));
    	
    	try {
    		service.update(ID_AGENDA, agenda);
    	} catch (Exception ex) {
            assertObjectNotFoundException(ex);
        }
    }

	@Test
    void testeUpdateWhenTheDateAndTheTimeAreAlreadyScheduled() {
		agenda.setTheClientName("João da Muriçoca");
		when(repo.findById(anyLong())).thenReturn(optAgenda);
    	when(repo.findByTheDateAndTheTime(any(), any())).thenReturn(optAgenda);
    	try {
    		service.update(ID_AGENDA, agenda);
    	} catch (Exception ex) {
    		assertDataIntegrityViolationException(ex);
    	}
    }

	@Test
    void testeUpdateWhenSameClienteScheduledToSpecificTheDateAndTheTime() {
		when(repo.findById(anyLong())).thenReturn(optAgenda);
    	when(repo.findByTheDateAndTheTime(any(), any())).thenReturn(optAgenda);
    	try {
    		service.update(ID_AGENDA, agenda);
    	} catch (Exception ex) {
    		assertDataIntegrityViolationExceptionForClientAlreadyScheduled(ex);
    	}
    }
	
	@Test
	void testDelete() {
		when(repo.findById(anyLong())).thenReturn(optAgenda);
		doNothing().when(repo).delete(any());
		service.delete(ID_AGENDA);
		Mockito.verify(repo, times(1)).delete(any());
	}
	
	@Test
	void deleteWhenAgendaNotFound() {
    	when(repo.findById(anyLong())).thenThrow(new ObjectNotFoundException(AGENDA_NAO_LOCALIZADA));
    	
    	try {
    		service.delete(ID_AGENDA);
    	} catch (Exception ex) {
            assertObjectNotFoundException(ex);
        }
	}

    private void assertObjectNotFoundException(Exception ex) {
        assertEquals(ObjectNotFoundException.class, ex.getClass());
        assertEquals(AGENDA_NAO_LOCALIZADA, ex.getMessage());
    }

    private void assertDataIntegrityViolationException(Exception ex) {
        assertEquals(DataIntegrityViolationException.class, ex.getClass());
        assertEquals(JA_EXISTE_AGENDAMENTO_PARA_DATA_HORA, ex.getMessage());
    }

    private void assertDataIntegrityViolationExceptionForClientAlreadyScheduled(Exception ex) {
        assertEquals(DataIntegrityViolationException.class, ex.getClass());
        assertEquals(agenda.getTheClientName() + JA_AGENDADO_PARA_ESTA_DATA_HORA, ex.getMessage());
    }

    private void assertingAgendaData(Agenda expectedAgenda) {
        assertEquals(agenda.getIdAgenda(), expectedAgenda.getIdAgenda());
        assertEquals(agenda.getTheDate(), expectedAgenda.getTheDate());
        assertEquals(agenda.getTheTime(), expectedAgenda.getTheTime());
        assertEquals(agenda.getRetorno(), expectedAgenda.getRetorno());
        assertEquals(agenda.getTheClientName(), expectedAgenda.getTheClientName());
    }
    private void init() {
        agenda = Agenda.builder().idAgenda(ID_AGENDA).theDate(THE_DATE)
                .theTime(THE_TIME)
                .retorno(RETURNING)
                .theClientName(THE_CLIENT_NAME).build();

        optAgenda = Optional.of(Agenda.builder().idAgenda(ID_AGENDA).theDate(THE_DATE)
                .theTime(THE_TIME)
                .retorno(RETURNING)
                .theClientName(THE_CLIENT_NAME).build());
    }
}