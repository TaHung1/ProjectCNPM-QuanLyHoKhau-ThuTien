package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.HoKhauModel;
import models.KhoanThuModel;
import models.NopTienModel;

public class NopTienService {
	private List<KhoanThuModel> listKhoanThu;
	Map<Integer, String> mapmaktTotenkt;
	
	
	// checked
	public boolean add(NopTienModel nopTienModel) throws ClassNotFoundException, SQLException {
		Connection connection = MysqlConnection.getMysqlConnection();
        String query = "INSERT INTO nop_tien(IDNopTien, MaKhoanThu, NgayThu)" 
                    + " values (?, ?, NOW())";
        
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1,nopTienModel.getIdNopTien());
        preparedStatement.setInt(2, nopTienModel.getMaKhoanThu());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
		return true;
	}
	
	// checked
	public boolean del(int idNopTien, int maKhoanThu) throws ClassNotFoundException, SQLException {
		listKhoanThu = new KhoanThuService().getListKhoanThu();
		mapmaktTotenkt = new HashMap<>();
		listKhoanThu.forEach(khoanthu -> {
			mapmaktTotenkt.put(khoanthu.getMaKhoanThu(), khoanthu.getTenKhoanThu());
		});
		String check = mapmaktTotenkt.get(maKhoanThu);
		String sql = "DELETE FROM nop_tien WHERE IDNopTien = '" +idNopTien + "' AND MaKhoanThu = '" +maKhoanThu + "';";
		String sql2 = "UPDATE ho_khau SET checkNoptien = REPLACE(checkNoptien,', " + check + "','') where MaHo = " + idNopTien + ";";    
		String sql3 = "UPDATE ho_khau SET checkNoptien = REPLACE(checkNoptien,'" + check + ",','') where MaHo = " + idNopTien + ";";
		Connection connection = MysqlConnection.getMysqlConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
        PreparedStatement preparedStatement3 = connection.prepareStatement(sql3);
        preparedStatement.executeUpdate();
        preparedStatement2.executeUpdate();
        preparedStatement3.executeUpdate();
        return true;
	}
	
	// checked
	public List<NopTienModel> getListNopTien() throws ClassNotFoundException, SQLException{
		List<NopTienModel> list = new ArrayList<>();
		Connection connection = MysqlConnection.getMysqlConnection();
    	String query = "SELECT * FROM nop_tien";
        PreparedStatement preparedStatement = (PreparedStatement)connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
        	NopTienModel nopTienModel = new NopTienModel();
        	nopTienModel.setIdNopTien(rs.getInt("IDNopTien"));
        	nopTienModel.setMaKhoanThu(rs.getInt("MaKhoanThu"));
        	nopTienModel.setNgayThu(rs.getDate("NgayThu"));
        	list.add(nopTienModel);
        }
        preparedStatement.close();
        connection.close();
		return list;
	}
	
}
