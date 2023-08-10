package br.com.senai.view.restaurante;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.CategoriaService;
import br.com.senai.core.service.RestauranteService;
import br.com.senai.view.componentes.table.RestauranteTableModel;

public class ViewConsultaRestaurante extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private JTextField edtNome;
	
	private JComboBox<Categoria> cbCategoria;
	
	private JScrollPane spTable;
	
	private JTable tableRestaurantes;
	
	private RestauranteService restauranteService;
	private CategoriaService categoriaService;
	
	public void carregarComboCategoria() {
		cbCategoria.addItem(null);
		List<Categoria> categorias = categoriaService.listarTodas();
		for (Categoria ca : categorias) {
			cbCategoria.addItem(ca);
		}
	}

	public ViewConsultaRestaurante() {
		setResizable(false);
		setName("frmConsultaCategoria");
		setTitle("Gerenciar Restaurante - Listagem");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 664, 402);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		this.restauranteService = new RestauranteService();
		
		JLabel lblFiltro = new JLabel("Filtros");
		lblFiltro.setFont(new Font("Tahoma", Font.BOLD, 11));		
		lblFiltro.setBounds(10, 39, 46, 14);
		contentPane.add(lblFiltro);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNome.setBounds(20, 64, 46, 14);
		contentPane.add(lblNome);
		
		edtNome = new JTextField();
		edtNome.setBounds(76, 61, 172, 20);
		contentPane.add(edtNome);
		edtNome.setColumns(10);
		
		JButton btnListar = new JButton("Listar");		
		btnListar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String nomeInformado = edtNome.getText().toUpperCase();
					Categoria categoriaInformada = (Categoria) cbCategoria.getSelectedItem();
					List<Restaurante> restaurantesEncontrados = restauranteService.listarPor(nomeInformado, categoriaInformada);
					if (restaurantesEncontrados.isEmpty()) {
						JOptionPane.showMessageDialog(contentPane,
								"Não foi encontrado nenhum restaurante");
					} else {
						RestauranteTableModel model = new RestauranteTableModel(restaurantesEncontrados);
						tableRestaurantes.setModel(model);
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
				}
			}
		});
		btnListar.setBounds(549, 60, 89, 23);
		contentPane.add(btnListar);
		
		JButton btnNovo = new JButton("Novo");	
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewCadastroRestaurante view = new ViewCadastroRestaurante();
				view.setVisible(true);
				dispose();
			}
		});
		btnNovo.setBounds(550, 6, 89, 23);
		contentPane.add(btnNovo);
		
		JLabel lblRestaurantesEncontrados = new JLabel("Restaurantes Encontrados");
		lblRestaurantesEncontrados.setName("");
		lblRestaurantesEncontrados.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRestaurantesEncontrados.setBounds(9, 107, 319, 14);
		contentPane.add(lblRestaurantesEncontrados);
				
		tableRestaurantes = new JTable(new RestauranteTableModel(new ArrayList<Restaurante>()));
		this.configurarTabela();
		spTable = new JScrollPane(tableRestaurantes);
		spTable.setBounds(10, 129, 628, 148);
		contentPane.add(spTable);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "A\u00E7\u00F5es", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(428, 289, 210, 62);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton btnEditar = new JButton("Editar");		
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linhaSelecionada = tableRestaurantes.getSelectedRow();
				if (linhaSelecionada >= 0) {
					RestauranteTableModel model = (RestauranteTableModel) tableRestaurantes.getModel();
					Restaurante restauranteSelecionado = model.getPor(linhaSelecionada);
					ViewCadastroRestaurante view = new ViewCadastroRestaurante();
					view.setRestaurante(restauranteSelecionado);
					view.setVisible(true);
					dispose();

				} else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para edição.");
				}
			}
		});
		btnEditar.setBounds(12, 27, 89, 23);
		panel.add(btnEditar);
		
		JButton btnExcluir = new JButton("Excluir");		
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linhaSelecionada = tableRestaurantes.getSelectedRow();
				if (linhaSelecionada >= 0) {
					int opcao = JOptionPane.showConfirmDialog(contentPane, "Deseja realmente remover!?", "Remoção",
							JOptionPane.YES_NO_OPTION);
					if (opcao == 0) {
						
						RestauranteTableModel model = (RestauranteTableModel) tableRestaurantes.getModel();
						
						Restaurante restauranteSelecionado = model.getPor(linhaSelecionada);
						try {
							restauranteService.excluirPor(restauranteSelecionado.getId());
							List<Restaurante> restaurantesRestantes = restauranteService.listarPor(edtNome.getText().toUpperCase(), (Categoria) cbCategoria.getSelectedItem());
							model = new RestauranteTableModel(restaurantesRestantes);
							tableRestaurantes.setModel(model);								
							JOptionPane.showMessageDialog(contentPane, "Restaurante removido com sucesso!");
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(contentPane, ex.getMessage());
						}
						tableRestaurantes.clearSelection();
					}
				} else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para remoção.");
				}
			}
		});
		btnExcluir.setBounds(113, 27, 89, 23);
		panel.add(btnExcluir);
		
		cbCategoria = new JComboBox<Categoria>();
		cbCategoria.setBounds(339, 58, 198, 25);
		contentPane.add(cbCategoria);
		
		JLabel lblCategoria = new JLabel("Categoria");
		lblCategoria.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCategoria.setBounds(266, 64, 69, 14);
		contentPane.add(lblCategoria);
		this.categoriaService = new CategoriaService();
		this.carregarComboCategoria();
			
	}
	
	private void configurarColuna(int indice, int largura) {
		this.tableRestaurantes.getColumnModel().getColumn(indice).setResizable(false);
		this.tableRestaurantes.getColumnModel().getColumn(indice).setPreferredWidth(largura);
	}
	
	private void configurarTabela() {
		final int COLUNA_ID = 0;
		final int COLUNA_NOME = 1;
		this.tableRestaurantes.getTableHeader().setReorderingAllowed(false);
		this.tableRestaurantes.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.configurarColuna(COLUNA_ID, 50);
		this.configurarColuna(COLUNA_NOME, 550);
	}
	
}	