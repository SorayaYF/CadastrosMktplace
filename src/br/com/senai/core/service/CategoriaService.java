package br.com.senai.core.service;

import java.util.List;

import br.com.senai.core.dao.DaoCategoria;
import br.com.senai.core.dao.DaoRestaurante;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.Categoria;

public class CategoriaService {
	
	private DaoCategoria dao;
	
	private DaoRestaurante daoRestaurante;
	
	public CategoriaService() {
		this.dao = FactoryDao.getInstance().getDaoCategoria();
		this.daoRestaurante = FactoryDao.getInstance().getDaoRestaurante();
	}
	
	public void salvar(Categoria categoria) {
		this.validar(categoria);
		boolean isJaInserido = categoria.getId() > 0;
		if (isJaInserido) {
			this.dao.alterar(categoria);
		}else {
			this.dao.inserir(categoria);
		}
	}
	
	public void removerPor(int id) {
		if (id > 0) {
			int qtdeDeRestaurantes = daoRestaurante.contarPor(id);
			if (qtdeDeRestaurantes > 0) {
				throw new IllegalArgumentException("Não foi possível excluir a categoria."
						+ "Motivo: Existe(m) " + qtdeDeRestaurantes + " restaurantes vinculados à categoria");
			}
			this.dao.excluirPor(id);
		}else {
			throw new IllegalArgumentException("O id da categoria deve ser maior que zero");
		}
	}
	
	public Categoria buscarPor(int id) {
		if (id > 0) {
			Categoria categoriaEncontrada = this.dao.buscarPor(id);
			if (categoriaEncontrada == null) {
				throw new IllegalArgumentException("Não foi encontrada "
						+ "categoria para o código informado");
			}
			return categoriaEncontrada;
		}else {
			throw new IllegalArgumentException("O id da categoria deve ser maior que zero");
		}
	}
	
	public List<Categoria> listarPor(String nome){
		if (nome != null && nome.length() >= 3) {
			return this.dao.listarPor("%" + nome + "%");
		}
		throw new IllegalArgumentException("O filtro é obrigatório e deve conter mais de 2 caracteres");
	}
	
	public List<Categoria> listarTodas(){
		return dao.listarPor("%%");
	}
	
	private void validar(Categoria categoria) {
		if (categoria != null) {
			
			boolean isNomeInvalido = categoria.getNome() == null 
					|| categoria.getNome().isBlank()
					|| categoria.getNome().length() > 100
					|| categoria.getNome().length() < 3;
					
			if (isNomeInvalido) {
				throw new IllegalArgumentException("O nome da categoria é obrigatório e "
						+ "deve possuir entre 3 e 100 caracteres");
			}
			
		}else {
			throw new NullPointerException("A categoria não pode ser nula");
		}
	}

}
