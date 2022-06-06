package br.com.axe.agendadorapi.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import br.com.axe.agendadorapi.domain.dto.AgendaDTO;
import br.com.axe.agendadorapi.domain.model.Agenda;
import br.com.axe.agendadorapi.services.AgendaService;

@SpringBootTest
@TestPropertySource(locations="classpath:application.properties")
public class AgendaControllerTest {
	
    private  static final LocalDate THE_DATE = LocalDate.of(2022, 06, 06);
    private  static final LocalTime THE_TIME = LocalTime.of(18, 0);
    private  static final boolean RETURNING = false;
    private  static final String THE_CLIENT_NAME = "Astolpho Pamphilo";
    private  static final long ID_AGENDA = 1L;
	private static final String JA_EXISTE_AGENDAMENTO_PARA_DATA_HORA = "Já existe agenda para a data e hora informados!";
	private static final String AGENDA_NAO_LOCALIZADA = "Objeto não encontrado!";
	private static final String JA_AGENDADO_PARA_ESTA_DATA_HORA = " já agendado(a) para esta data e hora!";

	@InjectMocks
	private AgendaController controller;
	
	@Mock
	private ModelMapper mapper;
	
	@Mock
	private AgendaService service;
	
	private Agenda agenda = new Agenda();
	
	private AgendaDTO agendaDTO = new AgendaDTO();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        init();
    }
    
    @Test
    void listAllAgendaWithSomeAgenda() {
    	when(service.list()).thenReturn(List.of(agenda));
    	when(mapper.map(any(), any())).thenReturn(agendaDTO);
    	ResponseEntity<List<AgendaDTO>> response = controller.list();
    	
    	assertNotNull(response);
    	assertNotNull(response.getBody());
    	List<AgendaDTO> responseList = response.getBody();
    	assertEquals(1, responseList.size());
    	assertNotNull(responseList.get(0));
    	AgendaDTO responseAgenda = responseList.get(0);
    	assertingAgendaDataDTO(responseAgenda);
    }
    
    @Test
    void listAllAgendaWithNoAgenda() {
    	when(service.list()).thenReturn(List.of());
    	when(mapper.map(any(), any())).thenReturn(agendaDTO);
    	ResponseEntity<List<AgendaDTO>> response = controller.list();
    	
    	assertNotNull(response);
    	assertNotNull(response.getBody());
    	List<AgendaDTO> responseList = response.getBody();
    	assertEquals(0, responseList.size());
    }
    
    @Test
    void findById() {
    	when(service.findById(ID_AGENDA)).thenReturn(agenda);
    	when(mapper.map(any(), any())).thenReturn(agendaDTO);
    	
    	ResponseEntity<AgendaDTO> response = controller.findById(ID_AGENDA);

    	assertNotNull(response);
    	assertNotNull(response.getBody());
    	AgendaDTO agendaResponse = response.getBody();
    	assertNotNull(agendaResponse);
    	assertingAgendaDataDTO(agendaResponse);
    }
    
    @Test
    void testeFindByDateAndTime() {
    	when(service.findByTheDateAndTheTime(agenda)).thenReturn(agenda);
    	when(mapper.map(any(), any())).thenReturn(agendaDTO);
    	
    	ResponseEntity<AgendaDTO> response = controller.findByDateAndTime(THE_DATE, THE_TIME);
    	
    	assertNotNull(response);
    	assertNotNull(response.getBody());
    	AgendaDTO agendaResponse = response.getBody();
    	assertNotNull(agendaResponse);
    	assertingAgendaDataDTO(agendaResponse);
    }
    
    @Test
    void findByTheDate() {
    	when(service.findByTheDate(any())).thenReturn(List.of(agenda));
    	when(mapper.map(any(), any())).thenReturn(agendaDTO);
    	
    	ResponseEntity<List<AgendaDTO>> response = controller.listAgendaOfSpecificDate(THE_DATE);
    	
    	assertNotNull(response);
    	assertNotNull(response.getBody());
    	List<AgendaDTO> listResponse = response.getBody();
    	assertNotNull(listResponse);
    	assertEquals(1, listResponse.size());
    	assertNotNull(listResponse.get(0));
    	AgendaDTO agendaResponse = listResponse.get(0);
    	assertingAgendaDataDTO(agendaResponse);    	
    }
    
    @Test
    void save() {
    	when(service.save(any())).thenReturn(agenda);
    	when(mapper.map(agendaDTO, Agenda.class)).thenReturn(agenda);
    	when(mapper.map(agenda, AgendaDTO.class)).thenReturn(agendaDTO);
    	
    	ResponseEntity<AgendaDTO> response = controller.save(agendaDTO);

        assertNotNull(response);
        assertNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());    	
    }
    
    @Test
    void update() {
    	when(service.update(anyLong(), any())).thenReturn(agenda);
    	when(mapper.map(agendaDTO, Agenda.class)).thenReturn(agenda);
    	when(mapper.map(agenda, AgendaDTO.class)).thenReturn(agendaDTO);
    	
    	ResponseEntity<AgendaDTO> response = controller.update(ID_AGENDA, agendaDTO);

        assertNotNull(response);
        assertNotNull(response.getBody());
    	AgendaDTO agendaResponse = response.getBody();
    	assertNotNull(agendaResponse);
    	assertingAgendaDataDTO(agendaResponse);
    }
    
    @Test
    void delete() {
    	doNothing().when(service).delete(anyLong());

    	ResponseEntity<AgendaDTO> response = controller.delete(ID_AGENDA);

        assertNotNull(response);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).delete(anyLong());
    }

	private void init() {
		agenda = Agenda.builder().idAgenda(ID_AGENDA).theDate(THE_DATE)
                .theTime(THE_TIME)
                .retorno(RETURNING)
                .theClientName(THE_CLIENT_NAME).build();
		
		agendaDTO = AgendaDTO.builder().idAgenda(ID_AGENDA).theDate(THE_DATE)
                .theTime(THE_TIME)
                .retorno(RETURNING)
                .theClientName(THE_CLIENT_NAME).build();
	}

    private void assertingAgendaDataDTO(AgendaDTO expectedAgenda) {
        assertEquals(agendaDTO.getIdAgenda(), expectedAgenda.getIdAgenda());
        assertEquals(agendaDTO.getTheDate(), expectedAgenda.getTheDate());
        assertEquals(agendaDTO.getTheTime(), expectedAgenda.getTheTime());
        assertEquals(agendaDTO.getRetorno(), expectedAgenda.getRetorno());
        assertEquals(agendaDTO.getTheClientName(), expectedAgenda.getTheClientName());
    }

}
