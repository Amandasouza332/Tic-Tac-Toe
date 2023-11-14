package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TicTacToe {
    
    // Definindo as constantes para o jogo
    private static final String X = "X";
    private static final String O = "O";
    private static final Color X_COLOR = new Color(243, 74, 41);
    private static final Color O_COLOR = new Color(43, 67, 108);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 200);
    private static final Font TURN_FONT = new Font("Arial", Font.BOLD, 65);

    // Iniciando variáveis de instância
    private final JFrame frame = new JFrame();
    private final JLabel turn = new JLabel();
    private final JButton[][] buttons = new JButton[3][3];
    private boolean xTurn = true;
    private int scoreX = 0;
    private int scoreO = 0;
    private final JLabel scoreLabel = new JLabel();
    private final Timer timer = new Timer(450, null);

    public TicTacToe() {

        // Configurando a janela do jogo
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setTitle("Jogo da Velha");
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        // Configurando o label do turno
        turn.setBackground(X_COLOR);
        turn.setForeground(Color.WHITE);
        turn.setFont(TURN_FONT);
        turn.setHorizontalAlignment(JLabel.CENTER);
        turn.setText("TURNO DO " + (xTurn ? X : O));
        turn.setOpaque(true);

        // Configurando o painel do jogo
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBounds(0, 0, 800, 100);
        panel.add(turn);
        
        // Configurando o painel dos botões
        JPanel btPanel = new JPanel();
        btPanel.setLayout(new GridLayout(3, 3, 25, 25));
        btPanel.setBackground(new Color(91, 91, 91));
        
        // Criando os botões do jogo
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setFont(BUTTON_FONT);
                buttons[i][j].setFocusable(false);
                buttons[i][j].addActionListener(e -> {
                    JButton button = (JButton) e.getSource();
                    if (button.getText().isEmpty()) {
                        button.setText(xTurn ? X : O);
                        button.setForeground(xTurn ? X_COLOR : O_COLOR);
                        turn.setBackground(!xTurn ? X_COLOR : O_COLOR);
                        xTurn = !xTurn;
                        checkWinner();
                        turn.setText("TURNO DO " + (xTurn ? X : O));
                    }
                });
                btPanel.add(buttons[i][j]);
            }
        }
        
        // Configurando o label da pontuação
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setOpaque(true);
        updateScore();

        // Configurando o painel da pontuação
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BorderLayout());
        scorePanel.setBounds(0, 0, 800, 50);
        scorePanel.add(scoreLabel);
        
        // Adicionando os painéis à janela do jogo
        frame.add(panel, BorderLayout.NORTH);
        frame.add(btPanel);
        frame.add(scorePanel, BorderLayout.SOUTH);
    }

    // Método para atualizar a pontuação
    private void updateScore() {
        scoreLabel.setText("X - " + scoreX + " | O - " + scoreO);
        
        // Altera a cor do painel de pontuação para a cor do jogador com maior pontuação, caso o jogo esteja empatado, para cinza
        scoreLabel.setBackground(scoreX > scoreO ? X_COLOR : (scoreX == scoreO ? Color.GRAY : O_COLOR));
    }

    // Método para animar os botões
    private void blink(JButton b1, JButton b2, JButton b3, Color color) {
        
        // Removendo todos os action listeners do timer
        ActionListener[] listeners = timer.getActionListeners();
        for (ActionListener listener : listeners) {
            timer.removeActionListener(listener);
        }
        
        // Adicionando um novo action listener ao timer
        timer.addActionListener(e -> {
            
            // Alterando a cor de fundo dos botões
            if (b1.getBackground().equals(color)){
                b1.setBackground(Color.WHITE);
                b2.setBackground(Color.WHITE);
                b3.setBackground(Color.WHITE);
            } else {
                b1.setBackground(color);
                b2.setBackground(color);
                b3.setBackground(color);
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    // Método para verificar se há um vencedor
    public void checkWinner() {
        
        // Verificando linhas e colunas
        for (int i = 0; i < 3; i++) {
            if (checkLine(buttons[i][0], buttons[i][1], buttons[i][2])) return;
            if (checkLine(buttons[0][i], buttons[1][i], buttons[2][i])) return;
        }
        
        // Verificando as diagonais
        if (checkLine(buttons[0][0], buttons[1][1], buttons[2][2])) return;
        if (checkLine(buttons[0][2], buttons[1][1], buttons[2][0])) return;

        // Verificando se o jogo terminou em empate
        boolean tie = true;
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                if (button.getText().isEmpty()) {
                    tie = false;
                    break;
                }
            }
            if (!tie) break;
        }

        // Atualizando o label do turno e resetando o jogo caso termine em empate
        if (tie) {
            turn.setText("EMPATE");
            turn.setBackground(new Color(82, 82, 82));
            for (JButton[] row : buttons) {
                for (JButton button : row) {
                    button.setBackground(new Color(140, 140, 140));
                }
            }
            resetGame("EMPATE");
        }
    }

    // Método para verificar uma linha vencedora
    private boolean checkLine(JButton b1, JButton b2, JButton b3) {
        
        // Se os botões tem o mesmo texto e não estão vazios, então é uma linha vencedora
        if (b1.getText().equals(b2.getText()) && b1.getText().equals(b3.getText()) && !b1.getText().isEmpty()) {
            Color color;
            if (b1.getText().equals(X)) {
                color = new Color(255, 162, 136);
            } else {
                color = new Color(112, 177, 229);
            }
            turn.setText("VITÓRIA DO " + b1.getText());
            blink(b1, b2, b3, color);
            resetGame(b1.getText() + " VENCEU!");
            return true;
        }
        return false;
    }

    // Método para resetar o jogo
    public void resetGame(String s){
        
        turn.setBackground(!"EMPATE".equals(s) ? new Color(28, 217, 47) : turn.getBackground());

        // Atualizando a pontuação do vencedor
        if (s.equals(X + " VENCEU!")) {
            scoreX++;
        } else if (s.equals(O + " VENCEU!")) {
            scoreO++;
        }
        updateScore();

        // Caixa de diálogo que exibe o resultado do jogo e pergunta se o usuário quer jogar novamente
        Object[] option={"Continuar", "Sair"};
        int n = JOptionPane.showOptionDialog(frame, s,
                "Jogo da Velha", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, option, option[0]);
        
        // Se o usuário escolher continuar, o jogo é resetado, caso contrário o jogo é encerrado
        if (n == JOptionPane.YES_OPTION) {
            timer.stop();
            turn.setForeground(Color.WHITE);
            turn.setBackground(xTurn ? X_COLOR : O_COLOR);
            for (JButton[] row : buttons) {
                for (JButton button : row) {
                    button.setText("");
                    button.setBackground(Color.WHITE);
                    button.setEnabled(true);
                }
            }
        } else {
            System.exit(0);
        }
    }
}
