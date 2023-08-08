package br.com.senai.view.categoria;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
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
import br.com.senai.core.service.CategoriaService;
import br.com.senai.view.componentes.table.CategoriaTableModel;

public class ViewConsultaCategoria extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	
	private JTextField edtNome;
	
	private JScrollPane spTable;
	
	private JTable tableCategorias;	
	
	private CategoriaService service;

	public ViewConsultaCategoria() {		
		setResizable(false);
		setName("frmConsultaCategoria");
		setTitle("Gerenciar Categoria - Listagem");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 664, 402);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		this.service = new CategoriaService();
		
		JLabel lblFiltro = new JLabel("Filtros");
		lblFiltro.setFont(new Font("Tahoma", Font.BOLD, 11));		
		lblFiltro.setBounds(10, 39, 46, 14);
		contentPane.add(lblFiltro);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNome.setBounds(20, 64, 46, 14);
		contentPane.add(lblNome);
		
		edtNome = new JTextField();
		edtNome.setBounds(76, 61, 463, 20);
		contentPane.add(edtNome);
		edtNome.setColumns(10);
		
		JButton btnListar = new JButton("Listar");		
		btnListar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					List<Categoria> categorias = service.listarPor(edtNome.getText());
					CategoriaTableModel model = new CategoriaTableModel(categorias);
					tableCategorias.setModel(model);
					configurarTabela();
				}catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
				}
			}
		});
		btnListar.setBounds(549, 60, 89, 23);
		contentPane.add(btnListar);
		
		JButton btnNovo = new JButton("Novo");		
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewCadastroCategoria view = new ViewCadastroCategoria();
				view.setVisible(true);
				dispose();
			}
		});
		btnNovo.setBounds(550, 6, 89, 23);
		contentPane.add(btnNovo);
		
		JLabel lblCategoriasEncontradas = new JLabel("Categorias Encontradas");
		lblCategoriasEncontradas.setName("");
		lblCategoriasEncontradas.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCategoriasEncontradas.setBounds(9, 107, 319, 14);
		contentPane.add(lblCategoriasEncontradas);
		
		tableCategorias = new JTable(new CategoriaTableModel(new ArrayList<Categoria>()));
		this.configurarTabela();
		spTable = new JScrollPane(tableCategorias);
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
				try {
					int linhaSelecionada = tableCategorias.getSelectedRow();
					if (linhaSelecionada >= 0) {
						CategoriaTableModel model = (CategoriaTableModel)tableCategorias.getModel();
						Categoria categoriaSelecionada = model.getPor(linhaSelecionada);
						ViewCadastroCategoria view = new ViewCadastroCategoria();
						view.setCategoria(categoriaSelecionada);
						view.setVisible(true);
						dispose();						
					}else {
						JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para edição");
					}
				}catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
				}
			}
		});
		btnEditar.setBounds(12, 27, 89, 23);
		panel.add(btnEditar);
		
		JButton btnExcluir = new JButton("Excluir");		
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int linhaSelecionada = tableCategorias.getSelectedRow();
				
				if (linhaSelecionada >= 0) {
					
					int opcao = JOptionPane.showConfirmDialog(contentPane, "Deseja realmente excluir", 
							"Exclusão", JOptionPane.YES_NO_OPTION);
					
					if (opcao == 0) {
						
						CategoriaTableModel model = (CategoriaTableModel)tableCategorias.getModel();
						
						Categoria categoriaSelecionada = model.getPor(linhaSelecionada);
						
						try {
							service.removerPor(categoriaSelecionada.getId());
							List<Categoria> categoriasRestantes = service.listarPor(edtNome.getText());
							model = new CategoriaTableModel(categoriasRestantes);
							tableCategorias.setModel(model);								
							JOptionPane.showMessageDialog(contentPane, "Categoria removida com sucesso");
						}catch (Exception ex) {
							JOptionPane.showMessageDialog(contentPane, ex.getMessage());
						}
						
						tableCategorias.clearSelection();
						
					}
					
				}else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para exclusão");
				}

			}
		});
		btnExcluir.setBounds(113, 27, 89, 23);
		panel.add(btnExcluir);
	}	
	
	private void configurarColuna(int indice, int largura) {
		this.tableCategorias.getColumnModel().getColumn(indice).setResizable(true);
		this.tableCategorias.getColumnModel().getColumn(indice).setPreferredWidth(largura);
	}
	
	private void configurarTabela() {
		final int COLUNA_ID = 0;
		final int COLUNA_NOME = 1;
		this.tableCategorias.getTableHeader().setReorderingAllowed(false);
		this.tableCategorias.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.configurarColuna(COLUNA_ID, 50);
		this.configurarColuna(COLUNA_NOME, 550);
	}
	
}	
