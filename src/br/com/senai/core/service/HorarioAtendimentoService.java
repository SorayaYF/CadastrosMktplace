package br.com.senai.core.service;

import java.time.LocalTime;
import java.util.List;

import br.com.senai.core.dao.DaoHorarioAtendimento;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.HorarioAtendimento;
import br.com.senai.core.domain.Restaurante;

public class HorarioAtendimentoService {

	private DaoHorarioAtendimento daoHorarioAtendimento;

	public HorarioAtendimentoService() {
		this.daoHorarioAtendimento = FactoryDao.getInstance().getDaoHorarioAtendimento();
	}

	public void salvar(HorarioAtendimento horarioAtendimento) {
		
		this.validar(horarioAtendimento);

		boolean isPersistido = horarioAtendimento.getId() > 0;

		this.validarConflitoHorario(horarioAtendimento, daoHorarioAtendimento.listarPor(horarioAtendimento.getRestaurante()));
		
		if (isPersistido) {
			this.daoHorarioAtendimento.alterar(horarioAtendimento);
		} else {
			this.daoHorarioAtendimento.inserir(horarioAtendimento);
		}
	}

	private void validar(HorarioAtendimento horarioAtendimento) {
		if (horarioAtendimento != null) {
			if (horarioAtendimento.getRestaurante() != null && horarioAtendimento.getRestaurante().getId() > 0) {

				String diaDaSemana = horarioAtendimento.getDiaDaSemana();
				LocalTime horarioDeAbertura = horarioAtendimento.getHorarioDeAbertura();
				LocalTime horarioDeFechamento = horarioAtendimento.getHorarioDeFechamento();

				if (diaDaSemana == null) {
					throw new IllegalArgumentException("O dia da semana é obrigatório");
				}

				if (horarioDeAbertura == null || horarioDeFechamento == null) {
					throw new IllegalArgumentException("Os horários de abertura e fechamento são obrigatórios");
				}
				
				if (horarioDeAbertura == null || horarioDeFechamento == null) {
					throw new IllegalArgumentException("Os horários de abertura e fechamento são obrigatórios");
				}
				

				if (horarioDeAbertura.isAfter(horarioDeFechamento)) {
					throw new IllegalArgumentException(
							"O horário de abertura deve ser anterior ao horário de fechamento");
				}
				
				if (horarioDeAbertura.equals(horarioDeFechamento)) {
					throw new IllegalArgumentException(
							"O horário de abertura é igual ao horário de fechamento");
				}

			} else {
				throw new NullPointerException("O restaurante do horário é obrigatória");
			}
		} else {
			throw new NullPointerException("O horário de atendimento não pode ser nulo");
		}
	}

	public void removerPor(int idDoHorario) {
		if (idDoHorario > 0) {
			this.daoHorarioAtendimento.excluirPor(idDoHorario);
		} else {
			throw new IllegalArgumentException("O id para remoção do horário deve ser maior que zero");
		}
	}
	
	public HorarioAtendimento buscarPor(int idDoHorarioAtendimento) {
		if (idDoHorarioAtendimento > 0) {
			HorarioAtendimento horarioAtendimentoEncontrado = daoHorarioAtendimento.buscarPor(idDoHorarioAtendimento);
			if (horarioAtendimentoEncontrado == null) {
				throw new IllegalArgumentException("Não foi encontrado restaurante para o código informado");
			}
			return horarioAtendimentoEncontrado;
		} else {
			throw new IllegalArgumentException("O id para busca do restaurante deve ser maior que zero");
		}
	}

	public List<HorarioAtendimento> listarPor(Restaurante restaurante) {
		if (restaurante == null || restaurante.getId() <= 0) {
			throw new IllegalArgumentException("O restaurante é obrigatório para listar os horários de atendimento");
		}
		return daoHorarioAtendimento.listarPor(restaurante);
	}
	
    private boolean horariosConflitantes(LocalTime abertura1, LocalTime fechamento1, LocalTime abertura2, LocalTime fechamento2) {
        return (abertura1.isBefore(fechamento2) && abertura2.isBefore(fechamento1)) ||
               abertura1.equals(abertura2) ||
               fechamento1.equals(fechamento2);
    }

    public void validarConflitoHorario(HorarioAtendimento horarioNovo, List<HorarioAtendimento> horariosSalvos) {
        for (HorarioAtendimento horarioSalvo : horariosSalvos) {
            if (horarioSalvo.getDiaDaSemana().equals(horarioNovo.getDiaDaSemana())) {
                if (horariosConflitantes(
                        horarioNovo.getHorarioDeAbertura(), horarioNovo.getHorarioDeFechamento(),
                        horarioSalvo.getHorarioDeAbertura(), horarioSalvo.getHorarioDeFechamento())) {
                    throw new IllegalArgumentException("ERRO: O novo horário conflita com um horário já cadastrado");
                }
            }
        }
    }

}