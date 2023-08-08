package br.com.senai.core.domain;

import java.time.LocalTime;
import java.util.Objects;

public class HorarioAtendimento {

	private int id;
	private String diaDaSemana;
	private LocalTime horarioDeAbertura;
	private LocalTime horarioDeFechamento;
	private Restaurante restaurante;

	public HorarioAtendimento(String diaDaSemana, LocalTime horarioDeAbertura, LocalTime horarioDeFechamento,
			Restaurante restaurante) {
		this.diaDaSemana = diaDaSemana;
		this.horarioDeAbertura = horarioDeAbertura;
		this.horarioDeFechamento = horarioDeFechamento;
		this.restaurante = restaurante;
	}

	public HorarioAtendimento(int id, String diaDaSemana, LocalTime horarioDeAbertura, LocalTime horarioDeFechamento,
			Restaurante restaurante) {
		this(diaDaSemana, horarioDeAbertura, horarioDeFechamento, restaurante);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDiaDaSemana() {
		return diaDaSemana;
	}

	public void setDiaDaSemana(String diaDaSemana) {
		this.diaDaSemana = diaDaSemana;
	}

	public LocalTime getHorarioDeAbertura() {
		return horarioDeAbertura;
	}

	public void setHorarioDeAbertura(LocalTime horarioDeAbertura) {
		this.horarioDeAbertura = horarioDeAbertura;
	}

	public LocalTime getHorarioDeFechamento() {
		return horarioDeFechamento;
	}

	public void setHorarioDeFechamento(LocalTime horarioDeFechamento) {
		this.horarioDeFechamento = horarioDeFechamento;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HorarioAtendimento other = (HorarioAtendimento) obj;
		return id == other.id;
	}
}
