package br.com.senai.view.componentes.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.senai.core.domain.HorarioAtendimento;

public class HorarioAtendimentoTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;

	private final int QTDE_COLUNAS = 3;

	private List<HorarioAtendimento> horarios;

	public HorarioAtendimentoTableModel(List<HorarioAtendimento> horarios) {
		this.horarios = horarios;
	}
	
	@Override
	public int getColumnCount() {
		return QTDE_COLUNAS;
	}

	public String getColumnName(int column) {
		if (column == 0) {
			return "Dia da Semana";
		} else if (column == 1) {
			return "Abertura";
		} else if (column == 2) {
			return "Fechamento";
		}
		throw new IllegalArgumentException("Indíce inválido");
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return horarios.get(rowIndex).getDiaDaSemana();
		} else if (columnIndex == 1) {
			return horarios.get(rowIndex).getHorarioDeAbertura();
		} else if (columnIndex == 2) {
			return horarios.get(rowIndex).getHorarioDeFechamento();
		}
		throw new IllegalArgumentException("Índice inválido");
	}

	@Override
	public int getRowCount() {
		return horarios.size();
	}

	public HorarioAtendimento getPor(int rowIndex) {
		return horarios.get(rowIndex);
	}

	public void removerPor(int rowIndex) {
		this.horarios.remove(rowIndex);
	}
}
