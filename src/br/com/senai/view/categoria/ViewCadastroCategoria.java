package br.com.senai.view.categoria;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import br.com.senai.core.domain.Categoria;
import br.com.senai.core.service.CategoriaService;

public class ViewCadastroCategoria extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField edtNome;
	
	private CategoriaService service;
	
	private Categoria categoria;

	public ViewCadastroCategoria() {
		setTitle("Gerenciar Categorias - Cadastro");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 153);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		this.service = new CategoriaService();
		
		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewConsultaCategoria view = new ViewConsultaCategoria();
				view.setVisible(true);
				dispose();
			}
		});
		btnPesquisar.setBounds(318, 11, 106, 23);
		contentPane.add(btnPesquisar);
		
		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(10, 45, 55, 16);
		contentPane.add(lblNome);
		
		edtNome = new JTextField();
		edtNome.setBounds(75, 43, 349, 20);
		contentPane.add(edtNome);
		edtNome.setColumns(10);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearFields();
			}
		});
		btnCancelar.setBounds(318, 74, 106, 23);
		contentPane.add(btnCancelar);
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String nome = edtNome.getText();
					if (categoria == null) {
						categoria = new Categoria(nome);
					}else {
						categoria.setNome(nome);
					}
					service.salvar(categoria);
					JOptionPane.showMessageDialog(contentPane, "Categoria salva com sucesso");
					clearFields();
				}catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
				}
			}
		});
		btnSalvar.setBounds(202, 74, 106, 23);
		contentPane.add(btnSalvar);
	}
	
	public void clearFields() {
		edtNome.setText("");
		categoria = null;
	}
	
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
		this.edtNome.setText(categoria.getNome());
	}
}
