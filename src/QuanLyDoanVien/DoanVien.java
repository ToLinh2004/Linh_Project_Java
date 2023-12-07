package QuanLyDoanVien;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
public class DoanVien extends JFrame implements ActionListener , MouseListener {
    private JLabel maSo, hoTen, gioiTinh, doanPhi, chiDoan, chucVu, ngay, ngayDoan, diaChi, timKiem;
    private JComboBox<String> chi_Doan;
    private JComboBox<String> chuc_Vu;
    private JTextField id, ho_ten, gioi_tinh, doan_phi, date, date_doan, dia_chi, tim_kiem;
    private JButton btnadd, btnupdate, btndelete, btnsearch, btnclear;
    private JTable table;
    private DefaultTableModel dtb;

    public DoanVien(String title) {
        super(title);

        JPanel pnlTop = new JPanel(new GridLayout(1, 3));
        
       
        pnlTop.add(timKiem = new JLabel("Tìm kiếm chi đoàn"));
        pnlTop.add(tim_kiem = new JTextField(20));  //20 characters
        
        JPanel search = new JPanel();
        search.add(btnsearch = new JButton("Search"));
        pnlTop.add(search);
        
      

        JPanel pnlLeft = new JPanel(new GridLayout(0, 1));
        pnlLeft.add(new JLabel("THÔNG TIN ĐOÀN VIÊN", JLabel.CENTER));
        pnlLeft.add(maSo = new JLabel("Mã Đoàn Viên"));
        pnlLeft.add(id = new JTextField(20));
        id.setEditable(false);
       
        pnlLeft.add(hoTen = new JLabel("Họ Và Tên"));
        pnlLeft.add(ho_ten = new JTextField(20));
        pnlLeft.add(gioiTinh = new JLabel("Giới Tính"));
        pnlLeft.add(gioi_tinh = new JTextField(20));
        pnlLeft.add(doanPhi = new JLabel("Đoàn Phí"));
        pnlLeft.add(doan_phi = new JTextField(20));

        pnlLeft.add(chiDoan = new JLabel("Chi Đoàn"));
        String[] cacChiDoan = new String[]{"A", "B", "C"};
        pnlLeft.add(chi_Doan = new JComboBox<String>(cacChiDoan));
        pnlLeft.add(chucVu = new JLabel("Chức Vụ"));
        String[] cacChucVu = new String[]{"Bí Thư", "Đoàn Viên"};
        pnlLeft.add(chuc_Vu = new JComboBox<String>(cacChucVu));

        pnlLeft.add(ngay = new JLabel("Ngày Tháng Năm Sinh"));
        pnlLeft.add(date = new JTextField());
        pnlLeft.add(ngayDoan = new JLabel("Ngày Vào Đoàn"));
        pnlLeft.add(date_doan = new JTextField(20));
        pnlLeft.add(diaChi = new JLabel("Địa Chỉ"));
        pnlLeft.add(dia_chi = new JTextField(20));
    
        
        JPanel	pnlLeftBottom;
        pnlLeft.add(pnlLeftBottom=new JPanel());  
        pnlLeftBottom.add(btnadd = new JButton("Add"));
        pnlLeftBottom.add(btnupdate = new JButton("Update"));   
       
        JPanel	pnlLeftBottom2;
        pnlLeft.add(pnlLeftBottom2=new JPanel());
        pnlLeftBottom2.add(btndelete = new JButton("Delete"));
        pnlLeftBottom2.add(btnclear = new JButton("Clear"));

        JPanel	pnlRight=new JPanel(new GridLayout(1,1));
    	String header[]={"Mã Đoàn Viên","Họ Và Tên","Giới Tính","Đoàn Phí","Chi Đoàn","Chức Vụ","Ngày Tháng Năm Sinh","Ngày Vào Đoàn","Địa Chỉ"};
    	pnlRight.add(new JScrollPane(table = new JTable(
    	dtb=new DefaultTableModel(header,0))));
    	add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pnlLeft,pnlRight));
    	    table.setModel(dtb);

    	    // Load data into the table
    	    loadData();
    	
        add(pnlTop, BorderLayout.NORTH);
        add(pnlLeft, BorderLayout.WEST);
        add(pnlRight, BorderLayout.CENTER);

        btnadd.addActionListener(this);
        btnupdate.addActionListener(this);
        btndelete.addActionListener(this);
        btnsearch.addActionListener(this);
        btnclear.addActionListener(this);
        
// thiêt  lap mau sac TField
      
        ho_ten.setBackground(Color.PINK);
        gioi_tinh.setBackground(Color.PINK);
        doan_phi.setBackground(Color.PINK);
        date.setBackground(Color.PINK);
        date_doan.setBackground(Color.PINK);
        dia_chi.setBackground(Color.PINK);
        tim_kiem.setBackground(Color.PINK);
        chi_Doan.setBackground(Color.PINK);
        chuc_Vu.setBackground(Color.PINK);
        
        table.setBackground(Color.yellow);
     // thiêt  lap mau sac button
        btnadd.setBackground(Color.GREEN);
        btndelete.setBackground(Color.GREEN);
        btnclear.setBackground(Color.GREEN);
        btnupdate.setBackground(Color.GREEN);
        
  
     // thiet lap do dam 
 		id.setFont(id.getFont().deriveFont(Font.BOLD));
 		ho_ten.setFont(id.getFont().deriveFont(Font.BOLD));
 		gioi_tinh.setFont(id.getFont().deriveFont(Font.BOLD));
 		doan_phi.setFont(id.getFont().deriveFont(Font.BOLD));
 		date.setFont(id.getFont().deriveFont(Font.BOLD));
 		date_doan.setFont(id.getFont().deriveFont(Font.BOLD));
 		dia_chi.setFont(id.getFont().deriveFont(Font.BOLD));
 		tim_kiem.setFont(id.getFont().deriveFont(Font.BOLD));
    //  	
 		tim_kiem.setFont(new Font("Arial", Font.PLAIN, 25));
 		//
        setVisible(true);
//        setLocation(200, 500);
        setSize(1000, 800);
    }

    
    public void loadData() {
        try {
            // Create a database connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/qldoanvien", "root", "mysql");

            // Create a statement
            Statement st = conn.createStatement();

            // Execute a query
            String sql= "SELECT Doan_Vien.Id, Doan_Vien.ho_ten, Doan_Vien.gioi_tinh,Doan_Vien.so_tien,Doan_Vien.chi_doan,Doan_Vien.chuc_vu,Doan_Vien.ngay_sinh,Doan_Vien.ngay_vao_doan,Doan_Vien.dia_chi FROM Doan_Vien";
            ResultSet rs = st.executeQuery(sql);

            dtb.setRowCount(0);

            // Add new data to the table
            while (rs.next()) {
                Object[] row = new Object[9]; 
                for (int i = 0; i < 9; i++) {
                    row[i] = rs.getString(i + 1); 
                }
                dtb.addRow(row);
            }

            // Close the resources
            rs.close();
            st.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        table.addMouseListener(this);
    }
    public void xoatrang() {
    	id.setText("");
        ho_ten.setText("");
        gioi_tinh.setText("");
        doan_phi.setText("");
        date.setText("");
        date_doan.setText("");
        dia_chi.setText("");
        loadData();
    }

    @Override
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add")) {
            try {
                if (ho_ten.getText().equals("") || gioi_tinh.getText().equals("") ||
                        chi_Doan.getSelectedItem().equals("") || chuc_Vu.getSelectedItem().equals("") ||
                        date.getText().equals("") || date_doan.getText().equals("") || dia_chi.getText().equals("")) {
                    JOptionPane.showMessageDialog(this, "Bạn cần nhập đủ dữ liệu");
                } else {
                    // Kết nối đến cơ sở dữ liệu
                	Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/qldoanvien", "root", "mysql");
                    Statement st = conn.createStatement();

                    String dateindoan = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                    // thêm đoàn viên
                    String sqlInsert = "INSERT INTO Doan_Vien ( ho_ten,gioi_tinh,so_tien,chi_doan,chuc_vu,ngay_sinh,ngay_vao_doan,dia_chi,ngay_nop_phi) VALUES ('"+ho_ten.getText()+"', '"+gioi_tinh.getText()+"', '"+doan_phi.getText()+"','"+chi_Doan.getSelectedItem()+"', '"+chuc_Vu.getSelectedItem()+"', '"+date.getText()+"', '"+date_doan.getText()+"', '"+dia_chi.getText()+"', '"+dateindoan+"');";
                    st.execute(sqlInsert);
                    xoatrang();
                    
                }
               
            } catch (SQLException | NumberFormatException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        if (e.getActionCommand().equals("Delete")) {
            try {
                if (id.getText().equals("")) {
                    JOptionPane.showMessageDialog(this, "Bạn cần nhập ID để xóa đoàn viên");
                } else {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/qldoanvien", "root", "mysql");
                    Statement st = conn.createStatement();
                    
                    // delete đoàn viên
                    String sqlDelete = "DELETE FROM Doan_Vien WHERE id = '" + id.getText() + "'";
                    int kq = st.executeUpdate(sqlDelete);
                    
                    if (kq > 0) {
                        JOptionPane.showMessageDialog(this, "Xóa thành công");
                        loadData();
                        xoatrang();
                    } else {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy đoàn viên để xóa");
                    }
                }
            } catch (ClassNotFoundException | SQLException e1) {
                e1.printStackTrace();
            }
        }
        
        if(e.getActionCommand().equals("Update")) {
        	try {
        		if (ho_ten.getText().equals("") || gioi_tinh.getText().equals("") ||
                        chi_Doan.getSelectedItem().equals("") || chuc_Vu.getSelectedItem().equals("") ||
                        date.getText().equals("") || date_doan.getText().equals("") || dia_chi.getText().equals("")) {
                    JOptionPane.showMessageDialog(this, "Bạn cần nhập đủ dữ liệu");
                    }
                    else {
                    	Class.forName("com.mysql.jdbc.Driver");
      			      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/qldoanvien", "root", "mysql");
      		          Statement st = conn.createStatement();
      		           String dateindoan = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
      		            // Thực hiện thêm mới đoàn viên
      		           String sqlUpdate = "UPDATE Doan_Vien SET ho_ten = '" + ho_ten.getText() + "', gioi_tinh = '" + gioi_tinh.getText() + "', so_tien = '" + doan_phi.getText() + "', chi_doan = '" + chi_Doan.getSelectedItem() + "', chuc_vu = '" + chuc_Vu.getSelectedItem() + "', ngay_sinh = '" + date.getText() + "', ngay_vao_doan = '" + date_doan.getText() + "', dia_chi = '" + dia_chi.getText() + "', ngay_nop_phi = '" + dateindoan + "' WHERE id = '"+id.getText()+"'";		        
      		           st.execute(sqlUpdate);
      		           xoatrang();
                    }
				
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        if(e.getActionCommand().equals("Clear")) {
        	xoatrang();
        }
        if(e.getActionCommand().equals("Search")) {
        	
        	Search(tim_kiem.getText());
        }
      }
    public  void Search(String keyword) {
    	try {
            // Create a database connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/qldoanvien", "root", "mysql");

            // Create a statement
            Statement st = conn.createStatement();

            // Execute a query
            String sql = "SELECT Doan_Vien.Id, Doan_Vien.ho_ten, Doan_Vien.gioi_tinh, Doan_Vien.so_tien, Doan_Vien.chi_doan, Doan_Vien.chuc_vu, Doan_Vien.ngay_sinh, Doan_Vien.ngay_vao_doan, Doan_Vien.dia_chi FROM Doan_Vien WHERE chi_doan LIKE '%" + keyword + "%'";
            ResultSet rs = st.executeQuery(sql);
            System.out.println(sql);
            // xoa du lieu cu trong bang

            dtb.setRowCount(0);
          
            while (rs.next()) {
                Object[] row = new Object[9]; 
                for (int i = 0; i < 9; i++) {
                    row[i] = rs.getString(i + 1); 
                }
                dtb.addRow(row);
            }
            
            rs.close();
            st.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
     
    }
     

	@Override
	public void mouseClicked(MouseEvent e) {
		
	    if (table.getSelectedRow() >= 0) {
	        id.setText(dtb.getValueAt(table.getSelectedRow(), 0) + "");
	        id.setEnabled(true);
	        ho_ten.setText(table.getValueAt(table.getSelectedRow(), 1) + "");
	        gioi_tinh.setText(table.getValueAt(table.getSelectedRow(), 2) + "");
	        doan_phi.setText(table.getValueAt(table.getSelectedRow(), 3) + "");
	        date.setText(table.getValueAt(table.getSelectedRow(), 6) + "");
	        date_doan.setText(table.getValueAt(table.getSelectedRow(), 7) + "");
	        dia_chi.setText(table.getValueAt(table.getSelectedRow(), 8) + "");
	       
	    }
	    
	}
	

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		
		
        new DoanVien("Hi");
    }
}