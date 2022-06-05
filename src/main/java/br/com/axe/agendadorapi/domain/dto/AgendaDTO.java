package br.com.axe.agendadorapi.domain.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class AgendaDTO {
	private Long idAgenda;
	private LocalDate theDate;
	private LocalTime theTime;
	private String theClientName;
	private Boolean retorno;

}
