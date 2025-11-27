package escuelasframe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class EscuelasFrame extends javax.swing.JFrame {

   public EscuelasFrame() {
       initComponents();
       setLocationRelativeTo(null);
       cargarEscuelas();
    }

 

    private void cargarEscuelas() {
        DefaultTableModel model = (DefaultTableModel) tablaEscuelas.getModel();
        model.setRowCount(0); 

       String sql = "SELECT id_escuela, nombre, nivel, direccion, ruta FROM escuelas";

       try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

             while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("id_escuela");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("nivel");
                fila[3] = rs.getString("direccion");
                fila[4] = rs.getString("ruta");
                model.addRow(fila);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar escuelas: " + e.getMessage());
        }
    }

    private void agregarEscuela() {
      String textoId   = txtId.getText().trim();
      String nombre    = txtNombre.getText().trim();
      String nivelIn = comboNivel.getSelectedItem().toString();
      String direccion = txtDireccion.getText().trim();
      String ruta      = txtRuta.getText().trim();

       if (textoId.isEmpty() || nombre.isEmpty() || nivelIn.isEmpty()) {
           JOptionPane.showMessageDialog(this,
                "ID, Nombre y Nivel son obligatorios.\n" +
                "Nivel debe ser: PRE, PRI, SEC, MS o SUP.");
          return;
        }

      int id;
        try {
            id = Integer.parseInt(textoId);
        } catch (NumberFormatException e) {
           JOptionPane.showMessageDialog(this, "ID debe ser un número entero.");
        return;
        }
       String nivel = nivelIn;

       String sql = "INSERT INTO escuelas (id_escuela, nombre, nivel, direccion, ruta) "
            + "VALUES (?, ?, ?, ?, ?)";

       try (Connection con = ConexionBD.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id);
        ps.setString(2, nombre);
        ps.setString(3, nivel);      
        ps.setString(4, direccion);
        ps.setString(5, ruta);

       int filas = ps.executeUpdate();
        if (filas > 0) {
            JOptionPane.showMessageDialog(this, "Escuela agregada correctamente.");
            limpiarCampos();
            cargarEscuelas();
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al agregar escuela: " + e.getMessage());
     }
    }



    private void actualizarEscuela() {
       String textoId = txtId.getText().trim();
        if (textoId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona una escuela de la tabla para actualizar.");
            return;
       }

       int id;
        try {
           id = Integer.parseInt(textoId);
        } catch (NumberFormatException e) {
           JOptionPane.showMessageDialog(this, "ID inválido: " + textoId);
           return;
        }

       String nombre = txtNombre.getText().trim();
       String nivel = comboNivel.getSelectedItem().toString();
       String direccion = txtDireccion.getText().trim();
       String ruta = txtRuta.getText().trim();

        if (nombre.isEmpty() || nivel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y nivel son obligatorios.");
            return;
        }

        String sql = "UPDATE escuelas SET nombre = ?, nivel = ?, direccion = ?, ruta = ? WHERE id_escuela = ?";

        try (Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, nivel);
            ps.setString(3, direccion);
            ps.setString(4, ruta);
            ps.setInt(5, id);

           int filas = ps.executeUpdate();
           if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Escuela actualizada correctamente.");
                limpiarCampos();
                cargarEscuelas();
           } else {
                JOptionPane.showMessageDialog(this, "No se encontró la escuela a actualizar.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar escuela: " + e.getMessage());
        }
    }

    private void eliminarEscuela() {
       int filaSeleccionada = tablaEscuelas.getSelectedRow();
        if (filaSeleccionada == -1) {
           JOptionPane.showMessageDialog(this, "Seleccione una escuela en la tabla.");
           return;
        }

       Object valorId = tablaEscuelas.getValueAt(filaSeleccionada, 0);
       int id;
       try {
           id = Integer.parseInt(valorId.toString());
       } catch (NumberFormatException e) {
           JOptionPane.showMessageDialog(this, "ID inválido en la tabla: " + valorId);
            return;
       }

        int confirmar = JOptionPane.showConfirmDialog(this,
           "¿Seguro que deseas eliminar la escuela con ID " + id + "?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

       if (confirmar != JOptionPane.YES_OPTION) {
           return;
       }

       String sql = "DELETE FROM escuelas WHERE id_escuela = ?";

       try (Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

           ps.setInt(1, id);
           int filas = ps.executeUpdate();

            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Escuela eliminada correctamente.");
                cargarEscuelas();
                limpiarCampos();
           } else {
                JOptionPane.showMessageDialog(this, "No se encontró la escuela.");
            }

       } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar escuela: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
       txtId.setText("");
       txtNombre.setText("");
       comboNivel.setSelectedIndex(0);
       txtDireccion.setText("");
       txtRuta.setText("");
    }
    

    @SuppressWarnings("unchecked")
    private void initComponents() {

       jScrollPane1 = new javax.swing.JScrollPane();
       tablaEscuelas = new javax.swing.JTable();
       jLabel1 = new javax.swing.JLabel();
       txtId = new javax.swing.JTextField();
       jLabel2 = new javax.swing.JLabel();
       txtNombre = new javax.swing.JTextField();
       jLabel3 = new javax.swing.JLabel();
       comboNivel = new javax.swing.JComboBox<>();
       comboNivel.setModel(new javax.swing.DefaultComboBoxModel<>(
       new String[] { "PRE", "PRI", "SEC", "MS", "SUP" }
       ));
       jLabel4 = new javax.swing.JLabel();
       txtDireccion = new javax.swing.JTextField();
       jLabel5 = new javax.swing.JLabel();
       txtRuta = new javax.swing.JTextField();
       btnAgregar = new javax.swing.JButton();
       btnActualizar = new javax.swing.JButton();
       btnEliminar = new javax.swing.JButton();
       btnLimpiar = new javax.swing.JButton();
       btnRecargar = new javax.swing.JButton();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
       setTitle("Gestión de Escuelas - Regiduría");

       tablaEscuelas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

           },
            new String [] {
                "ID", "Nombre", "Nivel", "Dirección", "Ruta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class,
                java.lang.String.class, java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaEscuelas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaEscuelasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaEscuelas);

       jLabel1.setText("ID:");

       txtId.setEditable(true); 

       jLabel2.setText("Nombre:");

       jLabel3.setText("Nivel (PRE/PRI/SEC/MS/SUP):");

       jLabel4.setText("Dirección:");

       jLabel5.setText("Ruta (tramo):");

       btnAgregar.setText("Agregar");
       btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

       btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

       btnEliminar.setText("Eliminar");
       btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

       btnLimpiar.setText("Limpiar");
       btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

       btnRecargar.setText("Recargar");
       btnRecargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecargarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNombre))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboNivel))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDireccion))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtRuta)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                    .addComponent(btnRecargar, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActualizar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(comboNivel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRecargar))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }

   private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {
       agregarEscuela();
   }

   private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {
        actualizarEscuela();
   }

   private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        eliminarEscuela();
   }

   private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {
        limpiarCampos();
   }

    private void btnRecargarActionPerformed(java.awt.event.ActionEvent evt) {
        cargarEscuelas();
    }

    private void tablaEscuelasMouseClicked(java.awt.event.MouseEvent evt) {
        int fila = tablaEscuelas.getSelectedRow();
        if (fila != -1) {
            txtId.setText(tablaEscuelas.getValueAt(fila, 0).toString());
            txtNombre.setText(tablaEscuelas.getValueAt(fila, 1).toString());
            comboNivel.setSelectedItem(tablaEscuelas.getValueAt(fila, 2).toString());
            Object dir = tablaEscuelas.getValueAt(fila, 3);
            Object ruta = tablaEscuelas.getValueAt(fila, 4);
            txtDireccion.setText(dir != null ? dir.toString() : "");
            txtRuta.setText(ruta != null ? ruta.toString() : "");
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EscuelasFrame().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRecargar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaEscuelas;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtId;
    private javax.swing.JComboBox<String> comboNivel;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtRuta;
}
