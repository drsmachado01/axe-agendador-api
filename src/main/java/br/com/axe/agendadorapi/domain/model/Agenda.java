package br.com.axe.agendadorapi.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_AGENDA", uniqueConstraints = {@UniqueConstraint(name="THE_DATE_AND_TIME_CONSTRAINT", columnNames= {"THE_DATE", "THE_TIME"})})
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Agenda {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_AGENDA_SEQ")
	@SequenceGenerator(name = "ID_AGENDA_SEQ", sequenceName = "ID_AGENDA_SEQ")
	@Column(name = "ID_AGENDA")
	private Long idAgenda;
	@Column(name = "THE_DATE")
	private LocalDate theDate;
	@Column(name = "THE_TIME")
	private LocalTime theTime;
	@Column(name = "THE_CLIENT_NAME")
	private String theClientName;
	private Boolean retorno;
}
