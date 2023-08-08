package br.com.senai.core.service;

import java.util.List;

import br.com.senai.core.dao.DaoHorarioAtendimento;
import br.com.senai.core.dao.DaoRestaurante;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Restaurante;

public class RestauranteService {

	private DaoRestaurante dao;

	private DaoHorarioAtendimento daoHorario;

	public RestauranteService() {
		this.dao = FactoryDao.getInstance().getDaoRestaurante();
		this.daoHorario = FactoryDao.getInstance().getDaoHorarioAtendimento();
	}

	public void salvar(Restaurante restaurante) {
		this.validar(restaurante);
		boolean isJaInserido = restaurante.getId() > 0;
		if (isJaInserido) {
			this.dao.alterar(restaurante);
		} else {
			this.dao.inserir(restaurante);
		}
	}

	private void validar(Restaurante restaurante) {
		if (restaurante != null) {

			if (restaurante.getEndereco() != null) {

				if (restaurante.getCategoria() != null && restaurante.getCategoria().getId() > 0) {

					boolean isNomeInvalido = restaurante.getNome() == null || restaurante.getNome().isBlank()
							|| restaurante.getNome().length() > 250;

					if (isNomeInvalido) {
						throw new IllegalArgumentException(
								"O nome é obrigatório e deve conter menos de 250 caracteres");
					}

					boolean isDescricaoInvalida = restaurante.getDescricao() == null
							|| restaurante.getDescricao().isBlank();

					if (isDescricaoInvalida) {
						throw new IllegalArgumentException("A descrição é obrigatória");
					}

					boolean isLogradouroInvalido = restaurante.getEndereco().getLogradouro() == null
							|| restaurante.getEndereco().getLogradouro().isBlank()
							|| restaurante.getEndereco().getLogradouro().length() > 200;

					if (isLogradouroInvalido) {
						throw new IllegalArgumentException("O logradouro do endereço é obrigatória e não "
								+ "deve possuir mais de 200 caracteres");
					}

					boolean isCidadeInvalida = restaurante.getEndereco().getCidade() == null
							|| restaurante.getEndereco().getCidade().isBlank()
							|| restaurante.getEndereco().getCidade().length() > 80;

					if (isCidadeInvalida) {
						throw new IllegalArgumentException(
								"A cidade do endereço é obrigatória e não " + "deve possuir mais de 80 caracteres");
					}

					String nomeDoBairro = restaurante.getEndereco() != null ? restaurante.getEndereco().getBairro()
							: "";

					boolean isBairroInvalido = nomeDoBairro.isBlank() || nomeDoBairro.length() > 50;

					if (isBairroInvalido) {
						throw new IllegalArgumentException(
								"O bairro do endereço é obrigatório e não " + "deve possuir mais de 50 caracteres");
					}

				} else {
					throw new IllegalArgumentException("A categoria do restaurante é obrigatória");
				}

			} else {
				throw new NullPointerException("O endereço do restaurante não pode ser nulo");
			}

		} else {
			throw new NullPointerException("O restaurante não pode ser nulo");
		}
	}

	public List<Restaurante> listarPor(String nome, Categoria categoria) {

		boolean isCategoriaInformada = categoria != null && categoria.getId() > 0;

		boolean isNomeInformado = nome != null && !nome.isBlank();

		if (!isCategoriaInformada && !isNomeInformado) {
			throw new IllegalArgumentException("Informe o nome e/ou categoria para listagem");
		}

		String filtroNome = "";

		if (isCategoriaInformada) {
			filtroNome = nome + "%";
		} else {
			filtroNome = nome + "%";
		}

		return dao.listarPor(filtroNome, categoria);

	}

	public List<Restaurante> listarTodos() {
		return dao.listarPor("%%", null);
	}

	public void excluirPor(int idDoRestaurante) {
		if (idDoRestaurante > 0) {
			
			int qtdeDeHorarios = daoHorario.contarPor(idDoRestaurante);
			if (qtdeDeHorarios > 0) {
				throw new IllegalArgumentException("Não foi possivel excluir o restaurante. "
						+ "Motivo: Existem " + qtdeDeHorarios + " horários vinculados ao restaurante");
			}
			
			this.dao.excluirPor(idDoRestaurante);
			
		} else {
			throw new IllegalArgumentException("O id para exclusão deve ser maior que zero");
		}
	}

	public Restaurante buscarPor(int idDoRestaurante) {
		if (idDoRestaurante > 0) {
			Restaurante restauranteEncontrado = this.dao.buscarPor(idDoRestaurante);

			if (restauranteEncontrado == null) {
				throw new IllegalArgumentException("Não existe restaurante vinculado ao id " + "informado");
			}

			return restauranteEncontrado;

		} else {
			throw new IllegalArgumentException("O id para busca não pode ser menor que zero");
		}
	}

}
