package proyectomysql;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Ventana extends JFrame {
    JTextField txtSql;
    JTextArea areaResultados;
    JButton btnConsulta;

    public Ventana(String titulo) {
        super(titulo);

        txtSql = new JTextField("SELECT * FROM Productos", 30);
        areaResultados = new JTextArea(15, 40);
        btnConsulta = new JButton("Ejecutar SQL");
        JScrollPane scrollPanel = new JScrollPane(areaResultados);

        btnConsulta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ejecutarConsulta(txtSql.getText());
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(txtSql);
        panel.add(scrollPanel);
        panel.add(btnConsulta);

        getContentPane().add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Centrar ventana
        setVisible(true);
    }

    private void ejecutarConsulta(String sql) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/EmpresaMSQL?serverTimezone=UTC&useSSL=false";

            Connection conexion = DriverManager.getConnection(url, "root", "user1234"); // Cambia usuario y pass
            Statement sentencia = conexion.createStatement();

            boolean tieneResultados = sentencia.execute(sql);

            if (tieneResultados) {
                ResultSet resultado = sentencia.getResultSet();
                mostrarResultados(resultado);
            } else {
                areaResultados.setText("Sentencia ejecutada correctamente, sin resultados.");
            }

            conexion.close();
        } catch (Exception ex) {
            areaResultados.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void mostrarResultados(ResultSet r) throws SQLException {
        ResultSetMetaData meta = r.getMetaData();
        int columnas = meta.getColumnCount();
        StringBuilder texto = new StringBuilder();

        for (int i = 1; i <= columnas; i++) {
            texto.append(meta.getColumnName(i)).append("\t");
        }
        texto.append("\n");

        while (r.next()) {
            for (int i = 1; i <= columnas; i++) {
                texto.append(r.getString(i)).append("\t");
            }
            texto.append("\n");
        }

        areaResultados.setText(texto.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Ventana("Consulta MySQL con Connector/J 9.2.0");
        });
    }
}
