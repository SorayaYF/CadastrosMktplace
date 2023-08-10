package br.com.senai.view.horarioAtendimento;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import br.com.senai.core.domain.HorarioAtendimento;
import br.com.senai.core.domain.Restaurante;
import br.com.senai.core.service.HorarioAtendimentoService;
import br.com.senai.core.service.RestauranteService;
import br.com.senai.view.componentes.table.HorarioAtendimentoTableModel;

public class ViewCadastroHorarioAtendimento extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JFormattedTextField ftfAbertura;
	private JFormattedTextField ftfFechamento;
	private JLabel lblFechamento;
	private JTable tableHorarioAtendimento;
	private JComboBox<String> cbDiaDaSemana;
	private HorarioAtendimentoService horarioAtendimentoService;
	private RestauranteService restauranteService;
	private JComboBox<Restaurante> cbRestaurante;
	private HorarioAtendimento horarioAtendimento;
	
	String[] diasSemana = { null, "DOMINGO", "SEGUNDA", "TERCA", "QUARTA", "QUINTA", "SEXTA", "SABADO" };

	public void carregarComboSemana() {
		for (String dia : diasSemana) {
			cbDiaDaSemana.addItem(dia);
		}
	}

	public void carregarComboRestaurante() {
		List<Restaurante> restaurantes = restauranteService.listarTodos();
		for (Restaurante re : restaurantes) {
			cbRestaurante.addItem(re);
		}
	}

	public ViewCadastroHorarioAtendimento() {
		this.horarioAtendimentoService = new HorarioAtendimentoService();
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 682, 428);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblRestaurante = new JLabel("Restaurante");
		lblRestaurante.setBounds(10, 11, 78, 14);
		contentPane.add(lblRestaurante);

		cbRestaurante = new JComboBox<Restaurante>();
		cbRestaurante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    Restaurante restauranteSelecionado = (Restaurante) cbRestaurante.getSelectedItem();
			    if (restauranteSelecionado != null) {
			        List<HorarioAtendimento> horariosAtendimento = horarioAtendimentoService.listarPor(restauranteSelecionado);
			        HorarioAtendimentoTableModel model = new HorarioAtendimentoTableModel(horariosAtendimento);
			        tableHorarioAtendimento.setModel(model);
			        tableHorarioAtendimento.updateUI();
			        clearFieldsWithoutRestaurante();
			   }
			}
		});
		cbRestaurante.setBounds(98, 6, 558, 25);
		contentPane.add(cbRestaurante);

		JLabel lblDiaDaSemana = new JLabel("Dia da Semana");
		lblDiaDaSemana.setBounds(10, 45, 91, 16);
		contentPane.add(lblDiaDaSemana);

		cbDiaDaSemana = new JComboBox<String>();
		cbDiaDaSemana.setBounds(108, 42, 119, 25);
		contentPane.add(cbDiaDaSemana);

		JButton btnAdicionar = new JButton("Adicionar");
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String diaDaSemana = (String) cbDiaDaSemana.getSelectedItem();
					String aberturaStr = ftfAbertura.getText();
					String fechamentoStr = ftfFechamento.getText();
					Restaurante restaurante = (Restaurante) cbRestaurante.getSelectedItem();

					if (aberturaStr.isEmpty() || fechamentoStr.isEmpty()) {
						JOptionPane.showMessageDialog(contentPane, "Todos os campos são obrigatórios!");
					} else {
						LocalTime horarioDeAbertura = LocalTime.parse(aberturaStr);
						LocalTime horarioDeFechamento = LocalTime.parse(fechamentoStr);

						if (horarioAtendimento == null) {
							horarioAtendimento = new HorarioAtendimento(diaDaSemana, horarioDeAbertura,
									horarioDeFechamento, restaurante);
							horarioAtendimentoService.salvar(horarioAtendimento);
							JOptionPane.showMessageDialog(contentPane, "Horário de atendimento inserido com sucesso!");
							clearFields();
							horarioAtendimento = null;
						} else {
							horarioAtendimento.setDiaDaSemana(diaDaSemana);
							horarioAtendimento.setHorarioDeAbertura(horarioDeAbertura);
							horarioAtendimento.setHorarioDeFechamento(horarioDeFechamento);
							horarioAtendimento.setRestaurante(restaurante);
							horarioAtendimentoService.salvar(horarioAtendimento);
							JOptionPane.showMessageDialog(contentPane, "Horário de atendimento alterado com sucesso!");
							clearFields();
							horarioAtendimento = null;
						}

					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(contentPane, ex.getMessage());
					if (horarioAtendimento.getId() <= 0) {
						horarioAtendimento = null;
					}
				}

			}
		});
		btnAdicionar.setBounds(558, 42, 98, 26);
		contentPane.add(btnAdicionar);

		ftfFechamento = new JFormattedTextField();
		ftfFechamento.setBounds(470, 43, 78, 20);
		contentPane.add(ftfFechamento);

		JLabel lblAbertura = new JLabel("Abertura"); 
		lblAbertura.setBounds(237, 45, 55, 16);
		contentPane.add(lblAbertura);

		ftfAbertura = new JFormattedTextField();
		ftfAbertura.setBounds(302, 42, 71, 20);
		contentPane.add(ftfAbertura);

		lblFechamento = new JLabel("Fechamento");
		lblFechamento.setBounds(383, 45, 77, 16);
		contentPane.add(lblFechamento);

		JLabel lblHorarios = new JLabel("Horários");
		lblHorarios.setBounds(10, 72, 55, 16);
		contentPane.add(lblHorarios);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearFields();
			}
		});
		btnCancelar.setBounds(558, 351, 98, 26);
		contentPane.add(btnCancelar);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Ações", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(430, 131, 226, 120);
		contentPane.add(panel);
		panel.setLayout(null);

		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linhaSelecionada = tableHorarioAtendimento.getSelectedRow();
				HorarioAtendimentoTableModel model = (HorarioAtendimentoTableModel) tableHorarioAtendimento.getModel();
				if (linhaSelecionada >= 0) {
					HorarioAtendimento horarioAtendimentoSelecionado = model.getPor(linhaSelecionada);
					setHorarioAtendimento(horarioAtendimentoSelecionado);
				} else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para edição.");
				}
			}
		});
		btnEditar.setBounds(12, 30, 202, 26);
		panel.add(btnEditar);

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linhaSelecionada = tableHorarioAtendimento.getSelectedRow();
				if (linhaSelecionada >= 0) {
					int opcao = JOptionPane.showConfirmDialog(contentPane, "Deseja realmente remover?", "Remoção",
							JOptionPane.YES_NO_OPTION);
					if (opcao == 0) {
						HorarioAtendimentoTableModel model = (HorarioAtendimentoTableModel) tableHorarioAtendimento.getModel();
						HorarioAtendimento horarioAtendimentoSelecionado = model.getPor(linhaSelecionada);
						try {
							horarioAtendimentoService.removerPor(horarioAtendimentoSelecionado.getId());
							List<HorarioAtendimento> horariosAtendimentoRestantes = horarioAtendimentoService.listarPor((Restaurante) cbRestaurante.getSelectedItem());
							model = new HorarioAtendimentoTableModel(horariosAtendimentoRestantes);
							tableHorarioAtendimento.setModel(model);	
							JOptionPane.showMessageDialog(contentPane, "Horário de atendimento removido com sucesso!");
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(contentPane, ex.getMessage());
						}
						tableHorarioAtendimento.clearSelection();
					}
				} else {
					JOptionPane.showMessageDialog(contentPane, "Selecione uma linha para remoção.");
				}
			}
		});
		btnExcluir.setBounds(12, 68, 202, 26);
		panel.add(btnExcluir);

		JLabel lblLinha = new JLabel(
				"____________________________________________________________________________________________");
		lblLinha.setBounds(10, 78, 646, 16);
		contentPane.add(lblLinha);

		tableHorarioAtendimento = new JTable(new HorarioAtendimentoTableModel(new ArrayList<HorarioAtendimento>()));
		this.configurarTabela();
		JScrollPane scrollPane = new JScrollPane(tableHorarioAtendimento);
		scrollPane.setBounds(10, 114, 399, 203);
		contentPane.add(scrollPane);

		this.restauranteService = new RestauranteService();
		this.carregarComboRestaurante();
		this.carregarComboSemana();
	    mascara(ftfAbertura, "##:##");
	    mascara(ftfFechamento, "##:##");
	}
	
	private void mascara(JFormattedTextField field, String mask) {
	    try {
	        MaskFormatter maskFormatter = new MaskFormatter(mask);
	        maskFormatter.setPlaceholderCharacter('0');
	        maskFormatter.install(field);
	    } catch (ParseException ex) {
	        ex.printStackTrace();
	    }
	}
	
    private void clearFields() {
        cbDiaDaSemana.setSelectedItem(null);
        ftfAbertura.setText("");
        ftfFechamento.setText("");
        cbRestaurante.setSelectedIndex(0);
        horarioAtendimento = null;
    }
    
    private void clearFieldsWithoutRestaurante() {
        cbDiaDaSemana.setSelectedItem(null);
        ftfAbertura.setText("");
        ftfFechamento.setText("");
        horarioAtendimento = null;
    }
    
	private void setHorarioAtendimento(HorarioAtendimento horarioAtendimento) {
		this.horarioAtendimento = horarioAtendimento;
		cbRestaurante.setSelectedItem(horarioAtendimento.getRestaurante());
		ftfAbertura.setValue(horarioAtendimento.getHorarioDeAbertura());
		ftfFechamento.setValue(horarioAtendimento.getHorarioDeFechamento());
		cbDiaDaSemana.setSelectedItem(horarioAtendimento.getDiaDaSemana());
	}
	
	private void configurarColuna(int indice, int largura) {
		this.tableHorarioAtendimento.getColumnModel().getColumn(indice).setResizable(false);
		this.tableHorarioAtendimento.getColumnModel().getColumn(indice).setPreferredWidth(largura);
	}
	
	private void configurarTabela() {
		final int COLUNA_ID = 0;
		final int COLUNA_NOME = 1;
		this.tableHorarioAtendimento.getTableHeader().setReorderingAllowed(false);
		this.tableHorarioAtendimento.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.configurarColuna(COLUNA_ID, 50);
		this.configurarColuna(COLUNA_NOME, 550);
	}
}