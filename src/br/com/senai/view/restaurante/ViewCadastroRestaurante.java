package br.com.senai.view.restaurante;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Endereco;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.CategoriaService;
import br.com.senai.core.service.RestauranteService;

public class ViewCadastroRestaurante extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField edtNome;
	private JTextField edtLogradouro;
	private JTextField edtCidade;
	private JTextField edtBairro;
	private JTextField edtComplemento;
	private JComboBox<Categoria> cbCategoria;	
	private JTextArea taDescricao;
	private RestauranteService restauranteService;
	private CategoriaService categoriaService;
	private Restaurante restaurante;
	
	public void carregarComboCategoria() {
		List<Categoria> categorias = categoriaService.listarTodas();
		for (Categoria ca : categorias) {
			cbCategoria.addItem(ca);
		}
	}

	public ViewCadastroRestaurante() {
		setResizable(false);
		setTitle("Gerenciar Restaurante - Cadastro");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 758, 376);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		this.restauranteService = new RestauranteService();
		
		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewConsultaRestaurante view = new ViewConsultaRestaurante();
				view.setVisible(true);
				dispose();
			}
		});
		btnPesquisar.setBounds(620, 11, 105, 23);
		contentPane.add(btnPesquisar);
		
		edtNome = new JTextField();
		edtNome.setColumns(10);
		edtNome.setBounds(109, 56, 275, 20);
		contentPane.add(edtNome);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNome.setBounds(10, 59, 46, 14);
		contentPane.add(lblNome);
		
		JLabel lblCategoria = new JLabel("Categoria");
		lblCategoria.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCategoria.setBounds(394, 58, 69, 14);
		contentPane.add(lblCategoria);
		
		cbCategoria = new JComboBox<Categoria>();
		cbCategoria.setBounds(473, 54, 252, 25);
		contentPane.add(cbCategoria);
		
		JLabel lblDescricao = new JLabel("Descrição");
		lblDescricao.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDescricao.setBounds(10, 96, 55, 14);
		contentPane.add(lblDescricao);
		
		taDescricao = new JTextArea();
		taDescricao.setBounds(109, 94, 616, 104);
		contentPane.add(taDescricao);
		
		edtLogradouro = new JTextField();
		edtLogradouro.setColumns(10);
		edtLogradouro.setBounds(109, 209, 616, 20);
		contentPane.add(edtLogradouro);
		
		JLabel lblLogradouro = new JLabel("Logradouro");
		lblLogradouro.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblLogradouro.setBounds(10, 212, 66, 14);
		contentPane.add(lblLogradouro);
		
		edtCidade = new JTextField();
		edtCidade.setColumns(10);
		edtCidade.setBounds(109, 240, 307, 20);
		contentPane.add(edtCidade);
		
		JLabel lblCidade = new JLabel("Cidade");
		lblCidade.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCidade.setBounds(10, 243, 66, 14);
		contentPane.add(lblCidade);
		
		JLabel lblBairro = new JLabel("Bairro");
		lblBairro.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblBairro.setBounds(426, 243, 46, 14);
		contentPane.add(lblBairro);
		
		edtBairro = new JTextField();
		edtBairro.setColumns(10);
		edtBairro.setBounds(473, 240, 252, 20);
		contentPane.add(edtBairro);
		
		edtComplemento = new JTextField();
		edtComplemento.setColumns(10);
		edtComplemento.setBounds(109, 271, 616, 20);
		contentPane.add(edtComplemento);
		
		JLabel lblComplemento = new JLabel("Complemento");
		lblComplemento.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblComplemento.setBounds(10, 274, 89, 14);
		contentPane.add(lblComplemento);
		
		JButton btnSalvar = new JButton("Salvar");		
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String nome = edtNome.getText();
					String bairro = edtBairro.getText();
					String logradouro = edtLogradouro.getText();
					String cidade = edtCidade.getText();
					String complemento = edtComplemento.getText();
					String detalhamento = taDescricao.getText();
					Categoria categoria = (Categoria) cbCategoria.getSelectedItem();

					if (nome.isEmpty() || bairro.isEmpty() || logradouro.isEmpty() || cidade.isEmpty() || detalhamento.isEmpty()) {
						JOptionPane.showMessageDialog(contentPane, "Todos os campos são obrigatórios!");
					} else {
						if (restaurante == null) {
			                restaurante = new Restaurante(nome, detalhamento, new Endereco(cidade, logradouro, bairro, complemento), categoria);
							restauranteService.salvar(restaurante);
							JOptionPane.showMessageDialog(contentPane, "Restaurante inserido com sucesso!");
							clearFields();
							restaurante = null;

						} else {
							restaurante.setNome(nome);
							restaurante.setDescricao(detalhamento);
							restaurante.setEndereco(new Endereco(cidade, logradouro, bairro, complemento));
							restaurante.setCategoria(categoria);
							restauranteService.salvar(restaurante); 
							JOptionPane.showMessageDialog(contentPane, "Restaurante alterado com sucesso!");
							clearFields();
							restaurante = null;
						}
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
					if (restaurante.getId() <= 0) {
						restaurante = null;
					}
				}
			}
		});
		btnSalvar.setBounds(508, 302, 105, 23);
		contentPane.add(btnSalvar);
		
		JButton btnCancelar = new JButton("Cancelar");		
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearFields();
			}
		});
		btnCancelar.setBounds(620, 302, 105, 23);
		contentPane.add(btnCancelar);
		
		this.categoriaService = new CategoriaService();
		this.carregarComboCategoria();
	}
	
	public void clearFields() {
		edtNome.setText("");
		edtBairro.setText("");
		edtLogradouro.setText("");
		edtCidade.setText("");
		edtComplemento.setText("");
		taDescricao.setText("");
		cbCategoria.setSelectedIndex(0);
		restaurante = null;
	}
	
	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
		this.edtNome.setText(restaurante.getNome());
		this.taDescricao.setText(restaurante.getDescricao());
		this.edtBairro.setText(restaurante.getEndereco().getBairro());
		this.edtLogradouro.setText(restaurante.getEndereco().getLogradouro());
		this.edtCidade.setText(restaurante.getEndereco().getCidade());
		this.edtComplemento.setText(restaurante.getEndereco().getComplemento());
		this.cbCategoria.setSelectedItem(restaurante.getCategoria());
	}
	
}