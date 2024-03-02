import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class CalculadoraDistanciasEuclidianas extends JFrame {

    private JPanel panelPuntos;
    private JPanel panelDistancias;
    private JTextField dimensionesField;
    private JTextField puntosField;
    private JPanel mainPanel;

    public CalculadoraDistanciasEuclidianas() {
        setTitle("Calculadora de Distancias Euclidianas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        JPanel panelEntrada = new JPanel(new GridLayout(2, 2, 10, 20));
        panelEntrada.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel dimensionesLabel = new JLabel("Tamaño de las dimensiones:");
        dimensionesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dimensionesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dimensionesField = new JTextField();
        dimensionesField.setFont(new Font("Arial", Font.PLAIN, 16));
        dimensionesField.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel puntosLabel = new JLabel("Número de puntos:");
        puntosLabel.setFont(new Font("Arial", Font.BOLD, 16));
        puntosLabel.setHorizontalAlignment(SwingConstants.CENTER);
        puntosField = new JTextField();
        puntosField.setFont(new Font("Arial", Font.PLAIN, 16));
        puntosField.setHorizontalAlignment(SwingConstants.CENTER);

        panelEntrada.add(dimensionesLabel);
        panelEntrada.add(dimensionesField);
        panelEntrada.add(puntosLabel);
        panelEntrada.add(puntosField);
        add(panelEntrada, BorderLayout.NORTH);

        JButton calcularButton = new JButton("Calcular");
        calcularButton.setBackground(new Color(59, 89, 182));
        calcularButton.setForeground(Color.WHITE);
        calcularButton.setFocusPainted(false);
        calcularButton.setFont(new Font("Arial", Font.BOLD, 16));
        calcularButton.addActionListener(e -> {
            int dimensiones = Integer.parseInt(dimensionesField.getText());
            int puntos = Integer.parseInt(puntosField.getText());
            generarInterfaz(dimensiones, puntos);
        });

        JButton regresarButton = new JButton("Regresar");
        regresarButton.setBackground(new Color(220, 20, 60));
        regresarButton.setForeground(Color.WHITE);
        regresarButton.setFocusPainted(false);
        regresarButton.setFont(new Font("Arial", Font.BOLD, 16));
        regresarButton.addActionListener(e -> {
            dimensionesField.setText("");
            puntosField.setText("");
            regresar();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(calcularButton);
        buttonPanel.add(regresarButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void generarInterfaz(int dimensiones, int puntos) {
        getContentPane().removeAll();
        revalidate();
        repaint();

        panelPuntos = new JPanel();
        panelPuntos.setLayout(new BoxLayout(panelPuntos, BoxLayout.Y_AXIS));

        panelDistancias = new JPanel();
        panelDistancias.setLayout(new BoxLayout(panelDistancias, BoxLayout.Y_AXIS));

        double[][] coordenadas = generarPuntos(dimensiones, puntos);

        mostrarPuntos(coordenadas);
        mostrarDistancias(coordenadas);

        JScrollPane scrollPanePuntos = new JScrollPane(panelPuntos);
        JScrollPane scrollPaneDistancias = new JScrollPane(panelDistancias);

        mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(scrollPanePuntos);
        mainPanel.add(scrollPaneDistancias);

        add(mainPanel);
        pack();
    }

    public void mostrarPuntos(double[][] coordenadas) {
        panelPuntos.add(new JLabel("Coordenadas de los puntos:"));
        for (int i = 0; i < coordenadas.length; i++) {
            StringBuilder puntoStr = new StringBuilder("Punto " + (i + 1) + ": ");
            for (int j = 0; j < coordenadas[0].length; j++) {
                puntoStr.append(coordenadas[i][j]).append(" ");
            }
            JLabel puntoLabel = new JLabel(puntoStr.toString());
            puntoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            panelPuntos.add(puntoLabel);
        }
    }

    public void mostrarDistancias(double[][] coordenadas) {
        panelDistancias.add(new JLabel("Distancias Euclidianas:"));
        for (int i = 0; i < coordenadas.length; i++) {
            for (int j = i + 1; j < coordenadas.length; j++) {
                double distancia = distanciaEuclidiana(coordenadas[i], coordenadas[j]);
                JLabel distanciaLabel = new JLabel("Distancia entre Punto " + (i + 1) + " y Punto " + (j + 1) + ": " + distancia);
                distanciaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                panelDistancias.add(distanciaLabel);
            }
        }
    }

    public double[][] generarPuntos(int dimensiones, int puntos) {
        double[][] coordenadas = new double[puntos][dimensiones];
        Random rand = new Random();

        for (int i = 0; i < puntos; i++) {
            for (int j = 0; j < dimensiones; j++) {
                coordenadas[i][j] = rand.nextInt(100); // Genera coordenadas enteras entre 0 y 99
            }
        }

        return coordenadas;
    }

    public double distanciaEuclidiana(double[] punto1, double[] punto2) {
        double sumaCuadrados = 0;
        for (int i = 0; i < punto1.length; i++) {
            sumaCuadrados += Math.pow(punto1[i] - punto2[i], 2);
        }
        return Math.sqrt(sumaCuadrados);
    }

    public void regresar() {
        getContentPane().removeAll();
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalculadoraDistanciasEuclidianas::new);
    }
}
